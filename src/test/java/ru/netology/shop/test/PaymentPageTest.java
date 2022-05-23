package ru.netology.shop.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.*;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.shop.db.DbInteraction;
import ru.netology.shop.mode.Order;
import ru.netology.shop.page.TourOfTheDayPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.shop.data.DataGenerator.*;
import static ru.netology.shop.data.TestData.Regex;
import static ru.netology.shop.page.ElementsTexts.Errors;

public class PaymentPageTest {
    private static final DbInteraction db = new DbInteraction();
    private TourOfTheDayPage tourPage;
    private CardInfo card;
    private int price;

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setup() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "800x950";
        tourPage = open("http://localhost:8080", TourOfTheDayPage.class);
        price = tourPage.getTourPrice();
    }

    @DisplayName("Покупка тура по «APPROVED» карте с валидными реквизитами")
    @Severity(value = SeverityLevel.BLOCKER)
    @Test
    public void shouldMakePaymentIfValidApprovedCard() {
        card = Cards.generateValidApprovedCard();
        var expectedOrder = new Order("APPROVED", price);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);
        paymentPage.checkButtonIsLoading();
        paymentPage.checkOkNotification(10);
        var actualOrder = db.getPaymentOrder();

        actualOrder.assertPaymentOrder(expectedOrder);
    }

    @DisplayName("Покупка тура по «APPROVED» карте с заканчивающимся сроком действия (текущий месяц)")
    @Test
    public void shouldMakePaymentIfApprovedExpiringSoonCard() {
        card = Cards.generateApprovedCardExpiringIn(0, 0);
        var expectedOrder = new Order("APPROVED", price);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);
        paymentPage.checkButtonIsLoading();
        paymentPage.checkOkNotification(10);
        var actualOrder = db.getPaymentOrder();

        actualOrder.assertPaymentOrder(expectedOrder);
    }

    @DisplayName("Покупка тура по «APPROVED» карте с валидным именем владельца")
    @MethodSource("ru.netology.shop.data.TestData#validHolderNames")
    @ParameterizedTest
    public void shouldMakePaymentIfValidHolderName(String holderName) {
        card = Cards.generateCardAndSetHolder(holderName);
        var expectedOrder = new Order("APPROVED", price);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);
        paymentPage.checkButtonIsLoading();
        paymentPage.checkOkNotification(10);
        var actualOrder = db.getCreditOrder();

        actualOrder.assertCreditOrder(expectedOrder);
    }

    @DisplayName("Переход со страницы покупки тура на страницу покупки тура в кредит")
    @Test
    public void shouldChangePage() {
        tourPage.clickBuy();
        tourPage.clickCredit();
    }

    @DisplayName("Покупка тура по «DECLINED» карте с валидными реквизитами")
    @Test
    public void shouldShowErrorIfValidDeclinedCard() {
        card = Cards.generateValidDeclinedCard();
        var expectedOrder = new Order("DECLINED", price);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);

        paymentPage.checkButtonIsLoading();
        paymentPage.checkErrorNotification(10);
        var actualOrder = db.getPaymentOrder();
        actualOrder.assertPaymentOrder(expectedOrder);
    }

    @DisplayName("Покупка тура по «APPROVED» карте с месяцем меньше/больше допустимого")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableMonths")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedMonthIsNotAllowable(int shiftYears, int shiftMonths, String errorText) {
        card = Cards.generateApprovedCardExpiringIn(shiftYears, shiftMonths);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);

        paymentPage.checkButtonIsNormal();
        paymentPage.checkMonthFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Покупка тура по «APPROVED» карте с годом меньше/больше допустимого")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableYears")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedYearIsNotAllowable(int shiftYears, int shiftMonths, String errorText) {
        card = Cards.generateApprovedCardExpiringIn(shiftYears, shiftMonths);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);

        paymentPage.checkButtonIsNormal();
        paymentPage.checkYearFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Покупка тура по карте с невалидным номером")
    @MethodSource("ru.netology.shop.data.TestData#invalidCardNumbers")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedInvalidNumber(String cardNumber, String errorText) {
        card = Cards.generateCardAndSetNumber(cardNumber);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);

        paymentPage.checkButtonIsNormal();
        paymentPage.checkNumberFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Покупка тура по карте с невалидным месяцем")
    @MethodSource("ru.netology.shop.data.TestData#invalidMonths")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedInvalidMonth(String cardMonth, String errorText) {
        card = Cards.generateCardAndSetMonth(cardMonth);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);

        paymentPage.checkButtonIsNormal();
        paymentPage.checkMonthFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Покупка тура по карте с невалидным годом")
    @MethodSource("ru.netology.shop.data.TestData#invalidYears")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedInvalidYear(String cardYear, String errorText) {
        card = Cards.generateCardAndSetYear(cardYear);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);

        paymentPage.checkButtonIsNormal();
        paymentPage.checkYearFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Покупка тура по карте с невалидным именем владельца")
    @MethodSource("ru.netology.shop.data.TestData#invalidHolderNames")
    @ParameterizedTest
    public void shouldShowInputIfInputtedInvalidHolder(String holderName, String errorText) {
        card = Cards.generateCardAndSetHolder(holderName);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);

        paymentPage.checkButtonIsNormal();
        paymentPage.checkHolderFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Покупка тура по карте с невалидным CVC-кодом")
    @MethodSource("ru.netology.shop.data.TestData#invalidCodes")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedInvalidCode(String code, String errorText) {
        card = Cards.generateCardAndSetCode(code);

        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCardInfoAndSubmit(card);

        paymentPage.checkButtonIsNormal();
        paymentPage.checkCodeFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Отправка пустой формы покупки тура")
    @Test
    public void shouldShowErrorIfSendEmptyForm() {
        var paymentPage = tourPage.clickBuy();

        paymentPage.clickButton();

        paymentPage.checkButtonIsNormal();
        paymentPage.checkNumberFieldError(Errors.emptyField);
        paymentPage.checkMonthFieldError(Errors.emptyField);
        paymentPage.checkYearFieldError(Errors.emptyField);
        paymentPage.checkHolderFieldError(Errors.emptyField);
        paymentPage.checkCodeFieldError(Errors.emptyField);
        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Проверка допустимой длины поля «Номер карты»")
    @Test
    public void shouldLimitInputOfNumberField() {
        var paymentPage = tourPage.clickBuy();
        var actualFieldMaxLength = paymentPage.getNumberFieldMaxLength();

        assertEquals(19, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «Месяц»")
    @Test
    public void shouldLimitInputOfMonthField() {
        var paymentPage = tourPage.clickBuy();
        var actualFieldMaxLength = paymentPage.getMonthFieldMaxLength();

        assertEquals(2, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «Год»")
    @Test
    public void shouldLimitInputOfYearField() {
        var paymentPage = tourPage.clickBuy();
        var actualFieldMaxLength = paymentPage.getYearFieldMaxLength();

        assertEquals(2, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «Владелец»")
    @Test
    public void shouldLimitInputOfHolderField() {
        var paymentPage = tourPage.clickBuy();
        var actualFieldMaxLength = paymentPage.getHolderFieldMaxLength();

        assertEquals(27, actualFieldMaxLength);
    }

    @DisplayName("Проверка допустимой длины поля «CVC/CVV»")
    @Test
    public void shouldLimitInputOfCodeField() {
        var paymentPage = tourPage.clickBuy();
        var actualFieldMaxLength = paymentPage.getCodeFieldMaxLength();

        assertEquals(3, actualFieldMaxLength);
    }

    @DisplayName("Проверка плейсхолдера поля «Номер карты»")
    @Test
    public void shouldShowValidPlaceholderInNumberField() {
        var paymentPage = tourPage.clickBuy();
        paymentPage.inputNumber(null);
        var actualPlaceholder = paymentPage.getNumberFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.numberPlaceholder));
    }

    @DisplayName("Проверка плейсхолдера поля «Месяц»")
    @Test
    public void shouldShowValidPlaceholderInMonthField() {
        var paymentPage = tourPage.clickBuy();
        paymentPage.inputMonth(null);
        var actualPlaceholder = paymentPage.getMonthFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.monthPlaceholder));
    }

    @DisplayName("Проверка плейсхолдера поля «Год»")
    @Test
    public void shouldShowValidPlaceholderInYearField() {
        var paymentPage = tourPage.clickBuy();
        paymentPage.inputYear(null);
        var actualPlaceholder = paymentPage.getYearFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.yearPlaceholder));
    }

    @DisplayName("Проверка плейсхолдера поля «Владелец»")
    @Test
    public void shouldShowValidPlaceholderInHolderField() {
        var paymentPage = tourPage.clickBuy();
        paymentPage.inputHolder(null);
        var actualPlaceholder = paymentPage.getHolderFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.holderPlaceholder));
    }

    @DisplayName("Проверка плейсхолдера поля «CVC/CVV»")
    @Test
    public void shouldShowValidPlaceholderInCodeField() {
        var paymentPage = tourPage.clickBuy();
        paymentPage.inputCode(null);
        var actualPlaceholder = paymentPage.getCodeFieldPlaceholder();

        assertTrue(actualPlaceholder.matches(Regex.codePlaceholder));
    }
}