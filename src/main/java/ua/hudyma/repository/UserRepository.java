package ua.hudyma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.hudyma.domain.User;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
}
