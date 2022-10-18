package tech.americandad.ListenerEvents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import tech.americandad.domain.User;
import tech.americandad.service.LoginTentativaService;

@Component
public class SucessoAutenticacaoListener {

    private LoginTentativaService loginTentativaService;

    @Autowired
    public SucessoAutenticacaoListener(LoginTentativaService loginTentativaService) {
        this.loginTentativaService = loginTentativaService;
    }

    //evento que escuta quando o login houve sucesso no login do usuario e remove as tentativas de login do cache
    @EventListener
    public void onSucessoAutenticacao(AuthenticationSuccessEvent event){

        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof User){
            User user = (User) event.getAuthentication().getPrincipal();
            loginTentativaService.removerUserLoginTentativaCache(user.getUsuario());
        }

    }
    
}
