package tech.americandad.constants;

public class SecurityConstant {

    public static final long TIME_EXPIRATION = 432_000_000;
    public static final String TOKEN_HEADER = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_NAO_VERIFICADO = "Token não pode ser verificado";
    public static final String GET_ARRAYS = "Get Array, LLC";
    public static final String GET_ARRAYS_ADMIN = "Usuario administrador do AmericanDAD";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "Você precisa estar logado para acessar essa página";
    public static final String ACESSO_NEGADO_MESSAGE = "Você não tem permissão de acesso";
    public static final String OPTIONAL_HTTP_METHOD = "Opcional";
    
    public static final String[] URLS_PUBLICA = {"/user/login", "/user/registro", "/user/resetpassword/**", 
    "/user/imagem/**"};
    // testes | public static final String[] URLS_PUBLICA = {"**"};

}
