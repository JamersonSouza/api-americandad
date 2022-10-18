package tech.americandad.ListenerEvents;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import tech.americandad.service.LoginTentativaService;

@Component
public class FalhaAutenticacaoListener {

    private LoginTentativaService loginTentativaService;

    @Autowired
    public FalhaAutenticacaoListener(LoginTentativaService loginTentativaService) {
        this.loginTentativaService = loginTentativaService;
    }

    //método que escuta o evento de falha na autenticaçao e chama o método que adiciona tentativa de login.
    @EventListener
    public void onFalhaAutenticacao(AuthenticationFailureBadCredentialsEvent event){
        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof String){
            String username = (String) event.getAuthentication().getPrincipal();
            loginTentativaService.addUserLoginTentativaCache(username);
        }

    }
    
    
}
