package com.example.codechella.models.users;

import java.time.LocalDateTime;

public record UsuarioDTO(Long id, String nome, String email, String senha, TipoUsuario tipoUsuario, LocalDateTime criadoEm) {

    public static UsuarioDTO toDTO(Usuario usuario){
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getTipoUsuario(), usuario.getCriadoEm());
    }

    public Usuario toEntity(){
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        usuario.setTipoUsuario(this.tipoUsuario);
        usuario.setCriadoEm(this.criadoEm);
        return usuario;
    }
}


