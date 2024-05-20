package demotest.demo.validator;

import demotest.demo.model.ValidIsbn;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<ValidIsbn, String> {

    private static final String ISBN_REGEX = "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$";
    private Pattern pattern;

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
        return matcher.matches();
    }
}
