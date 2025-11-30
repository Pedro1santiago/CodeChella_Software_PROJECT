package com.example.codechella.serivce.superAdminAuth;

import com.example.codechella.controller.auth.SuperAdminLoginRequest;
import com.example.codechella.models.users.SuperAdmin;
import com.example.codechella.models.users.SuperAdminDTO;
import com.example.codechella.repository.SuperAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class SuperAdminAuthService {

    @Autowired
    private SuperAdminRepository superAdminRepository;

    public Mono<SuperAdminDTO> login(SuperAdminLoginRequest loginRequest) {
        return superAdminRepository.findByEmail(loginRequest.getEmail())
                .flatMap(superAdmin -> {
                    if (superAdmin.getSenha().equals(loginRequest.getSenha())) {
                        return Mono.just(SuperAdminDTO.toDTO(superAdmin));
                    } else {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha incorreta"));
                    }
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Super Admin não encontrado")));
    }
}
