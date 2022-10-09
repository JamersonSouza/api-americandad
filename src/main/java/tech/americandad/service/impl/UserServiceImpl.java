package tech.americandad.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import tech.americandad.domain.User;
import tech.americandad.domain.UserPrincipal;
import tech.americandad.repository.UserRepository;
import tech.americandad.service.UserService;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl  implements UserService, UserDetailsService{

    private Logger LOG = LoggerFactory.getLogger(getClass());

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByUsuario(username);

        if(user == null ){
            LOG.error("Usuário " + username + " não encontrado. Tente novamente");
           throw new UsernameNotFoundException("Usuário " + username + " não encontrado. Tente novamente");
        }else {
            user.setMostrarRegistroLogin(user.getMostrarRegistroLogin());
            user.setRegistroLogin(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOG.info("Retornando usuario encontrado: " + username);

            return userPrincipal;
        }

       
    }
    
}
