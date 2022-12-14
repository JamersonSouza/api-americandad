package tech.americandad.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import tech.americandad.Enums.Role;
import tech.americandad.constants.EmailConstant;
import tech.americandad.constants.FileConstants;
import tech.americandad.constants.UserImpConstants;
import tech.americandad.domain.User;
import tech.americandad.domain.UserPrincipal;
import tech.americandad.exceptions.domain.EmailExistsException;
import tech.americandad.exceptions.domain.EmailNotFoundException;
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
            LOG.error("Usu??rio " + username + " n??o encontrado. Tente novamente");
           throw new UsernameNotFoundException("Usu??rio " + username + " n??o encontrado. Tente novamente");
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
            //se o usu??rio ultrapassar o numero de tentativas tera a conta bloqueada
            if(loginTentativaService.ultrassouNumeroTentativas(user.getUsuario())){
                user.setDesbloqueado(false);
            }else{
                user.setDesbloqueado(true);
            }
        }else{
            loginTentativaService.removerUserLoginTentativaCache(user.getUsuario());
        }
    }



    //m??todo que inseri um usu??rio no banco de dados
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
        user.setImagemPerfilUrl(getImagemTemporariaProfileUrl(usuario));
        userRepository.save(user);
        LOG.info("Novo Usuario com senha: " + password);
        emailService.envioPasswordEmail(nome, password, email);
        return user;
    }


    //m??todo que pega a url atual + concatena????o com o diretorio da img
    private String getImagemTemporariaProfileUrl(String usuario) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstants.DEFAULT_USER_IMAGE_PATH + usuario).toUriString();
    }


    //m??todo de encoder da senha
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


    //m??todo que gera a senha
    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }


    //m??todo gerador de IDUsuario
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
                throw new UsuarioExistsException("Usu??rio j?? cadastrado");
            }
         
            if(userByEmail != null && !userAtual.getId().equals(userByEmail.getId())){
                throw new EmailExistsException("E-mail j?? cadastrado");
            }

            return userAtual;

        }else {
           
            if(userByNomeUsuario != null){
                throw new UsuarioExistsException("Usu??rio j?? cadastrado");
            }

            if(userByEmail != null){
                throw new EmailExistsException("E-mail j?? cadastrado");
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





    @Override
    public User addNovoUsuario(String nome, String sobrenome, String usuario, String email, String role,
            boolean isDesbloqueado, boolean isAtivo, MultipartFile imagemPerfil) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException, IOException {
        
        validaNovoUsuarioAndEmail(EMPTY, usuario, email);
        User user = new User();
        String password = generatePassword();
        String encodePassword = encodePassword(password);
        user.setUserId(generateUserId());
        user.setNome(nome);
        user.setSobrenome(sobrenome);
        user.setUsuario(usuario);
        user.setEmail(email);
        user.setDataRegistro(new Date());
        user.setSenha(encodePassword);
        user.setAtivo(true);
        user.setDesbloqueado(true);
        user.setRole(getRoleEnumNome(role).name());
        user.setAuthorities(getRoleEnumNome(role).getAuthorities());
        user.setImagemPerfilUrl(getImagemTemporariaProfileUrl(usuario));
        userRepository.save(user);
        saveImagemPerfil(user, imagemPerfil);
        return user;
    }






    @Override
    public User updateUsuario(String usuarioAtual, String novoNome, String novoSobrenome, String novoUsuario,
            String novoEmail, String role, boolean isDesbloqueado, boolean isAtivo, MultipartFile imagemPerfil) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException, IOException {
               User userAtual =  validaNovoUsuarioAndEmail(usuarioAtual, novoNome, novoEmail);
                userAtual.setNome(novoNome);
                userAtual.setSobrenome(novoSobrenome);
                userAtual.setUsuario(novoUsuario);
                userAtual.setEmail(novoEmail);
                userAtual.setAtivo(true);
                userAtual.setDesbloqueado(true);
                userAtual.setRole(getRoleEnumNome(role).name());
                userRepository.save(userAtual);
                saveImagemPerfil(userAtual, imagemPerfil);
                return userAtual;
    }



    @Override
    public void deletaUsuario(Long id) {
        userRepository.deleteById(id);
        
    }



    @Override
    public void resetPassword(String email) throws EmailNotFoundException  {
        User user = userRepository.findUserByEmail(email);
        if(user  == null) {
            throw new EmailNotFoundException( UserImpConstants.NO_USER_FOUND_BY_EMAIL  + email);
            
        }
        String senha = generatePassword();
        user.setSenha(encodePassword(senha));
        userRepository.save(user);
        emailService.envioPasswordEmail(user.getNome(), senha, user.getEmail());
        
    }



    @Override
    public User updateImagemPerfil(String usuario, MultipartFile imagemPerfil) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException, IOException {
        User user = validaNovoUsuarioAndEmail(usuario, null, null);
        saveImagemPerfil(user, imagemPerfil);
        return user;
    }

    private void saveImagemPerfil(User user, MultipartFile imagemPerfil) throws IOException {

        if(imagemPerfil != null){
            Path userFolder = Paths.get(FileConstants.USER_FOLDER, user.getUsuario())
            .toAbsolutePath().normalize();
            if(Files.exists(userFolder)){
                Files.createDirectories(userFolder);
                LOG.info(FileConstants.DIRECTORY_CREATED);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsuario() + FileConstants.DOT + 
            FileConstants.JPG_EXTENSION));

            Files.copy(imagemPerfil.getInputStream(), userFolder.resolve(user.getUsuario() 
            + FileConstants.DOT + 
            FileConstants.JPG_EXTENSION), REPLACE_EXISTING);
            user.setImagemPerfilUrl(setProfileImageUrl(user.getUsuario()));
            userRepository.save(user);
            LOG.info(FileConstants.FILE_SAVED_IN_FILE_SYSTEM + imagemPerfil.getOriginalFilename());
        }

    }

    private Role getRoleEnumNome(String role) {
        return Role.valueOf(role.toUpperCase());
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(FileConstants.USER_IMAGE_PATH + username + FileConstants.FORWARD_SLASH
        + username + FileConstants.DOT + 
        FileConstants.JPG_EXTENSION).toUriString();
    }

    
}
