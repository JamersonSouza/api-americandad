package tech.americandad.resourceControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.americandad.domain.User;
import tech.americandad.exceptions.ExceptionHandling;
import tech.americandad.exceptions.domain.EmailExistsException;
import tech.americandad.exceptions.domain.UsuarioExistsException;
import tech.americandad.exceptions.domain.UsuarioNotFoundException;
import tech.americandad.service.UserService;

@RestController
@RequestMapping("/user")
public class UserResource extends ExceptionHandling{

    private UserService userService;

    
    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/registro")
    public ResponseEntity<User> registro(@RequestBody User user) throws UsuarioExistsException, EmailExistsException, UsuarioNotFoundException{
       User newUser =  userService.registro(user.getNome(), user.getSobrenome(), user.getUsuario(), user.getEmail());
        return new ResponseEntity<User>(newUser, HttpStatus.OK);
    }
    
}
