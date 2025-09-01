package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.hudyma.domain.Grade;

@Repository
public interface GradeRepository extends JpaRepository <Grade, Long> {
}
