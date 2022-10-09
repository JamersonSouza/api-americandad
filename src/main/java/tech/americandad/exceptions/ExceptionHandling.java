package tech.americandad.exceptions.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import tech.americandad.domain.HttpResponse;

@RestControllerAdvice
public class ExceptionHandling {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private static final String CONTA_BLOQUEADA = "Conta bloqueada. Contate o administrador do sistema";
    private static final String METODO_NAO_PERMITIDO = "A operação solicitada não é permitida.";
    private static final String INTERNAL_SERVER_ERROR = "Ocorreu um erro durante o processamento da requisição";
    private static final String CREDENCIAIS_INCORRETAS = "Usuário ou senha inválido. Por favor tente novamente!";
    private static final String CONTA_DESATIVADA = "Sua conta foi desativada. Se isso for um erro, entre em contato com a administração";
    private static final String ERROR_PROCESSAR_ARQUIVO = "Ocorreu um erro durante o processamento do arquivo";
    private static final String PERMISSAO_NAO_SUFICIENTE = "Você não tem permissão suficiente para esta ação";
    public static final String ERROR_PATH = "/error";


    @ExceptionHandler(DisabledException.class)
    private ResponseEntity<HttpResponse> contaBloqueadaException(){
        return createHttpResponse(HttpStatus.BAD_REQUEST, CONTA_BLOQUEADA);
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
