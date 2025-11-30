package com.example.codechella.serivce.usuarioAdmin;

import com.example.codechella.models.users.SuperAdmin;
import com.example.codechella.models.users.UserAdmin;
import com.example.codechella.models.users.UsuarioAdminDTO;
import com.example.codechella.repository.UsuarioAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UsuarioAdminService {

    @Autowired
    UsuarioAdminRepository repository;

    public Mono<UsuarioAdminDTO> criarAdmin(SuperAdmin superAdmin, UserAdmin admin) {
        if (!superAdmin.isValid()) {
            return Mono.error(new RuntimeException("Super admin inválido"));
        }
        return repository.save(admin).map(UsuarioAdminDTO::toDTO);
    }

    public Flux<UsuarioAdminDTO> listarTodosAdmins() {
        return repository.findAll().map(UsuarioAdminDTO::toDTO);
    }

    public Mono<Void> removerAdmin(Long id, SuperAdmin superAdmin) {
        if (!superAdmin.isValid()) {
            return Mono.error(new RuntimeException("Super admin inválido"));
        }
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Admin não encontrado")))
                .flatMap(repository::delete);
    }

    public Flux<UsuarioAdminDTO> listarTodos() {
        return repository.findAll().map(UsuarioAdminDTO::toDTO);
    }

}
