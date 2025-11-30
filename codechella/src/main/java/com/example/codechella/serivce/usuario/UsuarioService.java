package com.example.codechella.serivce.usuario;

import com.example.codechella.controller.auth.LoginRequest;
import com.example.codechella.models.users.SuperAdmin;
import com.example.codechella.models.users.TipoUsuario;
import com.example.codechella.models.users.Usuario;
import com.example.codechella.models.users.UsuarioDTO;
import com.example.codechella.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Mono<UsuarioDTO> cadastrar(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioDTO.toEntity();
        usuario.setTipoUsuario(TipoUsuario.USER);
        return usuarioRepository
                .save(usuario)
                .map(UsuarioDTO::toDTO);
    }

    public Mono<UsuarioDTO> login(LoginRequest loginDTO) {
        return usuarioRepository.findByEmail(loginDTO.getEmail())
                .flatMap(usuario -> {
                    if (usuario.getSenha().equals(loginDTO.getSenha())) {
                        return Mono.just(UsuarioDTO.toDTO(usuario));
                    } else {
                        return Mono.error(new RuntimeException("Senha incorreta"));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Usuário não encontrado")));
    }

    public Mono<Void> removerUsuario(Long id, SuperAdmin superAdmin) {
        if (superAdmin == null || superAdmin.getTipoUsuario() != TipoUsuario.SUPER) {
            return Mono.error(new RuntimeException("Acesso negado: somente SuperAdmin pode remover usuários"));
        }

        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuário não encontrado")))
                .flatMap(usuario -> usuarioRepository.delete(usuario));
    }

    public Flux<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll()
                .map(UsuarioDTO::toDTO);
    }
}
