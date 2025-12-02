package com.example.codechella.models.evento;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("eventos")
public class Evento {

    @Id
    private Long id;
    private TipoEvento tipo;
    private String nome;
    private LocalDate data;
    private String descricao;

    @Column("evento_status")
    private StatusEvento statusEvento;

    private Long idAdminCriador;
    private Integer numeroIngressosDisponiveis;

    public Long getId() {
        return id;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusEvento getStatusEvento(){return statusEvento;}

    public Long getIdAdminCriador() { return idAdminCriador; }

    public Integer getNumeroIngressosDisponiveis() { return numeroIngressosDisponiveis; }
 
    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatusEvento(StatusEvento statusEvento){this.statusEvento = statusEvento;}

    public void setIdAdminCriador(Long idAdminCriador) { this.idAdminCriador = idAdminCriador; }

    public void setNumeroIngressosDisponiveis(Integer numeroIngressosDisponiveis) { this.numeroIngressosDisponiveis = numeroIngressosDisponiveis; }
}
