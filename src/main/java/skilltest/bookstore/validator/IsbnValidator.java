package skilltest.bookstore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<ValidIsbn, String> {

    private static final String ISBN_REGEX = "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$";
    private Pattern pattern;
    private static final char ZERO = '0';
    private static final char NINE = '9';

    @Override
    public void initialize(ValidIsbn constraintAnnotation) {
        pattern = Pattern.compile(ISBN_REGEX);
    }

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (Objects.isNull(isbn)) {
            return false;
        }

        Matcher matcher = pattern.matcher(isbn);
        if (!matcher.matches()) {
            return false;
        }

        return isValidISBN(isbn);
    }


    private boolean isValidISBN(String isbn) {
        if (isbn.length() == 10) {
            return isValidISBN10(isbn);
        } else if (isbn.length() == 13) {
            return isValidISBN13(isbn);
        }
        return false;
    }

    private boolean isValidISBN10(String isbn) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            char character = isbn.charAt(i);
            if (character < ZERO || character > NINE) {
                return false;
            }
            int digit = character - ZERO;
            sum += (digit * (10 - i));
        }

        char check = isbn.charAt(9);
        if (check == 'X') {
            sum += 10;
        } else if (check >= ZERO && check <= NINE) {
            sum += (check - ZERO);
        } else {
            return false;
        }

        return sum % 11 == 0;
    }


    private boolean isValidISBN13(String isbn) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            char c = isbn.charAt(i);
            if (c < ZERO || c > NINE) {
                return false;
            }
            int digit = c - ZERO;
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = 10 - (sum % 10);
        if (checkDigit == 10) {
            checkDigit = 0;
        }

        char lastChar = isbn.charAt(12);
        if (lastChar < ZERO || lastChar > NINE) {
            return false;
        }
        int actualCheckDigit = lastChar - ZERO;

        return checkDigit == actualCheckDigit;
    }
}
