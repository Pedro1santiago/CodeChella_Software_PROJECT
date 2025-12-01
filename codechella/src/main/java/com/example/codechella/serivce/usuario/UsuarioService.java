package com.example.codechella.serivce.usuario;

import com.example.codechella.models.users.LoginRequest;
import com.example.codechella.models.users.TipoUsuario;
import com.example.codechella.models.users.Usuario;
import com.example.codechella.models.users.UsuarioMapper;
import com.example.codechella.models.users.UsuarioRegisterRequest;
import com.example.codechella.models.users.UsuarioResponseDTO;
import com.example.codechella.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Mono<UsuarioResponseDTO> cadastrar(UsuarioRegisterRequest req) {
        log.info("[CADASTRO] Recebendo request: {}", req);

        if (!req.senha().equals(req.confirmarSenha())) {
            log.warn("[CADASTRO] Senhas não coincidem para email: {}", req.email());
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "As senhas não coincidem"));
        }

        return usuarioRepository.findByEmail(req.email())
                .flatMap(u -> {
                    log.warn("[CADASTRO] Email já cadastrado: {}", req.email());
                    return Mono.<UsuarioResponseDTO>error(
                            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado")
                    );
                })
                .switchIfEmpty(Mono.defer(() -> {
                    Usuario usuario = UsuarioMapper.toEntity(req);
                    usuario.setTipoUsuario(TipoUsuario.USER);
                    usuario.setSenha(passwordEncoder.encode(req.senha()));
                    usuario.setCriadoEm(LocalDateTime.now());

                    log.info("[CADASTRO] Criando usuário: {}", usuario.getEmail());

                    return usuarioRepository.save(usuario)
                            .doOnSuccess(u -> log.info("[CADASTRO] Usuário cadastrado com sucesso: {}", u.getEmail()))
                            .doOnError(err -> log.error("[CADASTRO] Erro ao salvar usuário", err))
                            .map(UsuarioMapper::toDTO);
                }));
    }

    public Mono<UsuarioResponseDTO> login(LoginRequest login) {
        return usuarioRepository.findByEmail(login.email())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .flatMap(usuario -> {
                    if (!passwordEncoder.matches(login.senha(), usuario.getSenha())) {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha incorreta"));
                    }
                    return Mono.just(UsuarioMapper.toDTO(usuario));
                });
    }

    public Mono<Void> removerUsuario(Long id, Long superAdminId, com.example.codechella.repository.SuperAdminRepository superAdminRepository) {
        // validar super admin por id (alternativa: injetar SuperAdminRepository no serviço)
        return superAdminRepository.findById(superAdminId)
                .filter(s -> s.getTipoUsuario() == TipoUsuario.SUPER)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN,"Acesso negado")))
                .then(usuarioRepository.findById(id)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                        .flatMap(usuarioRepository::delete));
    }

    public Flux<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .map(UsuarioMapper::toDTO);
    }
}
