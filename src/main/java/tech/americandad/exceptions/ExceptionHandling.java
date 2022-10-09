package tech.americandad.exceptions;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import tech.americandad.exceptions.domain.*;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Objects;

import javax.persistence.NoResultException;

import com.auth0.jwt.exceptions.TokenExpiredException;

import tech.americandad.domain.HttpResponse;

@RestControllerAdvice
public class ExceptionHandling {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private static final String CONTA_BLOQUEADA = "Conta bloqueada. Contate o administrador do sistema";
    private static final String METODO_NAO_PERMITIDO = "A operação solicitada não é permitida.";
    private static final String INTERNAL_SERVER_ERRORS = "Ocorreu um erro durante o processamento da requisição";
    private static final String CREDENCIAIS_INCORRETAS = "Usuário ou senha inválido. Por favor tente novamente!";
    private static final String CONTA_DESATIVADA = "Sua conta foi desativada. Se isso for um erro, entre em contato com a administração";
    private static final String ERROR_PROCESSAR_ARQUIVO = "Ocorreu um erro durante o processamento do arquivo";
    private static final String PERMISSAO_NAO_SUFICIENTE = "Você não tem permissão suficiente para esta ação";
    public static final String ERROR_PATH = "/error";


    @ExceptionHandler(DisabledException.class)
    private ResponseEntity<HttpResponse> contaBloqueadaException(){
        return createHttpResponse(HttpStatus.BAD_REQUEST, CONTA_BLOQUEADA);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, CREDENCIAIS_INCORRETAS);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return createHttpResponse(HttpStatus.FORBIDDEN, PERMISSAO_NAO_SUFICIENTE);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, CONTA_DESATIVADA);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistsException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsuarioExistsException.class)
    public ResponseEntity<HttpResponse> userExistsException(UsuarioExistsException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UsuarioNotFoundException exception) {
        return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METODO_NAO_PERMITIDO, supportedMethod));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        LOG.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERRORS);
    }


    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
        LOG.error(exception.getMessage());
        return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        LOG.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSAR_ARQUIVO);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
        //passando o construtor da Classe HttpResponse e parâmetros como 1º parametro de retorno do  ResponseEntity.
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
        message.toUpperCase()), httpStatus);

            /* uma forma alternativa de fazer o método
            HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
             message);

             return new ResponseEntity<HttpResponse>(httpResponse, httpStatus);
            */

    }
    
}
