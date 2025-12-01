package com.example.codechella.controller.auth;

import com.example.codechella.models.users.Usuario;
import com.example.codechella.models.users.UsuarioMapper;
import com.example.codechella.models.users.UsuarioRegisterRequest;
import com.example.codechella.models.users.UsuarioResponseDTO;
import com.example.codechella.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/usuario")
public class UsuarioAuthController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioAuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/registrar")
    public Mono<UsuarioResponseDTO> registrar(@RequestBody UsuarioRegisterRequest req) {
        if (!req.senha().equals(req.confirmarSenha())) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "As senhas não coincidem"));
        }

        return usuarioRepository.findByEmail(req.email())
                .flatMap(existing -> Mono.<Usuario>error(
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado")
                ))
                .switchIfEmpty(Mono.defer(() -> {
                    Usuario novo = UsuarioMapper.toEntity(req);
                    // senha será codificada no UsuarioService; se você preferir codificar aqui, injete PasswordEncoder
                    return usuarioRepository.save(novo);
                }))
                .map(UsuarioMapper::toDTO);
    }

    @GetMapping("/{id}")
    public Mono<UsuarioResponseDTO> obterUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .map(UsuarioMapper::toDTO);
    }

    @PutMapping("/{id}")
    public Mono<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioRegisterRequest req
    ) {
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"
                )))
                .flatMap(usuario -> {

                    if (req.nome() != null) usuario.setNome(req.nome());

                    if (req.email() != null && !req.email().equals(usuario.getEmail())) {
                        return usuarioRepository.findByEmail(req.email())
                                .flatMap(existing -> Mono.error(new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST, "Email já cadastrado"
                                )))
                                .then(Mono.defer(() -> {
                                    usuario.setEmail(req.email());
                                    return usuarioRepository.save(usuario);
                                }));
                    }

                    if (req.senha() != null) {
                        if (!req.senha().equals(req.confirmarSenha())) {
                            return Mono.error(new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST, "As senhas não coincidem"
                            ));
                        }
                        usuario.setSenha(req.senha()); // ideal: hash aqui
                    }

                    return usuarioRepository.save(usuario);
                })
                .map(UsuarioMapper::toDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deletarUsuario(@PathVariable Long id) {
        return usuarioRepository.deleteById(id);
    }
}
