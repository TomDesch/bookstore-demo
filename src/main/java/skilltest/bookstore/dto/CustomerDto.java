package skilltest.bookstore.dto;

import jakarta.validation.constraints.Email;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder
public final class CustomerDto {

    private long id;
    private FullNameDto fullName;
    @Email
    private String email;
}
