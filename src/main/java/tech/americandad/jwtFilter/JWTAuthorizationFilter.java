package tech.americandad.jwtFilter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import tech.americandad.Util.TokenJWTProvider;
import tech.americandad.constants.SecurityConstant;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter{

    private TokenJWTProvider tokenJWTProvider;

    
    //construtor para injetar a classe
    public JWTAuthorizationFilter(TokenJWTProvider tokenJWTProvider) {
        this.tokenJWTProvider = tokenJWTProvider;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, 
    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
      
        if(request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONAL_HTTP_METHOD)){
            response.setStatus(HttpStatus.OK.value());
        }else{
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstant.TOKEN_HEADER)){
                filterChain.doFilter(request, response);
                return;
            }

            String token = authorizationHeader.substring(SecurityConstant.TOKEN_HEADER.length());
            String username = tokenJWTProvider.getSubject(token);

            if(tokenJWTProvider.isTokenValido(username, token) && SecurityContextHolder.getContext().getAuthentication() == null){ // caso estivesse usando seção deveria fazer esta verificação  && SecurityContextHolder.getContext().getAuthentication() == null
                    List<GrantedAuthority> authorities = tokenJWTProvider.getAuthorities(token);
                    Authentication authentication = tokenJWTProvider.geAuthentication(username, authorities, request);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                SecurityContextHolder.clearContext();
            }

        }
            filterChain.doFilter(request, response);
    }
}
