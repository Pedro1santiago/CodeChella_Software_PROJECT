package com.example.codechella.repository;

import com.example.codechella.models.users.SuperAdmin;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SuperAdminRepository extends ReactiveCrudRepository<SuperAdmin, Long> {
    Mono<SuperAdmin> findByEmail(String email);
}
