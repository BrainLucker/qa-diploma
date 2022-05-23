package ru.netology.shop.data;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static ru.netology.shop.page.ElementsTexts.Errors;

public class TestData {
    public static Stream<String> validHolderNames() {
        return Stream.of(
                "card holder",
                "CARD HOLDER",
                "Card-Holder",
                "CH",
                "Card Holder With Loong Name"
        );
    }

    public static Stream<Arguments> notAllowableMonths() {
        return Stream.of(
                arguments(0, -1, Errors.expiredDateError),
                arguments(5, 1, Errors.invalidInputDateError)
        );
    }

    public static Stream<Arguments> notAllowableYears() {
        return Stream.of(
                arguments(-1, 0, Errors.expiredDateError),
                arguments(6, 0, Errors.invalidInputDateError)
        );
    }

    public static Stream<Arguments> invalidCardNumbers() {
        return Stream.of(
                arguments("1111 1111 1111 1111", Errors.invalidInputField),
                arguments("4444 4444 4444 444", Errors.invalidInputField),
                arguments("Look like card nmbr", Errors.emptyField),
                arguments(".... ,,,, **** ----", Errors.emptyField),
                arguments("                ", Errors.emptyField),
                arguments("", Errors.emptyField)
        );
    }

    public static Stream<Arguments> invalidMonths() {
        return Stream.of(
                arguments("00", Errors.invalidInputDateError),
                arguments("13", Errors.invalidInputDateError),
                arguments("1", Errors.invalidInputField),
                arguments("mo", Errors.emptyField),
                arguments(".,", Errors.emptyField),
                arguments("  ", Errors.emptyField),
                arguments("", Errors.emptyField)
        );
    }

    public static Stream<Arguments> invalidYears() {
        return Stream.of(
                arguments("00", Errors.expiredDateError),
                arguments("99", Errors.invalidInputDateError),
                arguments("1", Errors.invalidInputField),
                arguments("ye", Errors.emptyField),
                arguments(".,", Errors.emptyField),
                arguments("  ", Errors.emptyField),
                arguments("", Errors.emptyField)
        );
    }

    public static Stream<Arguments> invalidHolderNames() {
        return Stream.of(
                arguments("A", Errors.invalidInputField),
                arguments("Too Loooong Card Holder Name", Errors.invalidInputField),
                arguments("Имя Владельца", Errors.emptyField),
                arguments("1234567890", Errors.emptyField),
                arguments("!@#$%^&№;%:?*()-+/\\", Errors.emptyField),
                arguments("     ", Errors.emptyField),
                arguments("", Errors.emptyField)
        );
    }

    public static Stream<Arguments> invalidCodes() {
        return Stream.of(
                arguments("12", Errors.invalidInputField),
                arguments("xyz", Errors.emptyField),
                arguments(".,-", Errors.emptyField),
                arguments("   ", Errors.emptyField),
                arguments("", Errors.emptyField)
        );
    }

    public static class Regex {
        private Regex(){
        }

        public static final String numberPlaceholder = "^(\\d{4}\\s){3}\\d{4}$";
        public static final String monthPlaceholder = "^0[1-9]|1[0-2]$";
        public static final String yearPlaceholder = "^\\d{2}$";
        public static final String holderPlaceholder = "^(?=.{3,27}$)[A-Z]+\\s[A-Z]+$";
        public static final String codePlaceholder = "^\\d{3}$";
    }
}