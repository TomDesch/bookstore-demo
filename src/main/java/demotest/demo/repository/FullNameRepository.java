package demotest.demo.repository;

import demotest.demo.model.FullName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FullNameRepository extends JpaRepository<FullName, Long> {
}
