package ru.netology.shop.test;

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
        buyTourPage = tourOfTheDayPage.clickBuy();
    }

    @ParameterizedTest(name = "Покупка тура по «APPROVED» карте с валидным реквизитами: {0}")
    @MethodSource("ru.netology.shop.data.TestData#validCardInfo")
    @Override
    public void shouldBuyTourIfValidCardInfo(String testName, CardInfo card) {
        var expectedOrder = new Order("APPROVED", price);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsLoading();
        buyTourPage.checkSuccessNotification(15);
        var actualOrder = db.getPaymentOrder();
        actualOrder.assertPaymentOrder(expectedOrder);
    }

    @DisplayName("Переход со страницы покупки тура на страницу покупки тура в кредит")
    @Test
    @Override
    public void shouldChangePageIfClickButton() {
        tourOfTheDayPage.clickBuy();
        tourOfTheDayPage.clickCredit();
    }

    @DisplayName("Покупка тура по «DECLINED» карте с валидными реквизитами")
    @Test
    @Override
    public void shouldShowErrorIfValidDeclinedCard() {
        var card = Cards.generateValidDeclinedCard();
        var expectedOrder = new Order("DECLINED", price);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsLoading();
        buyTourPage.checkErrorNotification(15);
        var actualOrder = db.getPaymentOrder();
        actualOrder.assertPaymentOrder(expectedOrder);
    }

    @ParameterizedTest(name = "Покупка тура по «APPROVED» карте со сроком действия {0}")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableMonth")
    @Override
    public void shouldShowErrorIfInputtedMonthIsNotAllowable(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkMonthFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по «APPROVED» карте со сроком действия {0}")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableYear")
    @Override
    public void shouldShowErrorIfInputtedYearIsNotAllowable(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkYearFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным номером: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidCardNumber")
    @Override
    public void shouldShowErrorIfInputtedInvalidNumber(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkNumberFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным месяцем: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidMonth")
    @Override
    public void shouldShowErrorIfInputtedInvalidMonth(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkMonthFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным годом: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidYear")
    @Override
    public void shouldShowErrorIfInputtedInvalidYear(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkYearFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным именем владельца: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidHolderName")
    @Override
    public void shouldShowErrorIfInputtedInvalidHolder(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkHolderFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @ParameterizedTest(name = "Покупка тура по карте с невалидным CVC-кодом: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidCode")
    @Override
    public void shouldShowErrorIfInputtedInvalidCode(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkCodeFieldError(errorText);
        assertNull(db.getPaymentOrderId());
    }

    @DisplayName("Отправка пустой формы покупки тура по карте")
    @Test
    @Override
    public void shouldShowErrorIfSendEmptyForm() {
        buyTourPage.clickContinueButton();

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkNumberFieldError(Errors.emptyField);
        buyTourPage.checkMonthFieldError(Errors.emptyField);
        buyTourPage.checkYearFieldError(Errors.emptyField);
        buyTourPage.checkHolderFieldError(Errors.emptyField);
        buyTourPage.checkCodeFieldError(Errors.emptyField);
        assertNull(db.getPaymentOrderId());
    }
}