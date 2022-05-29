package ru.netology.shop.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.shop.api.ApiInteraction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.shop.data.DataGenerator.CardInfo;

@DisplayName("Отправка запросов с реквизитами карт на API")
public class ApiTest {
    private final static ApiInteraction api = new ApiInteraction();
    private String actualStatus;

    @ParameterizedTest(name = "Отправка POST-запроса на Payment Gate с валидными реквизитами {0}")
    @MethodSource("ru.netology.shop.data.TestData#validApiCardInfo")
    public void shouldRespondWithCardStatusIfSendValidCardToPaymentGate(String testName, CardInfo card, String expectedStatus) {
        actualStatus = api.sendCardInfoToPaymentGate(card, 200);
        assertEquals(expectedStatus, actualStatus);
    }

    @ParameterizedTest(name = "Отправка POST-запроса на Credit Gate с валидными реквизитами {0}")
    @MethodSource("ru.netology.shop.data.TestData#validApiCardInfo")
    public void shouldRespondWithCardStatusIfSendValidCardToCreditGate(String testName, CardInfo card, String expectedStatus) {
        actualStatus = api.sendCardInfoToCreditGate(card, 200);
        assertEquals(expectedStatus, actualStatus);
    }

    @MethodSource("ru.netology.shop.data.TestData#invalidApiCardInfo")
    @ParameterizedTest(name = "Отправка POST-запроса на Payment Gate с невалидными реквизитами: {0}")
    public void shouldRespondWithErrorIfSendInvalidCardToPaymentGate(String testName, CardInfo card) {
        actualStatus = api.sendCardInfoToPaymentGate(card, 500);
        assertEquals("500", actualStatus);
    }

    @MethodSource("ru.netology.shop.data.TestData#invalidApiCardInfo")
    @ParameterizedTest(name = "Отправка POST-запроса на Credit Gate с невалидными реквизитами: {0}")
    public void shouldRespondWithErrorIfSendInvalidCardToCreditGate(String testName, CardInfo card) {
        actualStatus = api.sendCardInfoToCreditGate(card, 500);
        assertEquals("500", actualStatus);
    }
}