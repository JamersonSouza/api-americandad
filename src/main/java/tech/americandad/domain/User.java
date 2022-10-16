package tech.americandad.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String userId;
    private String nome;
    private String sobrenome;
    private String usuario;
    private String senha;
    private String email;
    private String imagemPerfilUrl;
    private Date registroLogin;
    private Date mostrarRegistroLogin;
    private Date dataRegistro;
    private String role;//Funções User{leitura e edição} / Admin {delete}
    private String[] authorities;
    private boolean isAtivo;
    private boolean isDesbloqueado;

    

    public User() {
    }

    

    public User(Long id, String userId, String nome, String sobrenome, String usuario, String senha, String email,
            String imagemPerfilUrl, Date registroLogin, Date mostrarRegistroLogin, Date dataRegistro, String role,
            String[] authorities, boolean isAtivo, boolean isDesbloqueado) {
        this.id = id;
        this.userId = userId;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.usuario = usuario;
        this.senha = senha;
        this.email = email;
        this.imagemPerfilUrl = imagemPerfilUrl;
        this.registroLogin = registroLogin;
        this.mostrarRegistroLogin = mostrarRegistroLogin;
        this.dataRegistro = dataRegistro;
        this.role = role;
        this.authorities = authorities;
        this.isAtivo = isAtivo;
        this.isDesbloqueado = isDesbloqueado;
    }



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSobrenome() {
        return sobrenome;
    }
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getImagemPerfilUrl() {
        return imagemPerfilUrl;
    }
    public void setImagemPerfilUrl(String imagemPerfilUrl) {
        this.imagemPerfilUrl = imagemPerfilUrl;
    }
    public Date getRegistroLogin() {
        return registroLogin;
    }
    public void setRegistroLogin(Date registroLogin) {
        this.registroLogin = registroLogin;
    }
    public Date getMostrarRegistroLogin() {
        return mostrarRegistroLogin;
    }
    public void setMostrarRegistroLogin(Date mostrarRegistroLogin) {
        this.mostrarRegistroLogin = mostrarRegistroLogin;
    }
    public Date getDataRegistro() {
        return dataRegistro;
    }
    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String[] getAuthorities() {
        return authorities;
    }
    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }
    public boolean isAtivo() {
        return isAtivo;
    }
    public void setAtivo(boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public boolean isDesbloqueado() {
        return isDesbloqueado;
    }

    public void setDesbloqueado(boolean isDesbloqueado) {
        this.isDesbloqueado = isDesbloqueado;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
   

    

}
