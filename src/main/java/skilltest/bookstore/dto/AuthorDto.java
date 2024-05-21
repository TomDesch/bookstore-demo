package skilltest.bookstore.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@SuperBuilder
public final class AuthorDto {

    private Long id;
    private FullNameDto fullName;
}

