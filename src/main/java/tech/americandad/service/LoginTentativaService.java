package tech.americandad.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class LoginTentativaService {

    private static final int NUM_MAX_TENTATIVAS = 5;

    private static final int TENTATIVAS_INCREMENT = 1;

    private LoadingCache<String, Integer> loginTentativasCache;

    //construtor que define o tempo max e min do cache guardado para as tentativas
    public LoginTentativaService() {
        super();

        loginTentativasCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(100).build(new CacheLoader<String,Integer>() {
                public Integer load(String key){
                    return 0;
                }
            });

    }

    //método que remove as tentativas de login do usuário do cache
    public void removerUserLoginTentativaCache(String username){
        loginTentativasCache.invalidate(username);
    }
    //método que add  tentativa de login do usuário do cache
    public void addUserLoginTentativaCache(String username) throws ExecutionException{
        int tentativa = 0;
        tentativa = TENTATIVAS_INCREMENT + loginTentativasCache.get(username);
        loginTentativasCache.put(username, tentativa);
    

    }

    public boolean ultrassouNumeroTentativas(String username) throws ExecutionException{

        return loginTentativasCache.get(username) >= NUM_MAX_TENTATIVAS;


    }
    
}
