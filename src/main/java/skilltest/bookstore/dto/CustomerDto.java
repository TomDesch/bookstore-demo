package skilltest.bookstore.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import skilltest.bookstore.validator.ValidEmail;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@SuperBuilder
public final class CustomerDto {

    private long id;
    private FullNameDto fullName;
    @ValidEmail
    private String email;
}
