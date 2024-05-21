package skilltest.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skilltest.bookstore.model.FullName;

@Repository
public interface FullNameRepository extends JpaRepository<FullName, Long> {
}
