package com.example.codechella.serivce.permissao;

import com.example.codechella.models.users.SolicitacaoPermissao;
import com.example.codechella.models.users.SolicitacaoPermissaoDTO;
import com.example.codechella.models.users.StatusPermissao;
import com.example.codechella.models.users.SuperAdmin;
import com.example.codechella.models.users.TipoPermissao;
import com.example.codechella.models.users.TipoUsuario;
import com.example.codechella.models.users.Usuario;
import com.example.codechella.repository.SolicitacaoPermissaoRepository;
import com.example.codechella.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PermissaoService {

    @Autowired
    private SolicitacaoPermissaoRepository solicitacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Mono<Void> validarSuperAdmin(SuperAdmin superAdmin) {
        if (superAdmin == null || superAdmin.getTipoUsuario() != TipoUsuario.SUPER) {
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado. Super Admin requerido."));
        }
        return Mono.empty();
    }

    // Usuário Normal solicita permissão para virar Admin
    public Mono<SolicitacaoPermissaoDTO> solicitarPermissaoAdmin(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .flatMap(usuario -> {
                    if (usuario.getTipoUsuario() != TipoUsuario.USER) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Você já é um administrador"));
                    }
                    SolicitacaoPermissao solicitacao = new SolicitacaoPermissao(idUsuario, TipoPermissao.ADMIN);
                    return solicitacaoRepository.save(solicitacao);
                })
                .flatMap(solicitacao -> usuarioRepository.findById(idUsuario)
                        .map(usuario -> SolicitacaoPermissaoDTO.fromEntity(solicitacao, usuario.getNome())));
    }

    // Listar solicitações pendentes
    public Flux<SolicitacaoPermissaoDTO> listarSolicitacoesPendentes(SuperAdmin superAdmin) {
        return validarSuperAdmin(superAdmin)
                .thenMany(solicitacaoRepository.findByStatus(StatusPermissao.PENDENTE)
                        .flatMap(solicitacao -> usuarioRepository.findById(solicitacao.getIdUsuario())
                                .map(usuario -> SolicitacaoPermissaoDTO.fromEntity(solicitacao, usuario.getNome()))));
    }

    // Aprovar solicitação de permissão
    public Mono<SolicitacaoPermissaoDTO> aprovarSolicitacao(Long idSolicitacao, SuperAdmin superAdmin) {
        return validarSuperAdmin(superAdmin)
                .then(solicitacaoRepository.findById(idSolicitacao))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação não encontrada")))
                .flatMap(solicitacao -> {
                    if (solicitacao.getStatus() != StatusPermissao.PENDENTE) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solicitação já foi processada"));
                    }
                    solicitacao.setStatus(StatusPermissao.APROVADO);
                    return solicitacaoRepository.save(solicitacao)
                            .flatMap(solicitacaoAtualizada -> usuarioRepository.findById(solicitacaoAtualizada.getIdUsuario())
                                    .flatMap(usuario -> {
                                        usuario.setTipoUsuario(TipoUsuario.ADMIN);
                                        return usuarioRepository.save(usuario)
                                                .map(u -> SolicitacaoPermissaoDTO.fromEntity(solicitacaoAtualizada, u.getNome()));
                                    }));
                });
    }

    // Negar solicitação de permissão
    public Mono<SolicitacaoPermissaoDTO> negarSolicitacao(Long idSolicitacao, String motivo, SuperAdmin superAdmin) {
        return validarSuperAdmin(superAdmin)
                .then(solicitacaoRepository.findById(idSolicitacao))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitação não encontrada")))
                .flatMap(solicitacao -> {
                    if (solicitacao.getStatus() != StatusPermissao.PENDENTE) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solicitação já foi processada"));
                    }
                    solicitacao.setStatus(StatusPermissao.NEGADO);
                    solicitacao.setMotivoNegacao(motivo);
                    return solicitacaoRepository.save(solicitacao)
                            .flatMap(solicitacaoAtualizada -> usuarioRepository.findById(solicitacaoAtualizada.getIdUsuario())
                                    .map(usuario -> SolicitacaoPermissaoDTO.fromEntity(solicitacaoAtualizada, usuario.getNome())));
                });
    }

    // Listar solicitações de um usuário específico
    public Flux<SolicitacaoPermissaoDTO> minhasSolicitacoes(Long idUsuario) {
        return solicitacaoRepository.findByIdUsuario(idUsuario)
                .flatMap(solicitacao -> usuarioRepository.findById(idUsuario)
                        .map(usuario -> SolicitacaoPermissaoDTO.fromEntity(solicitacao, usuario.getNome())));
    }
}
