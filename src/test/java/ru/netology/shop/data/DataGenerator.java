package ru.netology.shop.data;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Locale;

public class DataGenerator {

    public static String generateApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String generateDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    private static LocalDate getDate(int yearsToAdd, int monthToAdd) {
        return LocalDate.now().plusYears(yearsToAdd).plusMonths(monthToAdd);
    }

    public static LocalDate generateValidCardDate() {
        long minDay = getDate(0, 0).toEpochDay();
        long maxDay = getDate(4, 0).minusMonths(1).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static String getMonth(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getYear(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String generateHolder() {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String generateCode() {
        Faker faker = new Faker();
        return faker.number().digits(3);
    }

    public static class Cards {
        private Cards() {
        }

        private static CardInfo generateCardInfo(String cardNumber, LocalDate cardDate) {
            return new CardInfo(cardNumber, getYear(cardDate), getMonth(cardDate), generateHolder(), generateCode());
        }

        public static CardInfo generateValidApprovedCard() {
            return generateCardInfo(generateApprovedCardNumber(), generateValidCardDate());
        }

        public static CardInfo generateValidDeclinedCard() {
            return generateCardInfo(generateDeclinedCardNumber(), generateValidCardDate());
        }

        public static CardInfo generateApprovedCardExpiringIn(int years, int months) {
            return generateCardInfo(generateApprovedCardNumber(), getDate(years, months));
        }

        public static CardInfo generateCardAndSetNumber(String number) {
            var cardDate = generateValidCardDate();
            return new CardInfo(number, getYear(cardDate), getMonth(cardDate), generateHolder(), generateCode());
        }

        public static CardInfo generateCardAndSetYear(String year) {
            var cardDate = generateValidCardDate();
            return new CardInfo(generateApprovedCardNumber(), year, getMonth(cardDate), generateHolder(), generateCode());
        }

        public static CardInfo generateCardAndSetMonth(String month) {
            var cardDate = generateValidCardDate();
            return new CardInfo(generateApprovedCardNumber(), getYear(cardDate), month, generateHolder(), generateCode());
        }

        public static CardInfo generateCardAndSetHolder(String holder) {
            var cardDate = generateValidCardDate();
            return new CardInfo(generateApprovedCardNumber(), getYear(cardDate), getMonth(cardDate), holder, generateCode());
        }

        public static CardInfo generateCardAndSetCode(String code) {
            var cardDate = generateValidCardDate();
            return new CardInfo(generateApprovedCardNumber(), getYear(cardDate), getMonth(cardDate), generateHolder(), code);
        }
    }

    @Value
    @AllArgsConstructor
    public static class CardInfo {
        String number;
        String year;
        String month;
        String holder;
        String cvc;
    }

}