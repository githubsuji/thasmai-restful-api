package in.prvak.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;

import in.prvak.jpa.entities.security.User;


/**
 * @author SUJIKUMAR
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
