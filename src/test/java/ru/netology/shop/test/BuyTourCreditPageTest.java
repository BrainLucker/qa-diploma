package ru.netology.shop.test;

import io.qameta.allure.*;
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

    @ParameterizedTest(name = "Покупка тура в кредит по «APPROVED» карте с валидным реквизитами: {0}")
    @MethodSource("ru.netology.shop.data.TestData#validCardInfo")
    @Severity(value = SeverityLevel.BLOCKER)
    @TmsLinks({@TmsLink(value = "ui-02"), @TmsLink(value = "ui-04")})
    @Issue(value = "1")
    @Override
    public void shouldBuyTourIfValidCardInfo(String testName, CardInfo card) {
        var expectedOrder = new Order("APPROVED");

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsLoading();
        buyTourPage.checkSuccessNotification(15);
        var actualOrder = db.getCreditOrder();
        actualOrder.assertCreditOrder(expectedOrder);
    }

    @DisplayName("Переход со страницы покупки тура в кредит на страницу покупки тура")
    @Test
    @Severity(value = SeverityLevel.MINOR)
    @TmsLink(value = "ui-06")
    @Override
    public void shouldChangePageIfClickButton() {
        tourOfTheDayPage.clickCredit();
        tourOfTheDayPage.clickBuy();
    }

    @DisplayName("Покупка тура в кредит по «DECLINED» карте с валидными реквизитами")
    @Test
    @Severity(value = SeverityLevel.CRITICAL)
    @TmsLink(value = "ui-08")
    @Issue(value = "2")
    @Override
    public void shouldShowErrorIfValidDeclinedCard() {
        var card = Cards.generateValidDeclinedCard();
        var expectedOrder = new Order("DECLINED");

        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsLoading();
        buyTourPage.checkErrorNotification(15);
        var actualOrder = db.getCreditOrder();
        actualOrder.assertCreditOrder(expectedOrder);
    }

    @ParameterizedTest(name = "Покупка тура в кредит по «APPROVED» карте со сроком действия {0}")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableMonth")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "ui-10")
    @Issue(value = "11")
    @Override
    public void shouldShowErrorIfInputtedMonthIsNotAllowable(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkMonthFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @ParameterizedTest(name = "Покупка тура в кредит по «APPROVED» карте со сроком действия {0}")
    @MethodSource("ru.netology.shop.data.TestData#notAllowableYear")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "ui-10")
    @Override
    public void shouldShowErrorIfInputtedYearIsNotAllowable(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkYearFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @ParameterizedTest(name = "Покупка тура в кредит по карте с невалидным номером: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidCardNumber")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-12"), @TmsLink(value = "ui-21")})
    @Issues({@Issue(value = "15"), @Issue(value = "10")})
    @Override
    public void shouldShowErrorIfInputtedInvalidNumber(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkNumberFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @ParameterizedTest(name = "Покупка тура в кредит по карте с невалидным месяцем: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidMonth")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-14"), @TmsLink(value = "ui-22")})
    @Issues({@Issue(value = "12"), @Issue(value = "10")})
    @Override
    public void shouldShowErrorIfInputtedInvalidMonth(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkMonthFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @ParameterizedTest(name = "Покупка тура в кредит по карте с невалидным годом: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidYear")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-16"), @TmsLink(value = "ui-23")})
    @Issue(value = "10")
    @Override
    public void shouldShowErrorIfInputtedInvalidYear(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkYearFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @ParameterizedTest(name = "Покупка тура в кредит по карте с невалидным именем владельца: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidHolderName")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-18"), @TmsLink(value = "ui-24")})
    @Issue(value = "5")
    @Override
    public void shouldShowErrorIfInputtedInvalidHolder(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkHolderFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @ParameterizedTest(name = "Покупка тура в кредит по карте с невалидным CVC-кодом: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidCode")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink(value = "ui-20"), @TmsLink(value = "ui-25")})
    @Issues({@Issue(value = "4"), @Issue(value = "10")})
    @Override
    public void shouldShowErrorIfInputtedInvalidCode(String testName, CardInfo card, String errorText) {
        buyTourPage.inputCardInfoAndClickContinue(card);

        buyTourPage.checkButtonIsNormal();
        buyTourPage.checkCodeFieldError(errorText);
        assertNull(db.getCreditOrderId());
    }

    @DisplayName("Отправка пустой формы покупки тура в кредит")
    @Test
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "ui-26")
    @Issue(value = "10")
    @Override
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