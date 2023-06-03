package ru.netology.shop.test;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.shop.db.Order;

import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.netology.shop.page.ElementsTexts.Errors;
import static ru.netology.shop.data.DataGenerator.*;

@DisplayName("Покупка «Путешествия дня» c оплатой по карте")
public class BuyTourPaymentPageTest extends BasePageTest {
    private int price;

    @BeforeEach
    public void getPrice() {
        setup();
        price = tourOfTheDayPage.getTourPrice();
        buyTourPage = tourOfTheDayPage.clickBuyButton();
    }

    @ParameterizedTest(name = "Покупка тура по «APPROVED» карте с валидным реквизитами: {0}")
    @MethodSource("ru.netology.shop.data.TestData#validCardInfo")
    @Severity(value = SeverityLevel.BLOCKER)
    @TmsLinks({@TmsLink(value = "ui-01"), @TmsLink(value = "ui-03")})
    @Override
    public void shouldBuyTourIfValidCardInfo(String testName, CardInfo card) {
        Order expectedOrder = new Order("APPROVED", price);

        buyTourPage.inputCardInfo(card)
                .clickContinueButton()
                .checkButtonIsLoading()
                .checkSuccessNotification(15);
        Order actualOrder = db.getPaymentOrder();

        actualOrder.assertPaymentOrder(expectedOrder);
    }

    @DisplayName("Переход со страницы покупки тура на страницу покупки тура в кредит")
    @Test
    @Severity(value = SeverityLevel.MINOR)
    @TmsLink(value = "ui-05")
    @Override
    public void shouldChangePageIfClickButton() {
        tourOfTheDayPage.clickBuyButton();
        tourOfTheDayPage.clickCreditButton();
    }

    @DisplayName("Покупка тура по «DECLINED» карте с валидными реквизитами")
    @Test
    @Severity(value = SeverityLevel.CRITICAL)
    @TmsLink(value = "ui-07")
    @Issue(value = "2")
    @Override
    public void shouldShowErrorIfValidDeclinedCard() {
        CardInfo card = Cards.generateValidDeclinedCard();
        Order expectedOrder = new Order("DECLINED", price);

        buyTourPage.inputCardInfo(card)
                .clickContinueButton()
                .checkButtonIsLoading()
                .checkErrorNotification(15);
        Order actualOrder = db.getPaymentOrder();

        actualOrder.assertPaymentOrder(expectedOrder);
    }

    @ParameterizedTest(name = "Покупка тура по «APPROVED» карте со сроком действия {0}")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableMonth")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "ui-09")
    @Issue(value = "11")
    @Override
    public void shouldShowErrorIfInputtedMonthIsNotAllowable(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfo(card)
                .clickContinueButton()
                .checkButtonIsNormal()
                .checkMonthFieldError(errorText);

        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по «APPROVED» карте со сроком действия {0}")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableYear")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "ui-09")
    @Override
    public void shouldShowErrorIfInputtedYearIsNotAllowable(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfo(card)
                .clickContinueButton()
                .checkButtonIsNormal()
                .checkYearFieldError(errorText);

        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным номером: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidCardNumber")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-11"), @TmsLink(value = "ui-21")})
    @Issues({@Issue(value = "15"), @Issue(value = "10")})
    @Override
    public void shouldShowErrorIfInputtedInvalidNumber(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfo(card)
                .clickContinueButton()
                .checkButtonIsNormal()
                .checkNumberFieldError(errorText);

        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным месяцем: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidMonth")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-13"), @TmsLink(value = "ui-22")})
    @Issues({@Issue(value = "12"), @Issue(value = "10")})
    @Override
    public void shouldShowErrorIfInputtedInvalidMonth(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfo(card)
                .clickContinueButton()
                .checkButtonIsNormal()
                .checkMonthFieldError(errorText);

        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным годом: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidYear")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-15"), @TmsLink(value = "ui-23")})
    @Issue(value = "10")
    @Override
    public void shouldShowErrorIfInputtedInvalidYear(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfo(card)
                .clickContinueButton()
                .checkButtonIsNormal()
                .checkYearFieldError(errorText);

        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным именем владельца: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidHolderName")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-17"), @TmsLink(value = "ui-24")})
    @Issue(value = "5")
    @Override
    public void shouldShowErrorIfInputtedInvalidHolder(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfo(card)
                .clickContinueButton()
                .checkButtonIsNormal()
                .checkHolderFieldError(errorText);

        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным CVC-кодом: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidCode")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-19"), @TmsLink(value = "ui-25")})
    @Issues({@Issue(value = "4"), @Issue(value = "10")})
    @Override
    public void shouldShowErrorIfInputtedInvalidCode(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfo(card)
                .clickContinueButton()
                .checkButtonIsNormal()
                .checkCodeFieldError(errorText);

        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Отправка пустой формы покупки тура по карте")
    @Test
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "ui-26")
    @Issue(value = "10")
    @Override
    public void shouldShowErrorIfSendEmptyForm() {
        buyTourPage.clickContinueButton()
                .checkButtonIsNormal()
                .checkNumberFieldError(Errors.EMPTY_FIELD)
                .checkMonthFieldError(Errors.EMPTY_FIELD)
                .checkYearFieldError(Errors.EMPTY_FIELD)
                .checkHolderFieldError(Errors.EMPTY_FIELD)
                .checkCodeFieldError(Errors.EMPTY_FIELD);

        assertNull(db.getPaymentOrderId());
    }
}