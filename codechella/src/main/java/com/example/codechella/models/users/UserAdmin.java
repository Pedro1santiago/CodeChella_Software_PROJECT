package com.example.codechella.models.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("usuario_adm")
public class UserAdmin {

    @Id
    private Long idUsuario;

    private String nome;
    private String email;
    private String senha;
    private TipoUsuario tipoUsuario = TipoUsuario.ADMIN;

    public UserAdmin(){}

    public UserAdmin(Long idUsuario, String nome, String email, String senha, TipoUsuario tipoUsuario){
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public Long getIdUsuario() {return idUsuario;}
    public void setIdUsuario(Long idUsuario){this.idUsuario = idUsuario;}

    public String getNome() {return nome;}
    public void setNome(String nome){ this.nome = nome;}

    public String getEmail() {return email;}
    public void setEmail(String email){this.email = email;}

    public String getSenha() {return senha;}
    public void setSenha(String senha){this.senha = senha;}

    public TipoUsuario getTipoUsuario() {return tipoUsuario;}
    public void setTipoUsuario(TipoUsuario tipoUsuario){
        this.tipoUsuario = tipoUsuario;
    }
}
