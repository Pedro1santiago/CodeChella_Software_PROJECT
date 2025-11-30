package com.example.codechella.repository;

import com.example.codechella.models.users.SolicitacaoPermissao;
import com.example.codechella.models.users.StatusPermissao;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SolicitacaoPermissaoRepository extends R2dbcRepository<SolicitacaoPermissao, Long> {
    Flux<SolicitacaoPermissao> findByStatus(StatusPermissao status);
    Flux<SolicitacaoPermissao> findByIdUsuario(Long idUsuario);
    Mono<SolicitacaoPermissao> findByIdUsuarioAndStatus(Long idUsuario, StatusPermissao status);
}
