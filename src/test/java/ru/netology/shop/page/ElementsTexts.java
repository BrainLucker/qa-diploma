package ru.netology.shop.page;

public class ElementsTexts {
    public static class Headings {
        private Headings() {
        }

        public static final String tourOfTheDay = "Путешествие дня";
        public static final String payment = "Оплата по карте";
        public static final String credit = "Кредит по данным карты";
    }

    public static class Buttons {
        private Buttons() {
        }

        public static final String buy = "Купить";
        public static final String credit = "Купить в кредит";
        public static final String submit = "Продолжить";
        public static final String loading = "Отправляем запрос в Банк...";
    }

    public static class Notifications {
        private Notifications() {
        }

        public static final String[] success = {"Успешно", "Операция одобрена Банком."};
        public static final String[] error = {"Ошибка", "Ошибка! Банк отказал в проведении операции."};
    }

    public static class Errors {
        private Errors() {
        }

        public static final String emptyField = "Поле обязательно для заполнения";
        public static final String incorrectFormat = "Неверный формат";
        public static final String incorrectExpirationDate = "Неверно указан срок действия карты";
        public static final String expiredDate = "Истёк срок действия карты";
    }
}