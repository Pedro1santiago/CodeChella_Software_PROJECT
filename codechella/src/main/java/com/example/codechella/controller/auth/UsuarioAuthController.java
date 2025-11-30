package com.example.codechella.controller.auth;

import com.example.codechella.models.users.Usuario;
import com.example.codechella.models.users.UsuarioDTO;
import com.example.codechella.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth/usuario")
public class UsuarioAuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Registrar novo usuário
    @PostMapping("/registrar")
    public Mono<UsuarioDTO> registrar(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioDTO.toEntity();

        return usuarioRepository.findByEmail(usuario.getEmail())
                .flatMap(existing -> Mono.<Usuario>error(
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado")))
                .switchIfEmpty(Mono.defer(() -> usuarioRepository.save(usuario)))
                .map(saved -> {
                    Usuario u = (Usuario) saved;
                    return new UsuarioDTO(
                            u.getId(),
                            u.getNome(),
                            u.getEmail(),
                            u.getSenha(),
                            u.getTipoUsuario(),
                            u.getCriadoEm()
                    );
                });
    }



    // Login de usuário
    @PostMapping("/login")
    public Mono<UsuarioDTO> login(@RequestParam String email, @RequestParam String senha) {
        return usuarioRepository.findByEmail(email)
                .filter(usuario -> usuario.getSenha().equals(senha))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou senha inválidos")))
                .map(UsuarioDTO::toDTO);
    }

    // Obter dados do usuário
    @GetMapping("/{id}")
    public Mono<UsuarioDTO> obterUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .map(UsuarioDTO::toDTO);
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public Mono<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .flatMap(usuario -> {
                    if (usuarioDTO.nome() != null) usuario.setNome(usuarioDTO.nome());
                    if (usuarioDTO.email() != null && !usuarioDTO.email().equals(usuario.getEmail())) {
                        return usuarioRepository.findByEmail(usuarioDTO.email())
                                .flatMap(existing -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado")))
                                .then(Mono.defer(() -> {
                                    usuario.setEmail(usuarioDTO.email());
                                    return usuarioRepository.save(usuario);
                                }));
                    }
                    if (usuarioDTO.senha() != null) usuario.setSenha(usuarioDTO.senha());
                    return usuarioRepository.save(usuario);
                })
                .map(UsuarioDTO::toDTO);
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public Mono<Void> deletarUsuario(@PathVariable Long id) {
        return usuarioRepository.deleteById(id);
    }
}
