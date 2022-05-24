package ru.netology.shop.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.shop.db.Order;

import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.netology.shop.page.ElementsTexts.Errors;
import static ru.netology.shop.data.DataGenerator.*;

@DisplayName("Покупка «Путешествия дня» в кредит по данным карты")
public class BuyTourCreditPageTest extends BasePageTest {

    @BeforeEach
    public void getPrice() {
        setup();
        buyTourPage = tourOfTheDayPage.clickCredit();
    }

    @Override
    @DisplayName("Покупка тура в кредит по «APPROVED» карте с валидными реквизитами")
    @Test
    public void shouldBuyTourIfValidApprovedCard() {
        card = Cards.generateValidApprovedCard();
        var expectedOrder = new Order("APPROVED");

        buyTourPage.inputCardInfoAndClickContinue(card);
        buyTourPage.checkButtonIsLoading();
        buyTourPage.checkSuccessNotification(12);
        var actualOrder = db.getCreditOrder();

        actualOrder.assertCreditOrder(expectedOrder);
    }

    @Override
    @DisplayName("Покупка тура в кредит по «APPROVED» карте с заканчивающимся сроком действия (текущий месяц)")
    @Test
    public void shouldBuyTourIfApprovedExpiringSoonCard() {
        card = Cards.generateApprovedCardExpiringIn(0, 0);
        var expectedOrder = new Order("APPROVED");

        buyTourPage.inputCardInfoAndClickContinue(card);
        buyTourPage.checkButtonIsLoading();
        buyTourPage.checkSuccessNotification(12);
        var actualOrder = db.getCreditOrder();

        actualOrder.assertCreditOrder(expectedOrder);
    }

    @Override
    @DisplayName("Покупка тура в кредит по «APPROVED» карте с валидным именем владельца")
    @MethodSource("ru.netology.shop.data.TestData#validHolderNames")
    @ParameterizedTest
    public void shouldBuyTourIfValidHolderName(String holderName) {
        card = Cards.generateCardAndSetHolder(holderName);
        var expectedOrder = new Order("APPROVED");

        buyTourPage.inputCardInfoAndClickContinue(card);
        buyTourPage.checkButtonIsLoading();
        buyTourPage.checkSuccessNotification(12);
        var actualOrder = db.getCreditOrder();

        actualOrder.assertCreditOrder(expectedOrder);
    }

    @Override
    @DisplayName("Переход со страницы покупки тура в кредит на страницу покупки тура")
    @Test
    public void shouldChangePage() {
        tourOfTheDayPage.clickCredit();
        tourOfTheDayPage.clickBuy();
    }

    @Override
    @DisplayName("Покупка тура в кредит по «DECLINED» карте с валидными реквизитами")
    @Test
    public void shouldShowErrorIfValidDeclinedCard() {
        card = Cards.generateValidDeclinedCard();
        var expectedOrder = new Order("DECLINED");

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsLoading();
        buyTourPage.checkErrorNotification(10);
        var actualOrder = db.getCreditOrder();
        actualOrder.assertCreditOrder(expectedOrder);
    }

    @Override
    @DisplayName("Покупка тура в кредит по «APPROVED» карте с месяцем меньше/больше допустимого")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableMonths")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedMonthIsNotAllowable(int shiftYears, int shiftMonths, String errorText) {
        card = Cards.generateApprovedCardExpiringIn(shiftYears, shiftMonths);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkMonthFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @Override
    @DisplayName("Покупка тура в кредит по «APPROVED» карте с годом меньше/больше допустимого")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableYears")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedYearIsNotAllowable(int shiftYears, int shiftMonths, String errorText) {
        card = Cards.generateApprovedCardExpiringIn(shiftYears, shiftMonths);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkYearFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @Override
    @DisplayName("Покупка тура в кредит по карте с невалидным номером")
    @MethodSource("ru.netology.shop.data.TestData#invalidCardNumbers")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedInvalidNumber(String cardNumber, String errorText) {
        card = Cards.generateCardAndSetNumber(cardNumber);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkNumberFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @Override
    @DisplayName("Покупка тура в кредит по карте с невалидным месяцем")
    @MethodSource("ru.netology.shop.data.TestData#invalidMonths")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedInvalidMonth(String cardMonth, String errorText) {
        card = Cards.generateCardAndSetMonth(cardMonth);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkMonthFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @Override
    @DisplayName("Покупка тура в кредит по карте с невалидным годом")
    @MethodSource("ru.netology.shop.data.TestData#invalidYears")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedInvalidYear(String cardYear, String errorText) {
        card = Cards.generateCardAndSetYear(cardYear);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkYearFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @Override
    @DisplayName("Покупка тура в кредит по карте с невалидным именем владельца")
    @MethodSource("ru.netology.shop.data.TestData#invalidHolderNames")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedInvalidHolder(String holderName, String errorText) {
        card = Cards.generateCardAndSetHolder(holderName);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkHolderFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @Override
    @DisplayName("Покупка тура в кредит по карте с невалидным CVC-кодом")
    @MethodSource("ru.netology.shop.data.TestData#invalidCodes")
    @ParameterizedTest
    public void shouldShowErrorIfInputtedInvalidCode(String code, String errorText) {
        card = Cards.generateCardAndSetCode(code);

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkCodeFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @Override
    @DisplayName("Отправка пустой формы покупки тура в кредит")
    @Test
    public void shouldShowErrorIfSendEmptyForm() {
        buyTourPage.clickContinueButton();

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkNumberFieldError(Errors.emptyField);
        buyTourPage.checkMonthFieldError(Errors.emptyField);
        buyTourPage.checkYearFieldError(Errors.emptyField);
        buyTourPage.checkHolderFieldError(Errors.emptyField);
        buyTourPage.checkCodeFieldError(Errors.emptyField);
        assertNull(db.getCreditOrderId());
    }
}