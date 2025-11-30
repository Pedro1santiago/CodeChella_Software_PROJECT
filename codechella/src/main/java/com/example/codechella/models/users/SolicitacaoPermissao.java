package com.example.codechella.models.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Table("solicitacao_permissao")
public class SolicitacaoPermissao {

    @Id
    private Long id;
    private Long idUsuario;
    private TipoPermissao tipoPermissao;
    private StatusPermissao status;
    private String motivoNegacao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public SolicitacaoPermissao() {}

    public SolicitacaoPermissao(Long idUsuario, TipoPermissao tipoPermissao) {
        this.idUsuario = idUsuario;
        this.tipoPermissao = tipoPermissao;
        this.status = StatusPermissao.PENDENTE;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public TipoPermissao getTipoPermissao() { return tipoPermissao; }
    public void setTipoPermissao(TipoPermissao tipoPermissao) { this.tipoPermissao = tipoPermissao; }

    public StatusPermissao getStatus() { return status; }
    public void setStatus(StatusPermissao status) { this.status = status; }

    public String getMotivoNegacao() { return motivoNegacao; }
    public void setMotivoNegacao(String motivoNegacao) { this.motivoNegacao = motivoNegacao; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
