package com.example.codechella.serivce.superAdminService;

import com.example.codechella.models.users.SuperAdmin;
import com.example.codechella.models.users.TipoUsuario;
import com.example.codechella.models.users.UsuarioAdminDTO;
import com.example.codechella.models.users.UsuarioResponseDTO;
import com.example.codechella.models.users.UsuarioMapper;
import com.example.codechella.repository.EventoRepository;
import com.example.codechella.repository.SuperAdminRepository;
import com.example.codechella.repository.UsuarioAdminRepository;
import com.example.codechella.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SuperAdminService {

    @Autowired
    SuperAdminRepository superAdminRepository;

    @Autowired
    UsuarioAdminRepository usuarioAdminRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Mono<SuperAdmin> validarSuperPorId(Long superAdminId) {
        return superAdminRepository.findById(superAdminId)
                .filter(s -> s.getTipoUsuario() == TipoUsuario.SUPER)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado")));
    }

    public Mono<UsuarioAdminDTO> criarAdmin(Long superAdminId, UsuarioAdminDTO adminDTO) {
        return validarSuperPorId(superAdminId)
                .then(Mono.defer(() -> {
                    var entity = adminDTO.toEntity();
                    if (entity.getSenha() != null) {
                        entity.setSenha(passwordEncoder.encode(entity.getSenha()));
                    }
                    return usuarioAdminRepository.save(entity).map(UsuarioAdminDTO::toDTO);
                }));
    }

    public Flux<UsuarioAdminDTO> listarAdmins() {
        return usuarioAdminRepository.findAll().map(UsuarioAdminDTO::toDTO);
    }

    public Mono<Void> removerAdmin(Long id, Long superAdminId) {
        return validarSuperPorId(superAdminId)
                .then(usuarioAdminRepository.deleteById(id));
    }

    public Flux<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().map(UsuarioMapper::toDTO);
    }

    public Mono<Void> removerUsuario(Long id, Long superAdminId) {
        return validarSuperPorId(superAdminId)
                .then(usuarioRepository.deleteById(id));
    }

    public Mono<Void> excluirEventoQualquer(Long idEvento, Long superAdminId) {
        return validarSuperPorId(superAdminId)
                .then(eventoRepository.findById(idEvento))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado")))
                .flatMap(eventoRepository::delete);
    }

    public Mono<UsuarioResponseDTO> promoverParaAdmin(Long idUsuario, Long superAdminId) {
        return validarSuperPorId(superAdminId)
                .then(usuarioRepository.findById(idUsuario))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .flatMap(usuario -> {
                    if (usuario.getTipoUsuario() != TipoUsuario.USER) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já é administrador"));
                    }
                    usuario.setTipoUsuario(TipoUsuario.ADMIN);
                    return usuarioRepository.save(usuario);
                })
                .map(UsuarioMapper::toDTO);
    }

    public Mono<UsuarioResponseDTO> rebaixarParaUser(Long idUsuario, Long superAdminId) {
        return validarSuperPorId(superAdminId)
                .then(usuarioRepository.findById(idUsuario))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .flatMap(usuario -> {
                    if (usuario.getTipoUsuario() != TipoUsuario.ADMIN) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não é um administrador"));
                    }
                    usuario.setTipoUsuario(TipoUsuario.USER);
                    return usuarioRepository.save(usuario);
                })
                .map(UsuarioMapper::toDTO);
    }
}
