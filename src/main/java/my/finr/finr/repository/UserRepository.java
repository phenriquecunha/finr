package my.finr.finr.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import my.finr.finr.enumeration.RoleEnum;
import my.finr.finr.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);
    List<User> findAllByRole(RoleEnum role);
}
