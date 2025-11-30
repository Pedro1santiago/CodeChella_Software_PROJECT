package com.example.codechella.controller.usuario;

import com.example.codechella.models.users.UsuarioDTO;
import com.example.codechella.serivce.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuario")
public class Usuario {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public Mono<UsuarioDTO> cadastrar(@RequestBody UsuarioDTO usuario) {
        return usuarioService.cadastrar(usuario);
    }
}
