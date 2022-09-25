package tech.americandad.Util;

import org.springframework.beans.factory.annotation.Value;

public class TokenJWTProvider {

    @Value("jwt.secret")
    private String secret;

    
}
