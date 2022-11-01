package tech.americandad.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import tech.americandad.domain.User;
import tech.americandad.exceptions.domain.EmailExistsException;
import tech.americandad.exceptions.domain.EmailNotFoundException;
import tech.americandad.exceptions.domain.UsuarioExistsException;
import tech.americandad.exceptions.domain.UsuarioNotFoundException;

public interface UserService {

    User registro(String nome, String sobrenome, String usuario, String email) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException;

    List<User> listUsers();

    User findUserByUsuario(String usuario);

    User findUserByEmail(String email);

    User addNovoUsuario(String nome, String sobrenome, String usuario, String email, String role, boolean isDesbloqueado, boolean isAtivo, MultipartFile imagemPerfil) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException;

    User updateUsuario(String usuarioAtual, String novoNome, String novoSobrenome, String novoUsuario, String novoEmail, String role, boolean isDesbloqueado, boolean isAtivo, MultipartFile imagemPerfil) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException;
    
    void deletaUsuario(Long id);

    void resetPassword(String email) throws EmailNotFoundException;

    User updateImagemPerfil(String usuario, MultipartFile imagemPerfil) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException;
}
