package ru.netology.shop.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.netology.shop.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.shop.page.ElementsTexts.Notifications;
import static ru.netology.shop.page.ElementsTexts.Buttons;

public class BuyTourPage {
    final SelenideElement heading = $("div[id=root] > div > h3.heading");
    private final SelenideElement form = $("form fieldset");
    // Селекторы элементов формы
    private final SelenideElement numberField = findFieldByText("Номер карты");
    private final SelenideElement monthField = findFieldByText("Месяц");
    private final SelenideElement yearField = findFieldByText("Год");
    private final SelenideElement holderField = findFieldByText("Владелец");
    private final SelenideElement codeField = findFieldByText("CVC/CVV");
    private final SelenideElement continueButton = form.$("button.button");
    // Селекторы всплывающих сообщений
    private final SelenideElement notificationSuccess = $(".notification_status_ok");
    private final SelenideElement notificationError = $(".notification_status_error");

    private SelenideElement findFieldByText(String fieldName) {
        return form.$x(".//span[@class='input__top'][contains(text(),'" + fieldName + "')]//..");
    }

    @Step("Вводим номер карты: {0}")
    public BuyTourPage inputNumber(String cardNumber) {
        numberField.$("input").val(cardNumber);
        return this;
    }

    @Step("Вводим месяц: {0}")
    public BuyTourPage inputMonth(String month) {
        monthField.$("input").val(month);
        return this;
    }

    @Step("Вводим год: {0}")
    public BuyTourPage inputYear(String year) {
        yearField.$("input").val(year);
        return this;
    }

    @Step("Вводим имя и фамилию владельца: {0}")
    public BuyTourPage inputHolder(String holder) {
        holderField.$("input").val(holder);
        return this;
    }

    @Step("Вводим CVV-код: {0}")
    public BuyTourPage inputCode(String code) {
        codeField.$("input").setValue(code);
        return this;
    }

    @Step("Нажимаем на кнопку «Продолжить»")
    public BuyTourPage clickContinueButton() {
        continueButton.shouldHave(text(Buttons.SUBMIT), Duration.ofMillis(30)).click();
        return this;
    }

    public BuyTourPage inputCardInfo(DataGenerator.CardInfo cardInfo) {
        this.inputNumber(cardInfo.getNumber())
                .inputMonth(cardInfo.getMonth())
                .inputYear(cardInfo.getYear())
                .inputHolder(cardInfo.getHolder())
                .inputCode(cardInfo.getCvc());
        return this;
    }

    /**
     * Проверка появления иконки загрузки
     */
    @Step("Проверяем появление иконки загрузки и изменение текста кнопки")
    public BuyTourPage checkButtonIsLoading() {
        continueButton.shouldHave(cssClass("button_disabled"), Duration.ofMillis(30));
        continueButton.$(".button__text").shouldHave(text(Buttons.LOADING), Duration.ofMillis(30));
        continueButton.$(".button__text .spin").shouldBe(visible, Duration.ofMillis(30));
        return this;
    }

    /**
     * Проверка отсутствие иконки загрузки
     *
     */
    @Step("Проверяем отсутствие иконки загрузки и неизменность текста кнопки")
    public BuyTourPage checkButtonIsNormal() {
        continueButton.shouldNotHave(cssClass("button_disabled"), Duration.ofMillis(30));
        continueButton.$(".button__text").shouldHave(text(Buttons.SUBMIT), Duration.ofMillis(30));
        continueButton.$(".button__text .spin").shouldNotBe(visible, Duration.ofMillis(30));
        return this;
    }

    /**
     * Проверка сообщений о результате
     */
    @Step("Проверяем появилось ли сообщение с результатом «{title}»")
    public void checkNotification(SelenideElement notification, int waitingTimeInSeconds, String title, String content) {
        notification.shouldBe(visible, Duration.ofSeconds(waitingTimeInSeconds));
        notification.$(".notification__title").shouldHave(text(title));
        notification.$(".notification__content").shouldHave(text(content));
    }

    public void checkSuccessNotification(int waitingTimeInSeconds) {
        checkNotification(notificationSuccess, waitingTimeInSeconds, Notifications.SUCCESS_TITLE, Notifications.SUCCESS_TEXT);
    }

    public void checkErrorNotification(int waitingTimeInSeconds) {
        checkNotification(notificationError, waitingTimeInSeconds, Notifications.ERROR_TITLE, Notifications.ERROR_TEXT);
    }

    /**
     * Проверка текста ошибки под полем ввода
     */
    @Step("Проверяем, что под полем появляется ошибка «{1}»")
    private void checkFieldError(SelenideElement field, String errorText) {
        field.parent().shouldHave(cssClass("input_invalid"), Duration.ofMillis(10));
        field.$(".input__sub")
                .shouldBe(visible, Duration.ofMillis(10))
                .shouldHave(text(errorText), Duration.ofMillis(10));
    }

    public BuyTourPage checkNumberFieldError(String errorText) {
        checkFieldError(numberField, errorText);
        return this;
    }

    public BuyTourPage checkMonthFieldError(String errorText) {
        checkFieldError(monthField, errorText);
        return this;
    }

    public BuyTourPage checkYearFieldError(String errorText) {
        checkFieldError(yearField, errorText);
        return this;
    }

    public BuyTourPage checkHolderFieldError(String errorText) {
        checkFieldError(holderField, errorText);
        return this;
    }

    public void checkCodeFieldError(String errorText) {
        checkFieldError(codeField, errorText);
    }

    /**
     * Получение допустимой длины поля
     */
    @Step("Получаем допустимую длину поля")
    private int getFieldMaxLength(SelenideElement field) {
        String maxLength = field.$("input.input__control")
                .shouldHave(attribute("maxlength"), Duration.ofMillis(10))
                .getAttribute("maxlength");

        return Integer.parseInt(maxLength);
    }

    public int getNumberFieldMaxLength() {
        return getFieldMaxLength(numberField);
    }

    public int getMonthFieldMaxLength() {
        return getFieldMaxLength(monthField);
    }

    public int getYearFieldMaxLength() {
        return getFieldMaxLength(yearField);
    }

    public int getHolderFieldMaxLength() {
        return getFieldMaxLength(holderField);
    }

    public int getCodeFieldMaxLength() {
        return getFieldMaxLength(codeField);
    }

    /**
     * Получение плейсхолдера поля
     */
    @Step("Проверяем, появляется ли плейсхолдер в пустом поле и получаем его значение")
    private String getFieldPlaceholder(SelenideElement field) {
        field.parent().shouldHave(cssClass("input_focused"), Duration.ofMillis(20));
        String placeholder = field.$("input.input__control")
                .shouldBe(visible, Duration.ofMillis(20))
                .getAttribute("placeholder");

        return placeholder;
    }

    public String getNumberFieldPlaceholder() {
        return getFieldPlaceholder(numberField);
    }

    public String getMonthFieldPlaceholder() {
        return getFieldPlaceholder(monthField);
    }

    public String getYearFieldPlaceholder() {
        return getFieldPlaceholder(yearField);
    }

    public String getHolderFieldPlaceholder() {
        return getFieldPlaceholder(holderField);
    }

    public String getCodeFieldPlaceholder() {
        return getFieldPlaceholder(codeField);
    }
}