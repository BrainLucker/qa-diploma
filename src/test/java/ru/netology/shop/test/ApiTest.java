package ru.netology.shop.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.netology.shop.api.ApiInteraction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.shop.data.DataGenerator.*;

@DisplayName("Отправка запросов с реквизитами карт на API")
public class ApiTest {
    private final static ApiInteraction api = new ApiInteraction();
    private String actualStatus;

    @DisplayName("Отправка POST-запроса на Payment Gate с реквизитами «APPROVED» карты")
    @Test
    public void shouldRespondWithCardStatusIfSendValidApprovedCardToPaymentGate() {
        var card = Cards.generateValidApprovedCard();
        actualStatus = api.sendCardToPaymentGate(card, 200);
        assertEquals("APPROVED", actualStatus);
    }

    @DisplayName("Отправка POST-запроса на Credit Gate с реквизитами «APPROVED» карты")
    @Test
    public void shouldRespondWithCardStatusIfSendValidApprovedCardToCreditGate() {
        var card = Cards.generateValidApprovedCard();
        actualStatus = api.sendCardToCreditGate(card, 200);
        assertEquals("APPROVED", actualStatus);
    }

    @DisplayName("Отправка POST-запроса на Payment Gate с реквизитами «DECLINED» карты")
    @Test
    public void shouldRespondWithCardStatusIfSendValidDeclinedCardToPaymentGate() {
        var card = Cards.generateValidDeclinedCard();
        actualStatus = api.sendCardToPaymentGate(card, 200);
        assertEquals("DECLINED", actualStatus);
    }

    @DisplayName("Отправка POST-запроса на Credit Gate с реквизитами «DECLINED» карты")
    @Test
    public void shouldRespondWithCardStatusIfSendValidDeclinedCardToCreditGate() {
        var card = Cards.generateValidDeclinedCard();
        actualStatus = api.sendCardToCreditGate(card, 200);
        assertEquals("DECLINED", actualStatus);
    }

    @MethodSource("ru.netology.shop.data.TestData#invalidApiCardInfo")
    @ParameterizedTest(name = "Отправка POST-запроса на Payment Gate с невалидными реквизитами: {1}")
    public void shouldRespondWithErrorIfSendInvalidCardToPaymentGate(CardInfo card, String testName) {
        actualStatus = api.sendCardToPaymentGate(card, 500);
        assertEquals("500", actualStatus);
    }

    @MethodSource("ru.netology.shop.data.TestData#invalidApiCardInfo")
    @ParameterizedTest(name = "Отправка POST-запроса на Credit Gate с невалидными реквизитами: {1}")
    public void shouldRespondWithErrorIfSendInvalidCardToCreditGate(CardInfo card, String testName) {
        actualStatus = api.sendCardToCreditGate(card, 500);
        assertEquals("500", actualStatus);
    }
}