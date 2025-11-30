package com.example.codechella.controller.auth;


import com.example.codechella.models.users.SuperAdminDTO;
import com.example.codechella.models.users.UsuarioDTO;
import com.example.codechella.serivce.superAdminAuth.SuperAdminAuthService;
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
    private final SuperAdminAuthService superAdminAuthService;

    public AuthController(UsuarioService usuarioService, SuperAdminAuthService superAdminAuthService) {
        this.usuarioService = usuarioService;
        this.superAdminAuthService = superAdminAuthService;
    }

    @PostMapping("/usuario/register")
    public Mono<UsuarioDTO> registerUsuario(@RequestBody UsuarioDTO usuario) {
        return usuarioService.cadastrar(usuario);
    }

    @PostMapping("/usuario/login")
    public Mono<UsuarioDTO> loginUsuario(@RequestBody LoginRequest login) {
        return usuarioService.login(login);
    }

    @PostMapping("/super-admin/login")
    public Mono<SuperAdminDTO> loginSuperAdmin(@RequestBody SuperAdminLoginRequest login) {
        return superAdminAuthService.login(login);
    }
}
