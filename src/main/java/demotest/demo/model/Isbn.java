package demotest.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Isbn {

    @Id
    @Column(name = "isbn", nullable = false, length = 13)
    @ValidIsbn
    private String isbn;
}
