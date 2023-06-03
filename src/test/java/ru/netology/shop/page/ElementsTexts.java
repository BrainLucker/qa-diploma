package ru.netology.shop.page;

public class ElementsTexts {
    public static class Headings {
        private Headings() {
        }

        public static final String TOUR_OF_THE_DAY = "Путешествие дня";
        public static final String PAYMENT = "Оплата по карте";
        public static final String CREDIT = "Кредит по данным карты";
    }

    public static class Buttons {
        private Buttons() {
        }

        public static final String BUY = "Купить";
        public static final String CREDIT = "Купить в кредит";
        public static final String SUBMIT = "Продолжить";
        public static final String LOADING = "Отправляем запрос в Банк...";
    }

    public static class Notifications {
        private Notifications() {
        }

        public static final String SUCCESS_TITLE = "Успешно";
        public static final String SUCCESS_TEXT = "Операция одобрена Банком.";
        public static final String ERROR_TITLE = "Ошибка";
        public static final String ERROR_TEXT = "Ошибка! Банк отказал в проведении операции.";
    }

    public static class Errors {
        private Errors() {
        }

        public static final String EMPTY_FIELD = "Поле обязательно для заполнения";
        public static final String INCORRECT_FORMAT = "Неверный формат";
        public static final String INCORRECT_EXPIRATION_DATE = "Неверно указан срок действия карты";
        public static final String EXPIRED_DATE = "Истёк срок действия карты";
    }

}