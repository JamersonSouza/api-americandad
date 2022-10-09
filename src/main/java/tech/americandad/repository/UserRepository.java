package tech.americandad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tech.americandad.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findUserByUsuario (String usuario);

    User findUserByEmail (String email);
    
}
