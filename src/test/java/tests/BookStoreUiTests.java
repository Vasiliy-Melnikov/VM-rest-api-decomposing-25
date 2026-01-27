package tests;

import annotations.WithLogin;
import api.steps.BookStoreSteps;
import extensions.LoginExtension;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;
import ui.pages.ProfilePage;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookStoreUiTests extends TestBase {

    private final BookStoreSteps bookStoreSteps = new BookStoreSteps();
    private final ProfilePage profilePage = new ProfilePage();

    @Test
    @WithLogin
    void deleteBookFromProfileTest() {
        String userId = LoginExtension.USER_ID.get();
        String token = LoginExtension.TOKEN.get();

        assertNotNull(userId, "UserId was not set in LoginExtension (USER_ID.get() is null)");
        assertNotNull(token, "Token was not generated in LoginExtension (TOKEN.get() is null)");

        String isbn = "9781449325862";
        String bookName = "Git Pocket Guide";

        prepareUserBooks(userId, token, isbn);

        profilePage
                .openPage()
                .shouldSeeBook(bookName)
                .deleteBookFromList(bookName)
                .shouldNotSeeBook(bookName);
    }

    @Step("API: Prepare user books (clear collection and add isbn='{isbn}')")
    private void prepareUserBooks(String userId, String token, String isbn) {
        bookStoreSteps.deleteAllBooks(userId, token);
        bookStoreSteps.addBook(userId, token, isbn);
    }
}


