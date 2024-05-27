package skilltest.bookstore;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookStoreApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethodDoesNotThrowException() { // for Code Coverage ;)
		assertDoesNotThrow(() -> BookStoreApplication.main(new String[]{}));
	}

}
