package skilltest.bookstore.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@RequiredArgsConstructor
@Getter
@SuperBuilder
public final class AuthorDto {

    private Long id;
    private FullNameDto fullName;
}

