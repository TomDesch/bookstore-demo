package skilltest.bookstore.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import skilltest.bookstore.validator.ValidIsbn;

@RequiredArgsConstructor
@Getter
@SuperBuilder
public final class BookDto {

    @ValidIsbn
    private String isbn;
    private AuthorDto author;
    private BigDecimal price;
    private String description;
    private int stock;
}
