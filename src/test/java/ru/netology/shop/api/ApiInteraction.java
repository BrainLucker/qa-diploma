package ru.netology.shop.api;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static ru.netology.shop.data.DataGenerator.CardInfo;

public class ApiInteraction {
    private final static String baseUrl = System.getProperty("api.url");
    private final static int port = Integer.parseInt(System.getProperty("api.port"));
    private final String paymentGatePath = "/pay";
    private final String creditGatePath = "/credit";

    private static final RequestSpecification requestSpec = new RequestSpecBuilder() // @formatter:off
        .setBaseUri(baseUrl)
        .setPort(port)
        .setAccept(ContentType.JSON)
        .setContentType("application/json")
        .log(LogDetail.ALL)
        .build()
        ; // @formatter:on

    @Step("Отправляем POST-запрос на PaymentGate")
    public String sendCardInfoToPaymentGate(CardInfo cardInfo, int statusCode) {
        return sendRequest(paymentGatePath, cardInfo, statusCode);
    }

    @Step("Отправляем POST-запрос на CreditGate")
    public String sendCardInfoToCreditGate(CardInfo cardInfo, int statusCode) {
        return sendRequest(creditGatePath, cardInfo, statusCode);
    }

    @Step("Проверяем, что код ответа {2} и получаем статус карты")
    public String sendRequest(String path, CardInfo body, int code) {
        String status = // @formatter:off
        given()
            .spec(requestSpec)
            .body(body)
        .when()
            .post(path)
        .then()
            .statusCode(code)
        .extract()
            .path("status").toString()
        ; // @formatter:on
        return status;
    }
}