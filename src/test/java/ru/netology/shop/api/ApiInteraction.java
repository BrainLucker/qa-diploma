package ru.netology.shop.api;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static ru.netology.shop.data.DataGenerator.CardInfo;

public class ApiInteraction {
    private final static String BASE_URL = System.getProperty("api.url");
    private final static int PORT = Integer.parseInt(System.getProperty("api.port"));
    private final static String PAYMENT_GATE_PATH = System.getProperty("payment_gate_path");
    private final static String CREDIT_GATE_PATH = System.getProperty("credit_gate_path");

    private static final RequestSpecification REQUEST_SPEC = new RequestSpecBuilder() // @formatter:off
        .setBaseUri(BASE_URL)
        .setPort(PORT)
        .setAccept(ContentType.JSON)
        .setContentType("application/json")
        .log(LogDetail.ALL)
        .build()
        ; // @formatter:on

    @Step("Отправляем POST-запрос на PaymentGate")
    public String sendCardInfoToPaymentGate(CardInfo cardInfo, int statusCode) {
        return sendRequest(PAYMENT_GATE_PATH, cardInfo, statusCode);
    }

    @Step("Отправляем POST-запрос на CreditGate")
    public String sendCardInfoToCreditGate(CardInfo cardInfo, int statusCode) {
        return sendRequest(CREDIT_GATE_PATH, cardInfo, statusCode);
    }

    @Step("Проверяем, что код ответа {2} и получаем статус карты")
    public String sendRequest(String path, CardInfo body, int code) {
        String status = // @formatter:off
        given()
            .spec(REQUEST_SPEC)
            .body(body)
        .when()
            .post(path)
        .then()
            .statusCode(code)
            .extract().path("status")
            .toString()
        ; // @formatter:on

        return status;
    }
}