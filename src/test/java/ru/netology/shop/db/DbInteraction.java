package ru.netology.shop.db;

import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbInteraction {
    private final String url = "jdbc:postgresql://localhost:5432/app";
    private final String user = "admin";
    private final String password = "pass";
    private final String paymentTable = "payment_entity";
    private final String creditTable = "credit_request_entity";
    private final String interval = "now() - interval '5 seconds'";

    @SneakyThrows
    private Connection getConnection() {
        return DriverManager.getConnection(url, user, password);
    }

    @Step("Получаем статус покупки из «{0}»")
    @SneakyThrows
    private String getStatus(String table) {
        var statusSQL = "SELECT status FROM " + table +
                " WHERE created > " + interval +
                " ORDER BY created DESC";

        try (var conn = getConnection()) {
            return new QueryRunner().query(conn, statusSQL, new ScalarHandler<>());
        }
    }

    public String getPaymentStatus() {
        return getStatus(paymentTable);
    }

    public String getCreditStatus() {
        return getStatus(creditTable);
    }

    @Step("Получаем сумму оплаты из таблицы «" + paymentTable + "»")
    @SneakyThrows
    public int getPaymentAmount() {
        var amountSQL = "SELECT amount/100 FROM " + paymentTable +
                " WHERE created > " + interval +
                " ORDER BY created DESC";

        try (var conn = getConnection()) {
            return new QueryRunner().query(conn, amountSQL, new ScalarHandler<>());
        }
    }

    @Step("Получаем id заказа из таблицы «order_entity», связанного с оплатой из таблицы «{0}»")
    @SneakyThrows
    private String getOrderId(String table, String joinOnColumns) {
        var orderIdSQL = "SELECT oe.id FROM " + table +
                " INNER JOIN order_entity oe ON " + joinOnColumns +
                " WHERE oe.created > " + interval +
                " ORDER BY oe.created DESC";

        try (var conn = getConnection()) {
            return new QueryRunner().query(conn, orderIdSQL, new ScalarHandler<>());
        }
    }

    public String getPaymentOrderId() {
        return getOrderId(paymentTable, "transaction_id = payment_id");
    }

    public String getCreditOrderId() {
        return getOrderId(creditTable, "bank_id = credit_id");
    }

    public Order getPaymentOrder() {
        return new Order(getPaymentOrderId(), getPaymentStatus(), getPaymentAmount());
    }

    public Order getCreditOrder() {
        return new Order(getCreditOrderId(), getCreditStatus(), 0);
    }
}