package com.example.codechella.serivce.usuarioAdmin;

import com.example.codechella.models.users.SuperAdmin;
import com.example.codechella.models.users.UserAdmin;
import com.example.codechella.models.users.UsuarioAdminDTO;
import com.example.codechella.repository.SuperAdminRepository;
import com.example.codechella.repository.UsuarioAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UsuarioAdminService {

    @Autowired
    UsuarioAdminRepository repository;

    @Autowired
    SuperAdminRepository superAdminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Mono<SuperAdmin> validarSuperPorId(Long superAdminId) {
        return superAdminRepository.findById(superAdminId)
                .filter(s -> s.getTipoUsuario() != null && s.getTipoUsuario().name().equals("SUPER"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Super admin inválido")));
    }

    public Mono<UsuarioAdminDTO> criarAdmin(Long superAdminId, UserAdmin admin) {
        return validarSuperPorId(superAdminId)
                .then(Mono.defer(() -> {
                    if (admin.getSenha() != null) {
                        admin.setSenha(passwordEncoder.encode(admin.getSenha()));
                    }
                    return repository.save(admin).map(UsuarioAdminDTO::toDTO);
                }));
    }

    public Flux<UsuarioAdminDTO> listarTodos() {
        return repository.findAll().map(UsuarioAdminDTO::toDTO);
    }

    public Mono<Void> removerAdmin(Long id, Long superAdminId) {
        return validarSuperPorId(superAdminId)
                .then(repository.findById(id)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin não encontrado")))
                        .flatMap(repository::delete));
    }

    public Flux<UsuarioAdminDTO> listarTodosAdmins() {
        return repository.findAll().map(UsuarioAdminDTO::toDTO);
    }
}
