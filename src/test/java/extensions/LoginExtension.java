package extensions;

import annotations.WithLogin;
import api.models.LoginResponse;
import api.steps.AccountSteps;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.url;

public class LoginExtension implements BeforeEachCallback {

    public static final ThreadLocal<String> TOKEN = new ThreadLocal<>();
    public static final ThreadLocal<String> USER_ID = new ThreadLocal<>();

    private final AccountSteps accountSteps = new AccountSteps();

    @Override
    public void beforeEach(ExtensionContext context) {
        boolean needLogin = context.getRequiredTestMethod().isAnnotationPresent(WithLogin.class);
        if (!needLogin) return;

        String user = System.getenv("DEMOQA_USER");
        String pass = System.getenv("DEMOQA_PASS");

        if (user == null || pass == null) {
            throw new IllegalStateException("Env DEMOQA_USER / DEMOQA_PASS are not set");
        }

        LoginResponse loginResponse = accountSteps.login(user, pass);

        String token = loginResponse.getToken();
        String userId = loginResponse.getUserId();
        String expires = loginResponse.getExpires();

        if (token == null || token.isBlank()) throw new AssertionError("Token is empty after /Login");
        if (userId == null || userId.isBlank()) throw new AssertionError("UserId is empty after /Login");
        if (expires == null || expires.isBlank()) throw new AssertionError("Expires is empty after /Login");

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

    private void setStorage(String storageName, String token, String userId, String userName, String expires) {
        Selenide.executeJavaScript(storageName + ".setItem('token', arguments[0]);", token);
        Selenide.executeJavaScript(storageName + ".setItem('userID', arguments[0]);", userId);
        Selenide.executeJavaScript(storageName + ".setItem('userName', arguments[0]);", userName);
        Selenide.executeJavaScript(storageName + ".setItem('expires', arguments[0]);", expires);
    }
}










