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
    void isValid_nullInput_returnsFalse() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void isValid_invalidFormat_returnsFalse() {
        assertFalse(validator.isValid("12345", context));
    }

    @Test
    void isValid_validIsbn10_returnsTrue() {
        assertTrue(validator.isValid("0471958697", context));
    }

    @Test
    void isValid_validIsbn10WithX_returnsTrue() {
        assertTrue(validator.isValid("080442957X", context));
    }

    @Test
    void isValid_invalidIsbn10_returnsFalse() {
        assertFalse(validator.isValid("0471958698", context));
    }

    @Test
    void isValid_validIsbn13_returnsTrue() {
        assertTrue(validator.isValid("9783836221191", context));
    }

    @Test
    void isValid_invalidIsbn13_returnsFalse() {
        assertFalse(validator.isValid("9783836221190", context));
    }

    @Test
    void isValid_invalidIsbn_returnsFalse() {
        assertFalse(validator.isValid("1234567890", context));
    }

    @Test
    void isValid_invalidIsbnWithLetters_returnsFalse() {
        assertFalse(validator.isValid("97838362211A1", context));
    }
}