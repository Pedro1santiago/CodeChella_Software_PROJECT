package com.example.codechella.models.ingresso;

import java.time.LocalDate;

public record IngressoDTO(Long id, Long eventoId, LocalDate date, int quantidade, Double valor, String descricao, TipoStatus tipoStatus) {

    public static IngressoDTO toDTO(Ingresso ingresso){
        return new IngressoDTO(ingresso.getId(), ingresso.getEventoId(), ingresso.getDate(), ingresso.getQuantidade(), ingresso.getValor(), ingresso.getDescricao(), ingresso.getStatus());
    }

    public Ingresso toEntity(){
        Ingresso ingresso = new Ingresso();
        ingresso.setEventoId(this.eventoId);
        ingresso.setDate(this.date);
        ingresso.setQuantidade(this.quantidade);
        ingresso.setValor(this.valor);
        ingresso.setDescricao(this.descricao);
        ingresso.setStatus(this.tipoStatus);
        return ingresso;
    }


}
