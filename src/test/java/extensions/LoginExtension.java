package extensions;

import annotations.WithLogin;
import api.models.LoginResponse;
import api.steps.AccountSteps;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.url;

public class LoginExtension implements BeforeEachCallback, AfterEachCallback {

    public static final ThreadLocal<String> TOKEN = new ThreadLocal<>();
    public static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    private final AccountSteps accountSteps = new AccountSteps();

    @Override
    public void beforeEach(ExtensionContext context) {
        if (!isLoginRequired(context)) return;
        loginAndPrepareSession();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        TOKEN.remove();
        USER_ID.remove();
    }

    private boolean isLoginRequired(ExtensionContext context) {
        return context.getRequiredTestMethod().isAnnotationPresent(WithLogin.class);
    }

    private void loginAndPrepareSession() {
        String user = System.getenv("DEMOQA_USER");
        String pass = System.getenv("DEMOQA_PASS");

        if (user != null) user = user.trim();
        if (pass != null) pass = pass.trim();

        if (user == null || user.isBlank() || pass == null || pass.isBlank()) {
            throw new IllegalStateException("Env DEMOQA_USER / DEMOQA_PASS are not set (or blank)");
        }

        LoginResponse loginResponse = accountSteps.loginWithFreshToken(user, pass);

        String token = loginResponse.getToken();
        String userId = loginResponse.getUserId();
        String expires = loginResponse.getExpires();

        assertNotBlank(token, "Token is empty after /Login");
        assertNotBlank(userId, "UserId is empty after /Login");
        assertNotBlank(expires, "Expires is empty after /Login");

        accountSteps.checkAuthorized(user, pass);
        accountSteps.checkUserByToken(userId, token);

        TOKEN.set(token);
        USER_ID.set(userId);

        open("/favicon.ico");

        getWebDriver().manage().addCookie(new Cookie("userID", userId));
        getWebDriver().manage().addCookie(new Cookie("token", token));
        getWebDriver().manage().addCookie(new Cookie("expires", expires));

        Selenide.refresh();
        open("/profile");

        System.out.println("After login, current url = " + url());
    }

    private void assertNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new AssertionError(message);
        }
    }
}











