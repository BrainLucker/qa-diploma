package ru.netology.shop.db;

import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbInteraction {
    private static final String URL = System.getProperty("db.url");
    private static final String USER = System.getProperty("db.user");
    private static final String PASSWORD = System.getProperty("db.password");
    private static final String ORDER_TABLE = "order_entity";
    private static final String PAYMENT_TABLE = "payment_entity";
    private static final String CREDIT_TABLE = "credit_request_entity";

    @SneakyThrows
    private Connection getConnection() {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    @Step("Получаем статус покупки из таблицы «{0}»")
    @SneakyThrows
    private String getStatus(String table) {
        String statusSQL = String.format("SELECT status FROM %s", table);

        try (var conn = getConnection()) {
            return new QueryRunner().query(conn, statusSQL, new ScalarHandler<>());
        }
    }

    public String getPaymentStatus() {
        return getStatus(PAYMENT_TABLE);
    }

    public String getCreditStatus() {
        return getStatus(CREDIT_TABLE);
    }

    @Step("Получаем сумму оплаты из таблицы «" + PAYMENT_TABLE + "»")
    @SneakyThrows
    public int getPaymentAmount() {
        String amountSQL = String.format("SELECT amount FROM %s", PAYMENT_TABLE);

        try (var conn = getConnection()) {
            int result = new QueryRunner().query(conn, amountSQL, new ScalarHandler<>());
            return result / 100;
        }
    }

    @Step("Получаем id заказа из таблицы «" + ORDER_TABLE + "», связанного с оплатой из таблицы «{0}»")
    @SneakyThrows
    private String getOrderId(String table, String column_1, String column_2) {
        String orderIdSQL = String.format("SELECT o.id FROM %s INNER JOIN %s o ON %s = %s",
                table, ORDER_TABLE, column_1, column_2);

        try (var conn = getConnection()) {
            return new QueryRunner().query(conn, orderIdSQL, new ScalarHandler<>());
        }
    }

    @Step("Очищаем таблицы БД после выполнения теста")
    @SneakyThrows
    public void clearData() {
        QueryRunner runner = new QueryRunner();

        try (var conn = getConnection()) {
            runner.update(conn, "DELETE from " + PAYMENT_TABLE);
            runner.update(conn, "DELETE from " + CREDIT_TABLE);
            runner.update(conn, "DELETE from " + ORDER_TABLE);
        }
    }

    public String getPaymentOrderId() {
        return getOrderId(PAYMENT_TABLE, "transaction_id", "payment_id");
    }

    public String getCreditOrderId() {
        return getOrderId(CREDIT_TABLE, "bank_id", "credit_id");
    }

    public Order getPaymentOrder() {
        return new Order(getPaymentOrderId(), getPaymentStatus(), getPaymentAmount());
    }

    public Order getCreditOrder() {
        return new Order(getCreditOrderId(), getCreditStatus(), 0);
    }

}