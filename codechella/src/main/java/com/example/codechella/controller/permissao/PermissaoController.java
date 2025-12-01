package com.example.codechella.controller.permissao;

import com.example.codechella.models.users.SolicitacaoPermissaoDTO;
import com.example.codechella.models.users.SuperAdmin;
import com.example.codechella.serivce.permissao.PermissaoService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {

    private final PermissaoService permissaoService;

    public PermissaoController(PermissaoService permissaoService) {
        this.permissaoService = permissaoService;
    }

    // Usuário normal solicita permissão para virar admin
    @PostMapping("/solicitar")
    public Mono<SolicitacaoPermissaoDTO> solicitarPermissaoAdmin(@RequestHeader("usuario-id") Long idUsuario) {
        return permissaoService.solicitarPermissaoAdmin(idUsuario);
    }

    // Listar minhas solicitações
    @GetMapping("/minhas-solicitacoes")
    public Flux<SolicitacaoPermissaoDTO> minhasSolicitacoes(@RequestHeader("usuario-id") Long idUsuario) {
        return permissaoService.minhasSolicitacoes(idUsuario);
    }

    // Super Admin lista solicitações pendentes
    @GetMapping(value = "/pendentes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SolicitacaoPermissaoDTO> listarSolicitacoesPendentes(
            @RequestHeader("super-admin-id") Long superAdminId) {
        return permissaoService.listarSolicitacoesPendentes(superAdminId);
    }

    // Super Admin aprova solicitação
    @PutMapping("/{id}/aprovar")
    public Mono<SolicitacaoPermissaoDTO> aprovarSolicitacao(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        return permissaoService.aprovarSolicitacao(id, superAdminId);
    }

    // Super Admin nega solicitação
    @PutMapping("/{id}/negar")
    public Mono<SolicitacaoPermissaoDTO> negarSolicitacao(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId,
            @RequestParam(required = false, defaultValue = "") String motivo) {
        return permissaoService.negarSolicitacao(id, motivo, superAdminId);
    }
}
