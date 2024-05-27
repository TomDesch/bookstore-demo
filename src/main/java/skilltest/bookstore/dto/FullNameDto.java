package skilltest.bookstore.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;


@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@SuperBuilder
public final class FullNameDto {

    private Long id;
    private String firstName;
    private String lastName;
}
