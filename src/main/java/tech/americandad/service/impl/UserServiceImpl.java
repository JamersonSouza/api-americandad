package tech.americandad.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import tech.americandad.Enums.Role;
import tech.americandad.domain.User;
import tech.americandad.domain.UserPrincipal;
import tech.americandad.exceptions.domain.EmailExistsException;
import tech.americandad.exceptions.domain.UsuarioExistsException;
import tech.americandad.exceptions.domain.UsuarioNotFoundException;
import tech.americandad.repository.UserRepository;
import tech.americandad.service.EmailService;
import tech.americandad.service.LoginTentativaService;
import tech.americandad.service.UserService;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl  implements UserService, UserDetailsService{

    private Logger LOG = LoggerFactory.getLogger(getClass());

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private LoginTentativaService loginTentativaService;

    private EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoginTentativaService loginTentativaService,
    EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginTentativaService = loginTentativaService;
        this.emailService = emailService;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsuario(username);

        if(user == null ){
            LOG.error("Usuário " + username + " não encontrado. Tente novamente");
           throw new UsernameNotFoundException("Usuário " + username + " não encontrado. Tente novamente");
        }else {
            validaTentativaLogin(user);
            user.setMostrarRegistroLogin(user.getMostrarRegistroLogin());
            user.setRegistroLogin(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOG.info("Retornando usuario encontrado: " + username);

            return userPrincipal;
        }

       
    }



    private void validaTentativaLogin(User user) {
        if(user.isDesbloqueado()){
            //se o usuário ultrapassar o numero de tentativas tera a conta bloqueada
            if(loginTentativaService.ultrassouNumeroTentativas(user.getUsuario())){
                user.setDesbloqueado(false);
            }else{
                user.setDesbloqueado(true);
            }
        }else{
            loginTentativaService.removerUserLoginTentativaCache(user.getUsuario());
        }
    }



    //método que inseri um usuário no banco de dados
    @Override
    public User registro(String nome, String sobrenome, String usuario, String email) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException {
        validaNovoUsuarioAndEmail(StringUtils.EMPTY, usuario, email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodePassword = encodePassword(password);
        user.setNome(nome);
        user.setSobrenome(sobrenome);
        user.setUsuario(usuario);
        user.setEmail(email);
        user.setDataRegistro(new Date());
        user.setSenha(encodePassword);
        user.setAtivo(true);
        user.setDesbloqueado(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setImagemPerfilUrl(getImagemTemporariaProfileUrl());
        userRepository.save(user);
        LOG.info("Novo Usuario com senha: " + password);
        emailService.envioPasswordEmail(nome, password, email);
        return user;
    }


    //método que pega a url atual + concatenação com o diretorio da img
    private String getImagemTemporariaProfileUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/image/profile/temp").toUriString();
    }


    //método de encoder da senha
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


    //método que gera a senha
    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }


    //método gerador de IDUsuario
    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }


    
    private User validaNovoUsuarioAndEmail(String usuarioAtual, String novoUsuario, String novoEmail) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException {
       
        User userByNomeUsuario = findUserByUsuario(novoUsuario);
        User userByEmail = findUserByEmail(novoEmail);

        if(StringUtils.isNotBlank(usuarioAtual)){
            User userAtual = findUserByUsuario(usuarioAtual);
            if(userAtual == null){
                throw new UsuarioNotFoundException("Nenhum Usuario Encontrado");
            }
            
            if(userByNomeUsuario != null && !userAtual.getId().equals(userByNomeUsuario.getId())){
                throw new UsuarioExistsException("Usuário já cadastrado");
            }
         
            if(userByEmail != null && !userAtual.getId().equals(userByEmail.getId())){
                throw new EmailExistsException("E-mail já cadastrado");
            }

            return userAtual;

        }else {
           
            if(userByNomeUsuario != null){
                throw new UsuarioExistsException("Usuário já cadastrado");
            }

            if(userByEmail != null){
                throw new EmailExistsException("E-mail já cadastrado");
            }

            return null;
        }
    }



    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsuario(String usuario) {
        return userRepository.findUserByUsuario(usuario);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
    
}
