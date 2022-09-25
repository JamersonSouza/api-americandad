package tech.americandad.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static java.util.Arrays.stream;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import com.auth0.jwt.JWT;
import org.apache.commons.lang3.StringUtils;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import tech.americandad.constants.SecurityConstant;
import tech.americandad.domain.UserPrincipal;

public class TokenJWTProvider {

    @Value("${jwt.secret}")
    private String secret;

    public String generateJWTToken(UserPrincipal userPrincipal){
        String[] claims = getClaimsFromUser(userPrincipal);

        return JWT.create().withIssuer(SecurityConstant.GET_ARRAYS).withAudience(SecurityConstant.GET_ARRAYS_ADMIN)
                .withIssuedAt(new Date()).withSubject(userPrincipal.getUsername())
                .withArrayClaim(SecurityConstant.AUTHORITIES, claims).withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.TIME_EXPIRATION))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }


    public Authentication geAuthentication(String username, List<GrantedAuthority> authorities
    , HttpServletRequest request){
        UsernamePasswordAuthenticationToken usernamePasswordAuthToken = new 
        UsernamePasswordAuthenticationToken(username, null, authorities);

        usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return usernamePasswordAuthToken;

    }


    public boolean isTokenValido(String username, String token){

        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(username) && !isTokenExpirado(verifier, token);
    }

    private boolean isTokenExpirado(JWTVerifier verifier, String token) {
        Date expiracao = verifier.verify(token).getExpiresAt();
        return expiracao.before(new Date());
    }


    private String[] getClaimsFromUser(UserPrincipal user){
        List<String> authorities = new ArrayList<>();
        for(GrantedAuthority grantedAuthority : user.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }

    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(SecurityConstant.AUTHORITIES).asArray(String.class);
    }

    private JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer(SecurityConstant.GET_ARRAYS).build();
        } catch (JWTVerificationException  e) {
            throw new JWTVerificationException(SecurityConstant.TOKEN_NAO_VERIFICADO);
        }     
        return null;
    }
    
    public String getSubject(String token){
        JWTVerifier verifier = getJWTVerifier();
        return  verifier.verify(token).getSubject();
    }
}
