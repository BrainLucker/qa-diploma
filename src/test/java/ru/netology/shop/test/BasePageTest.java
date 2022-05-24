package ru.netology.shop.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;

import org.junit.jupiter.api.*;
import ru.netology.shop.db.DbInteraction;
import ru.netology.shop.page.BuyTourPage;
import ru.netology.shop.page.TourOfTheDayPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.shop.data.DataGenerator.*;
import static ru.netology.shop.data.TestData.Regex;

@SuppressWarnings({"UnusedDeclaration"})
public abstract class BasePageTest {
    static final DbInteraction db = new DbInteraction();
    TourOfTheDayPage tourOfTheDayPage;
    BuyTourPage buyTourPage;
    CardInfo card;

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    public void setup() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "800x1000";
        tourOfTheDayPage = open("http://localhost:8080", TourOfTheDayPage.class);
    }

    public abstract void shouldBuyTourIfValidApprovedCard();

    public abstract void shouldBuyTourIfApprovedExpiringSoonCard();

    public abstract void shouldBuyTourIfValidHolderName(String holderName);

    public abstract void shouldChangePage();

    public abstract void shouldShowErrorIfValidDeclinedCard();

    public abstract void shouldShowErrorIfInputtedMonthIsNotAllowable(int shiftYears, int shiftMonths, String errorText);

    public abstract void shouldShowErrorIfInputtedYearIsNotAllowable(int shiftYears, int shiftMonths, String errorText);

    public abstract void shouldShowErrorIfInputtedInvalidNumber(String cardNumber, String errorText);

    public abstract void shouldShowErrorIfInputtedInvalidMonth(String cardMonth, String errorText);

    public abstract void shouldShowErrorIfInputtedInvalidYear(String cardYear, String errorText);

    public abstract void shouldShowErrorIfInputtedInvalidHolder(String holderName, String errorText);

    public abstract void shouldShowErrorIfInputtedInvalidCode(String code, String errorText);

    public abstract void shouldShowErrorIfSendEmptyForm();

    @DisplayName("Проверка допустимой длины поля «Номер карты»")
    @Test
    public void shouldLimitInputOfNumberField() {
        var actualFieldMaxLength = buyTourPage.getNumberFieldMaxLength();

        assertEquals(19, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «Месяц»")
    @Test
    public void shouldLimitInputOfMonthField() {
        var actualFieldMaxLength = buyTourPage.getMonthFieldMaxLength();

        assertEquals(2, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «Год»")
    @Test
    public void shouldLimitInputOfYearField() {
        var actualFieldMaxLength = buyTourPage.getYearFieldMaxLength();

        assertEquals(2, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «Владелец»")
    @Test
    public void shouldLimitInputOfHolderField() {
        var actualFieldMaxLength = buyTourPage.getHolderFieldMaxLength();

        assertEquals(27, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «CVC/CVV»")
    @Test
    public void shouldLimitInputOfCodeField() {
        var actualFieldMaxLength = buyTourPage.getCodeFieldMaxLength();

        assertEquals(3, actualFieldMaxLength);
    }

    @DisplayName("Проверка плейсхолдера поля «Номер карты»")
    @Test
    public void shouldShowValidPlaceholderInNumberField() {
        buyTourPage.inputNumber(null);
        var actualPlaceholder = buyTourPage.getNumberFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.numberPlaceholder));
    }

    @DisplayName("Проверка плейсхолдера поля «Месяц»")
    @Test
    public void shouldShowValidPlaceholderInMonthField() {
        buyTourPage.inputMonth(null);
        var actualPlaceholder = buyTourPage.getMonthFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.monthPlaceholder));
    }

    @DisplayName("Проверка плейсхолдера поля «Год»")
    @Test
    public void shouldShowValidPlaceholderInYearField() {
        buyTourPage.inputYear(null);
        var actualPlaceholder = buyTourPage.getYearFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.yearPlaceholder));
    }

    @DisplayName("Проверка плейсхолдера поля «Владелец»")
    @Test
    public void shouldShowValidPlaceholderInHolderField() {
        buyTourPage.inputHolder(null);
        var actualPlaceholder = buyTourPage.getHolderFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.holderPlaceholder));
    }

    @DisplayName("Проверка плейсхолдера поля «CVC/CVV»")
    @Test
    public void shouldShowValidPlaceholderInCodeField() {
        buyTourPage.inputCode(null);
        var actualPlaceholder = buyTourPage.getCodeFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.codePlaceholder));
    }
}