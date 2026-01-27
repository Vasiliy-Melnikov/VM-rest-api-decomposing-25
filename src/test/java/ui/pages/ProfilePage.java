package ui.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;

public class ProfilePage {

    private final SelenideElement loginWarning = $(".text-danger");
    private final SelenideElement userNameValue = $("#userName-value");
    private final SelenideElement booksTable = $(".rt-table");

    @Step("UI: Open profile page")
    public ProfilePage openPage() {
        open("/profile");

        System.out.println("open(/profile) url = " + url());
        System.out.println("title = " + title());

        if (url().contains("/login")) {
            throw new AssertionError("Not logged in: redirected to /login");
        }

        if (loginWarning.exists() && loginWarning.getText().toLowerCase().contains("please login")) {
            throw new AssertionError("Not logged in: /profile opened, but page shows 'Please login'");
        }

        userNameValue.shouldBe(visible);
        booksTable.shouldBe(visible);

        return this;
    }

    @Step("UI: Verify book '{bookName}' is present")
    public ProfilePage shouldSeeBook(String bookName) {
        booksTable.shouldHave(text(bookName));
        return this;
    }

    @Step("UI: Delete book '{bookName}' from profile")
    public ProfilePage deleteBookFromList(String bookName) {
        SelenideElement row = $$(".rt-tbody .rt-tr-group")
                .findBy(text(bookName))
                .shouldBe(exist);
        row.$("span[title='Delete']")
                .shouldBe(visible)
                .click();

        $("#closeSmallModal-ok").shouldBe(visible).click();
        return this;
    }

    @Step("UI: Verify book '{bookName}' is not present")
    public ProfilePage shouldNotSeeBook(String bookName) {
        booksTable.shouldNotHave(text(bookName));
        return this;
    }
}










