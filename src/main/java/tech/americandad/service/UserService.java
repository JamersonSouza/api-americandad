package tech.americandad.service;

import java.util.List;

import tech.americandad.domain.User;
import tech.americandad.exceptions.domain.EmailExistsException;
import tech.americandad.exceptions.domain.UsuarioExistsException;
import tech.americandad.exceptions.domain.UsuarioNotFoundException;

public interface UserService {

    User registro(String nome, String sobrenome, String usuario, String email) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException;

    List<User> listUsers();

    User findUserByUsuario(String usuario);

    User findUserByEmail(String email);
    
}
