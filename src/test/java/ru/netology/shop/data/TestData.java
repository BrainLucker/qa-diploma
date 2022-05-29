package ru.netology.shop.data;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static ru.netology.shop.page.ElementsTexts.Errors;
import static ru.netology.shop.data.DataGenerator.*;

public class TestData {
    public static Stream<Arguments> validCardInfo() {
        return Stream.of(
                arguments("Happy Path",
                        Cards.generateValidApprovedCard()),
                arguments("заканчивающийся срок действия",
                        Cards.generateApprovedCardExpiringIn(0, 0)),
                arguments("имя владельца в нижнем регистре",
                        Cards.generateCardAndSetHolder("card holder")),
                arguments("имя владельца в верхнем регистре",
                        Cards.generateCardAndSetHolder("CARD HOLDER")),
                arguments("имя владельца с дефисом",
                        Cards.generateCardAndSetHolder("Card-Holder")),
                arguments("имя владельца из 2 букв",
                        Cards.generateCardAndSetHolder("CH")),
                arguments("имя владельца из 27 букв",
                        Cards.generateCardAndSetHolder("Card Holder With Loong Name"))
        );
    }

    public static Stream<Arguments> notAllowableMonth() {
        return Stream.of(
                arguments("меньше допустимого на 1 месяц",
                        Cards.generateApprovedCardExpiringIn(0, -1), Errors.expiredDate),
                arguments("больше допустимого на 1 месяц",
                        Cards.generateApprovedCardExpiringIn(5, 1), Errors.incorrectExpirationDate)
        );
    }

    public static Stream<Arguments> notAllowableYear() {
        return Stream.of(
                arguments("меньше допустимого на 1 год",
                        Cards.generateApprovedCardExpiringIn(-1, 0), Errors.expiredDate),
                arguments("больше допустимого на 1 год",
                        Cards.generateApprovedCardExpiringIn(6, 0), Errors.incorrectExpirationDate)
        );
    }

    public static Stream<Arguments> invalidCardNumber() {
        return Stream.of(
                arguments("неверный формат",
                        Cards.generateCardAndSetNumber("1111 1111 1111 1111"), Errors.incorrectFormat),
                arguments("15 цифр",
                        Cards.generateCardAndSetNumber("4444 4444 4444 444"), Errors.incorrectFormat),
                arguments("буквы",
                        Cards.generateCardAndSetNumber("Look like card nmbr"), Errors.emptyField),
                arguments("спец. символы",
                        Cards.generateCardAndSetNumber(".... ,,,, **** ----"), Errors.emptyField),
                arguments("пробелы",
                        Cards.generateCardAndSetNumber("                "), Errors.emptyField),
                arguments("пустое поле",
                        Cards.generateCardAndSetNumber(""), Errors.emptyField)
        );
    }

    public static Stream<Arguments> invalidMonth() {
        return Stream.of(
                arguments("00",
                        Cards.generateCardAndSetMonth("00"), Errors.incorrectExpirationDate),
                arguments("13",
                        Cards.generateCardAndSetMonth("13"), Errors.incorrectExpirationDate),
                arguments("1 цифра",
                        Cards.generateCardAndSetMonth("1"), Errors.incorrectFormat),
                arguments("буквы",
                        Cards.generateCardAndSetMonth("mo"), Errors.emptyField),
                arguments("спец. символы",
                        Cards.generateCardAndSetMonth(".,"), Errors.emptyField),
                arguments("пробелы",
                        Cards.generateCardAndSetMonth("  "), Errors.emptyField),
                arguments("пустое поле",
                        Cards.generateCardAndSetMonth(""), Errors.emptyField)
        );
    }

    public static Stream<Arguments> invalidYear() {
        return Stream.of(
                arguments("00",
                        Cards.generateCardAndSetYear("00"), Errors.expiredDate),
                arguments("99",
                        Cards.generateCardAndSetYear("99"), Errors.incorrectExpirationDate),
                arguments("1 цифра",
                        Cards.generateCardAndSetYear("1"), Errors.incorrectFormat),
                arguments("буквы",
                        Cards.generateCardAndSetYear("ye"), Errors.emptyField),
                arguments("спец. символы",
                        Cards.generateCardAndSetYear(".,"), Errors.emptyField),
                arguments("пробелы",
                        Cards.generateCardAndSetYear("  "), Errors.emptyField),
                arguments("пустое поле",
                        Cards.generateCardAndSetYear(""), Errors.emptyField)
        );
    }

    public static Stream<Arguments> invalidHolderName() {
        return Stream.of(
                arguments("1 буква",
                        Cards.generateCardAndSetHolder("A"), Errors.incorrectFormat),
                arguments("кириллица",
                        Cards.generateCardAndSetHolder("Имя Владельца"), Errors.emptyField),
                arguments("цифры",
                        Cards.generateCardAndSetHolder("1234567890"), Errors.emptyField),
                arguments("спец. символы",
                        Cards.generateCardAndSetHolder("!@#$%^&№;%:?*()-+/\\"), Errors.emptyField),
                arguments("пробелы",
                        Cards.generateCardAndSetHolder("     "), Errors.emptyField),
                arguments("пустое поле",
                        Cards.generateCardAndSetHolder(""), Errors.emptyField)
        );
    }

    public static Stream<Arguments> invalidCode() {
        return Stream.of(
                arguments("2 цифры",
                        Cards.generateCardAndSetCode("12"), Errors.incorrectFormat),
                arguments("буквы",
                        Cards.generateCardAndSetCode("xyz"), Errors.emptyField),
                arguments("спец. символы",
                        Cards.generateCardAndSetCode(".,-"), Errors.emptyField),
                arguments("пробелы",
                        Cards.generateCardAndSetCode("   "), Errors.emptyField),
                arguments("пустое поле",
                        Cards.generateCardAndSetCode(""), Errors.emptyField)
        );
    }

    public static Stream<Arguments> validApiCardInfo() {
        return Stream.of(
                arguments("«APPROVED» карты",
                        Cards.generateValidApprovedCard(), "APPROVED"),
                arguments("«DECLINED» карты",
                        Cards.generateValidDeclinedCard(), "DECLINED")
        );
    }

    public static Stream<Arguments> invalidApiCardInfo() {
        return Stream.of(
                arguments("неправильный формат номера",
                        new CardInfo("1111 1111 1111 1111", "12", "2025", "Card Holder", "123")),
                arguments("пустой ключ «number»",
                        new CardInfo(null, "05", "2023", "Card Holder", "123")),
                arguments("пустой ключ «year»",
                        new CardInfo("4444 4444 4444 4441", null, "2025", "Card Holder", "123")),
                arguments("пустой ключ «month»",
                        new CardInfo("4444 4444 4444 4441", "12", null, "Card Holder", "123")),
                arguments("пустой ключ «holder»",
                        new CardInfo("4444 4444 4444 4441", "12", "2025", null, "123")),
                arguments("пустой ключ «cvc»",
                        new CardInfo("4444 4444 4444 4441", "12", "2025", "Card Holder", null)),
                arguments("пустое тело запроса",
                        new CardInfo(null, null, null, null, null))
        );
    }

    public static class Regex {
        private Regex() {
        }

        public static final String numberPlaceholder = "^(\\d{4}\\s){3}\\d{4}$";
        public static final String monthPlaceholder = "^0[1-9]|1[0-2]$";
        public static final String yearPlaceholder = "^\\d{2}$";
        public static final String holderPlaceholder = "^(?=.{3,27}$)[A-Z]+\\s[A-Z]+$";
        public static final String codePlaceholder = "^\\d{3}$";
    }
}