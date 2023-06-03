package ru.netology.shop.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Issue;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
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

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @AfterEach
    public void tearDown() {
        db.clearData();
    }

    public void setup() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "800x1000";
        tourOfTheDayPage = open(System.getProperty("sut.url"), TourOfTheDayPage.class);
    }

    public abstract void shouldBuyTourIfValidCardInfo(String testName, CardInfo card);

    public abstract void shouldChangePageIfClickButton();

    public abstract void shouldShowErrorIfValidDeclinedCard();

    public abstract void shouldShowErrorIfInputtedMonthIsNotAllowable(String testName, CardInfo card, String errorText);

    public abstract void shouldShowErrorIfInputtedYearIsNotAllowable(String testName, CardInfo card, String errorText);

    public abstract void shouldShowErrorIfInputtedInvalidNumber(String testName, CardInfo card, String errorText);

    public abstract void shouldShowErrorIfInputtedInvalidMonth(String testName, CardInfo card, String errorText);

    public abstract void shouldShowErrorIfInputtedInvalidYear(String testName, CardInfo card, String errorText);

    public abstract void shouldShowErrorIfInputtedInvalidHolder(String testName, CardInfo card, String errorText);

    public abstract void shouldShowErrorIfInputtedInvalidCode(String testName, CardInfo card, String errorText);

    public abstract void shouldShowErrorIfSendEmptyForm();

    @DisplayName("Проверка допустимой длины поля «Номер карты»")
    @Test
    @Severity(value = SeverityLevel.MINOR)
    @TmsLink(value = "ui-27")
    public void shouldLimitInputOfNumberField() {
        int actualFieldMaxLength = buyTourPage.getNumberFieldMaxLength();

        assertEquals(19, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «Месяц»")
    @Test
    @Severity(value = SeverityLevel.MINOR)
    @TmsLink(value = "ui-28")
    public void shouldLimitInputOfMonthField() {
        int actualFieldMaxLength = buyTourPage.getMonthFieldMaxLength();

        assertEquals(2, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «Год»")
    @Test
    @Severity(value = SeverityLevel.MINOR)
    @TmsLink(value = "ui-29")
    public void shouldLimitInputOfYearField() {
        int actualFieldMaxLength = buyTourPage.getYearFieldMaxLength();

        assertEquals(2, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «Владелец»")
    @Test
    @Severity(value = SeverityLevel.MINOR)
    @TmsLink(value = "ui-30")
    @Issue(value = "6")
    public void shouldLimitInputOfHolderField() {
        int actualFieldMaxLength = buyTourPage.getHolderFieldMaxLength();

        assertEquals(27, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «CVC/CVV»")
    @Test
    @Severity(value = SeverityLevel.MINOR)
    @TmsLink(value = "ui-31")
    public void shouldLimitInputOfCodeField() {
        int actualFieldMaxLength = buyTourPage.getCodeFieldMaxLength();

        assertEquals(3, actualFieldMaxLength);
    }

    @DisplayName("Проверка отображения и значения плейсхолдера поля «Номер карты»")
    @Test
    @Severity(value = SeverityLevel.TRIVIAL)
    @TmsLink(value = "ui-32")
    public void shouldShowValidPlaceholderInNumberField() {
        buyTourPage.inputNumber(null);
        String actualPlaceholder = buyTourPage.getNumberFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.NUMBER_PLACEHOLDER));
    }

    @DisplayName("Проверка отображения и значения плейсхолдера поля «Месяц»")
    @Test
    @Severity(value = SeverityLevel.TRIVIAL)
    @TmsLink(value = "ui-33")
    public void shouldShowValidPlaceholderInMonthField() {
        buyTourPage.inputMonth(null);
        String actualPlaceholder = buyTourPage.getMonthFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.MONTH_PLACEHOLDER));
    }

    @DisplayName("Проверка отображения и значения плейсхолдера поля «Год»")
    @Test
    @Severity(value = SeverityLevel.TRIVIAL)
    @TmsLink(value = "ui-34")
    public void shouldShowValidPlaceholderInYearField() {
        buyTourPage.inputYear(null);
        String actualPlaceholder = buyTourPage.getYearFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.YEAR_PLACEHOLDER));
    }

    @DisplayName("Проверка отображения и значения плейсхолдера поля «Владелец»")
    @Test
    @Severity(value = SeverityLevel.TRIVIAL)
    @TmsLink(value = "ui-35")
    @Issue(value = "9")
    public void shouldShowValidPlaceholderInHolderField() {
        buyTourPage.inputHolder(null);
        String actualPlaceholder = buyTourPage.getHolderFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.HOLDER_PLACEHOLDER));
    }

    @DisplayName("Проверка отображения и значения плейсхолдера поля «CVC/CVV»")
    @Test
    @Severity(value = SeverityLevel.TRIVIAL)
    @TmsLink(value = "ui-36")
    public void shouldShowValidPlaceholderInCodeField() {
        buyTourPage.inputCode(null);
        String actualPlaceholder = buyTourPage.getCodeFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.CODE_PLACEHOLDER));
    }
}