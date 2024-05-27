package skilltest.bookstore.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IsbnValidatorTest {

    private final IsbnValidator validator = new IsbnValidator();
    private final ConstraintValidatorContext context = mock(ConstraintValidatorContext.class);

    @BeforeEach
    void setUp() {
        validator.initialize(null);
    }

    @Test
    void isValid_NullInput_ReturnsFalse() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void isValid_InvalidFormat_ReturnsFalse() {
        assertFalse(validator.isValid("12345", context));
    }

    @Test
    void isValid_ValidIsbn10_ReturnsTrue() {
        assertTrue(validator.isValid("0471958697", context));
    }

    @Test
    void isValid_ValidIsbn10WithX_ReturnsTrue() {
        assertTrue(validator.isValid("080442957X", context));
    }

    @Test
    void isValid_InvalidIsbn10_ReturnsFalse() {
        assertFalse(validator.isValid("0471958698", context));
    }

    @Test
    void isValid_ValidIsbn13_ReturnsTrue() {
        assertTrue(validator.isValid("9783836221191", context));
    }

    @Test
    void isValid_InvalidIsbn13_ReturnsFalse() {
        assertFalse(validator.isValid("9783836221190", context));
    }

    @Test
    void isValid_InvalidIsbn_ReturnsFalse() {
        assertFalse(validator.isValid("1234567890", context));
    }

    @Test
    void isValid_InvalidIsbnWithLetters_ReturnsFalse() {
        assertFalse(validator.isValid("97838362211A1", context));
    }
}