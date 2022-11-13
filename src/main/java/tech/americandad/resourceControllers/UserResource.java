package tech.americandad.resourceControllers;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import tech.americandad.Util.TokenJWTProvider;
import tech.americandad.constants.SecurityConstant;
import tech.americandad.domain.HttpResponse;
import tech.americandad.domain.User;
import tech.americandad.domain.UserPrincipal;
import tech.americandad.exceptions.ExceptionHandling;
import tech.americandad.exceptions.domain.EmailExistsException;
import tech.americandad.exceptions.domain.EmailNotFoundException;
import tech.americandad.exceptions.domain.UsuarioExistsException;
import tech.americandad.exceptions.domain.UsuarioNotFoundException;
import tech.americandad.service.UserService;

@RestController
@RequestMapping("/user")
public class UserResource extends ExceptionHandling{

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private TokenJWTProvider tokenJWTProvider;

    
    @Autowired

    public UserResource(UserService userService, AuthenticationManager authenticationManager,
            TokenJWTProvider tokenJWTProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenJWTProvider = tokenJWTProvider;
    }

    

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        autenticacao(user.getUsuario(), user.getSenha());
        User loginUser = userService.findUserByUsuario(user.getUsuario());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeaders = getJwtHeaders(userPrincipal);
        return new ResponseEntity<User>(loginUser, jwtHeaders, HttpStatus.OK);
    }



    private void autenticacao(String usuario, String senha) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario, senha));
    }
    
    private HttpHeaders getJwtHeaders(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, tokenJWTProvider.generateJWTToken(userPrincipal));
        return headers;
     }



    @PostMapping("/registro")
    public ResponseEntity<User> registro(@RequestBody User user) throws UsuarioExistsException, EmailExistsException, UsuarioNotFoundException{
       User newUser =  userService.registro(user.getNome(), user.getSobrenome(), user.getUsuario(), user.getEmail());
        return new ResponseEntity<User>(newUser, HttpStatus.OK);
    }


    @PostMapping("novo-usuario")
    public ResponseEntity<User> novoUsuario(@RequestParam("nome") String nome,
                                            @RequestParam("sobrenome") String sobrenome,
                                            @RequestParam("usuario") String usuario,
                                            @RequestParam("email") String email,
                                            @RequestParam("role") String role,
                                            @RequestParam("isAtivo") String isAtivo,
                                            @RequestParam("isDesbloqueado") String isDesbloqueado,
                                            @RequestParam(value = "imagemPerfilUrl", required = false) MultipartFile imagemPerfilUrl) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException, IOException{

            User newUser = userService.addNovoUsuario(nome, sobrenome, usuario, email, role, Boolean.parseBoolean(isDesbloqueado), Boolean.parseBoolean(isAtivo), imagemPerfilUrl);

            return new ResponseEntity<>(newUser, HttpStatus.OK);



    }

    @PostMapping("atualizar-usuario")
    public ResponseEntity<User> updateUsuario(@RequestParam("currentUsuario") String currentUsuario,
                                            @RequestParam("nome") String nome,
                                            @RequestParam("sobrenome") String sobrenome,
                                            @RequestParam("usuario") String usuario,
                                            @RequestParam("email") String email,
                                            @RequestParam("role") String role,
                                            @RequestParam("isAtivo") String isAtivo,
                                            @RequestParam("isDesbloqueado") String isDesbloqueado,
                                            @RequestParam(value = "imagemPerfilUrl", required = false) MultipartFile imagemPerfilUrl) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException, IOException{

            User updateUser = userService.updateUsuario(currentUsuario, nome, sobrenome, usuario, email, role, Boolean.parseBoolean(isDesbloqueado), Boolean.parseBoolean(isAtivo), imagemPerfilUrl);

            return new ResponseEntity<>(updateUser, HttpStatus.OK);



    }
 
    @GetMapping("/buscar/{usuario}")
    public ResponseEntity<User> buscarUsuario(@PathVariable("usuario") String usuario){

        User user = userService.findUserByUsuario(usuario);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<User>> buscarUsuarios(){

        List<User> user = userService.listUsers();
        return new ResponseEntity<>(user, HttpStatus.OK);

    }

     @GetMapping("/resetSenha/{email}")
    public ResponseEntity<HttpResponse> resetSenhaUsuario(@PathVariable("email") String email) throws EmailNotFoundException{

        userService.resetPassword(email);
        return resposta(HttpStatus.OK , "E-mail enviado para: " + email);

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") Long id){
        userService.deletaUsuario(id);
        return resposta(HttpStatus.OK, "Usuário Excluído com Sucesso!");

    }

    private ResponseEntity<HttpResponse> resposta(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
        message), httpStatus);
    }

    @PostMapping("atualizar-imagem-usuario")
    public ResponseEntity<User> updateImagemUsuario(@RequestParam("usuario") String usuario, @RequestParam(value = "imagemPerfilUrl") MultipartFile imagemPerfilUrl) throws EmailExistsException, UsuarioExistsException, UsuarioNotFoundException, IOException{

            User updateImagenUser = userService.updateImagemPerfil(usuario, imagemPerfilUrl);

            return new ResponseEntity<>(updateImagenUser, HttpStatus.OK);



    }
}
