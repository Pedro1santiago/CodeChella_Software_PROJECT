package com.example.codechella.controller.auth;


import com.example.codechella.models.users.UsuarioDTO;
import com.example.codechella.serivce.usuario.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public Mono<UsuarioDTO> register(@RequestBody UsuarioDTO usuario) {
        return usuarioService.cadastrar(usuario);
    }

    @PostMapping("/login")
    public Mono<UsuarioDTO> login(@RequestBody LoginRequest login) {
        return usuarioService.login(login);
    }
}
