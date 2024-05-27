package skilltest.bookstore.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@RequiredArgsConstructor
@Getter
@SuperBuilder
public final class CustomerDto {

    private long id;
    private FullNameDto fullName;
    @Email
    private String email;
}
