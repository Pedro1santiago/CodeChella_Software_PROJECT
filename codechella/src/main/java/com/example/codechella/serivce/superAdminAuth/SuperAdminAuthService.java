package com.example.codechella.serivce.superAdminAuth;

import com.example.codechella.models.users.SuperAdminDTO;
import com.example.codechella.models.users.SuperAdminLoginRequest;
import com.example.codechella.repository.SuperAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class SuperAdminAuthService {

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Mono<SuperAdminDTO> login(SuperAdminLoginRequest loginRequest) {
        return superAdminRepository.findByEmail(loginRequest.email())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Super Admin não encontrado")))
                .flatMap(superAdmin -> {
                    if (!passwordEncoder.matches(loginRequest.senha(), superAdmin.getSenha())) {
                        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha incorreta"));
                    }
                    return Mono.just(SuperAdminDTO.toDTO(superAdmin));
                });
    }
}
