package tech.americandad.Enums;

import tech.americandad.constants.Authority;

public enum Role {

    ROLE_USER(Authority.USER_AUTHORITIES),
    ROLE_RH(Authority.RH_AUTHORITIES),
    ROLE_MANAGER(Authority.MANAGER_AUTHORITIES),
    ROLE_ADMIN(Authority.ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(Authority.SUPER_USER_AUTHORITIES);

    private String[] authorities;

    //Nesse Construtor pode passar diretamente infinitas Strings, sem colocar dentro de uma lista
    Role(String... authorities){
        this.authorities = authorities;
    }

    public String[] getAuthorities(){
        return authorities;
    }
 
}
