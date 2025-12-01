package com.example.codechella.controller.usuario;

import com.example.codechella.models.users.UsuarioRegisterRequest;
import com.example.codechella.models.users.UsuarioResponseDTO;
import com.example.codechella.serivce.usuario.UsuarioService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public Mono<UsuarioResponseDTO> cadastrar(@RequestBody UsuarioRegisterRequest request) {
        return usuarioService.cadastrar(request);
    }
}
