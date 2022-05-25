package ru.netology.shop.db;

import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbInteraction {
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String password = System.getProperty("db.password");
    private final String orderTable = "order_entity";
    private final String paymentTable = "payment_entity";
    private final String creditTable = "credit_request_entity";

    @SneakyThrows
    private Connection getConnection() {
        return DriverManager.getConnection(url, user, password);
    }

    @Step("Получаем статус покупки из таблицы «{0}»")
    @SneakyThrows
    private String getStatus(String table) {
        var statusSQL = "SELECT status FROM " + table;

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
        var amountSQL = "SELECT amount FROM " + paymentTable;

        try (var conn = getConnection()) {
            int result = new QueryRunner().query(conn, amountSQL, new ScalarHandler<>());
            return result / 100;
        }
    }

    @Step("Получаем id заказа из таблицы «" + orderTable + "», связанного с оплатой из таблицы «{0}»")
    @SneakyThrows
    private String getOrderId(String table, String joinOnColumns) {
        var orderIdSQL = "SELECT o.id FROM " + table +
                " INNER JOIN " + orderTable + " o ON " + joinOnColumns;

        try (var conn = getConnection()) {
            return new QueryRunner().query(conn, orderIdSQL, new ScalarHandler<>());
        }
    }

    @Step("Очищаем таблицы БД после выполнения теста")
    @SneakyThrows
    public void clearData() {
        var runner = new QueryRunner();

        try (var conn = getConnection()) {
            runner.update(conn, "DELETE from " + paymentTable);
            runner.update(conn, "DELETE from " + creditTable);
            runner.update(conn, "DELETE from " + orderTable);
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