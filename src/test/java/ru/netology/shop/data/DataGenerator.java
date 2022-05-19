package ru.netology.shop.data;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {

    public static String generateApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String generateDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String generateInvalidCardNumber() {
        var faker = new Faker(new Locale("en"));
        return faker.finance().creditCard(CreditCardType.VISA).replace("-", " ");
    }

    private static LocalDate getDate(int yearsToAdd, int monthToAdd) {
        return LocalDate.now().plusYears(yearsToAdd).plusMonths(monthToAdd);
    }

    public static LocalDate generateValidCardDate() {
        var minDay = getDate(0, 0).toEpochDay();
        var maxDay = getDate(4, 0).minusMonths(1).toEpochDay();
        var randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static String getMonth(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getYear(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String generateHolder() {
        var faker = new Faker(new Locale("en"));
        var randomFirstName = faker.name().firstName().toUpperCase();
        var randomLastName = faker.name().lastName().toUpperCase();
        return randomFirstName + " " + randomLastName;
    }

    public static String generateCode() {
        var faker = new Faker();
        return faker.number().digits(3);
    }

    public static class Cards {
        private Cards() {
        }

        public static CardInfo newCardInfo(String cardNumber, LocalDate cardDate) {
            return new CardInfo(cardNumber, getYear(cardDate), getMonth(cardDate), generateHolder(), generateCode());
        }

        public static CardInfo generateValidApprovedCard() {
            return newCardInfo(generateApprovedCardNumber(), generateValidCardDate());
        }

        public static CardInfo generateValidDeclinedCard() {
            return newCardInfo(generateDeclinedCardNumber(), generateValidCardDate());
        }

        public static CardInfo generateApprovedCardExpiringIn(int years, int months) {
            return newCardInfo(generateApprovedCardNumber(), getDate(years,months));
        }

        public static CardInfo generateCardWithInvalidNumber() {
            return newCardInfo(generateInvalidCardNumber(), generateValidCardDate());
        }
    }

    @Setter
    @Value
    public static class CardInfo {
        String number;
        String year;
        String month;
        String holder;
        String cvc;
    }
}