package com.example.codechella.models.users;

import java.time.LocalDateTime;

public record SolicitacaoPermissaoDTO(
        Long id,
        Long idUsuario,
        String nomeUsuario,
        String emailUsuario,
        TipoPermissao tipoPermissao,
        StatusPermissao status,
        String motivo,
        String motivoNegacao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public SolicitacaoPermissao toEntity() {
        SolicitacaoPermissao solicitacao = new SolicitacaoPermissao(idUsuario, tipoPermissao);
        if (id != null) solicitacao.setId(id);
        solicitacao.setStatus(status);
        solicitacao.setMotivo(motivo);
        solicitacao.setMotivoNegacao(motivoNegacao);
        solicitacao.setCriadoEm(criadoEm);
        solicitacao.setAtualizadoEm(atualizadoEm);
        return solicitacao;
    }

    public static SolicitacaoPermissaoDTO fromEntity(SolicitacaoPermissao entity, String nomeUsuario, String emailUsuario) {
        return new SolicitacaoPermissaoDTO(
                entity.getId(),
                entity.getIdUsuario(),
                nomeUsuario,
                emailUsuario,
                entity.getTipoPermissao(),
                entity.getStatus(),
                entity.getMotivo(),
                entity.getMotivoNegacao(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}