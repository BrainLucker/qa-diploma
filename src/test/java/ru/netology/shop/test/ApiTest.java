package ru.netology.shop.test;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.shop.api.ApiInteraction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.shop.data.DataGenerator.CardInfo;

@DisplayName("Отправка запросов с реквизитами карт на API")
public class ApiTest {
    private final ApiInteraction api = new ApiInteraction();

    @ParameterizedTest(name = "Отправка POST-запроса на Payment Gate с валидными реквизитами {0}")
    @MethodSource("ru.netology.shop.data.TestData#validApiCardInfo")
    @Severity(value = SeverityLevel.BLOCKER)
    @TmsLinks({@TmsLink(value = "api-01"), @TmsLink(value = "api-02")})
    public void shouldRespondWithCardStatusIfSendValidCardToPaymentGate(String testName, CardInfo card, String expectedStatus) {
        String actualStatus = api.sendCardInfoToPaymentGate(card, 200);

        assertEquals(expectedStatus, actualStatus);
    }

    @ParameterizedTest(name = "Отправка POST-запроса на Credit Gate с валидными реквизитами {0}")
    @MethodSource("ru.netology.shop.data.TestData#validApiCardInfo")
    @Severity(value = SeverityLevel.BLOCKER)
    @TmsLinks({@TmsLink(value = "api-03"), @TmsLink(value = "api-04")})
    public void shouldRespondWithCardStatusIfSendValidCardToCreditGate(String testName, CardInfo card, String expectedStatus) {
        String actualStatus = api.sendCardInfoToCreditGate(card, 200);

        assertEquals(expectedStatus, actualStatus);
    }

    @ParameterizedTest(name = "Отправка POST-запроса на Payment Gate с невалидными реквизитами: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidApiCardInfo")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "api-05")
    @Issue(value = "14")
    public void shouldRespondWithErrorIfSendInvalidCardToPaymentGate(String testName, CardInfo card) {
        String actualStatus = api.sendCardInfoToPaymentGate(card, 500);

        assertEquals("500", actualStatus);
    }

    @ParameterizedTest(name = "Отправка POST-запроса на Credit Gate с невалидными реквизитами: {0}")
    @MethodSource("ru.netology.shop.data.TestData#invalidApiCardInfo")
    @Severity(value = SeverityLevel.NORMAL)
    @TmsLink(value = "api-06")
    @Issue(value = "14")
    public void shouldRespondWithErrorIfSendInvalidCardToCreditGate(String testName, CardInfo card) {
        String actualStatus = api.sendCardInfoToCreditGate(card, 500);

        assertEquals("500", actualStatus);
    }

}