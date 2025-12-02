package com.example.codechella.models.ingresso;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("ingressos")
public class Ingresso {

    @Id
    private Long id;

    private Long eventoId;
    private LocalDate date;
    private int quantidade;
    private Long quantidadeTotal;
    private Double valor;
    private String descricao;
    private TipoStatus status;

    public Long getId(){ return id;}

    public Long getEventoId(){return eventoId;}
    public void setEventoId(Long eventoId){this.eventoId = eventoId;}

    public LocalDate getDate(){return date;}
    public void setDate(LocalDate date){this.date = date;}

    public int getQuantidade(){return quantidade;}
    public void setQuantidade(int quantidade){this.quantidade = quantidade;}

    public Long getQuantidadeTotal(){return quantidadeTotal;}
    public void setQuantidadeTotal(Long quantidadeTotal){this.quantidadeTotal = quantidadeTotal;}

    public Double getValor(){return valor;}
    public void setValor(Double valor){this.valor = valor;}

    public String getDescricao(){return  descricao;}
    public void setDescricao(String descricao){this.descricao = descricao;}

    public TipoStatus getStatus(){return status;}
    public void setStatus(TipoStatus status){this.status = status;}

}
