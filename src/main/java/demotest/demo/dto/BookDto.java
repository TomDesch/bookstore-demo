package demotest.demo.dto;

import java.math.BigDecimal;

public record BookDto(String isbn, AuthorDto author, BigDecimal price, String description, int stock) {

}
