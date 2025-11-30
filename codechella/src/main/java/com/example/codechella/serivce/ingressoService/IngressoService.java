package com.example.codechella.serivce.ingressoService;

import com.example.codechella.models.ingresso.IngressoDTO;
import com.example.codechella.models.ingresso.TipoStatus;
import com.example.codechella.models.users.TipoUsuario;
import com.example.codechella.repository.IngressoRepository;
import com.example.codechella.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class IngressoService {

    @Autowired
    private IngressoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Mono<IngressoDTO> cadastrarIngresso(IngressoDTO dto) {
        var ingresso = dto.toEntity();
        ingresso.setStatus(TipoStatus.DISPONIVEL);
        return repository.save(ingresso).map(IngressoDTO::toDTO);
    }

    public Flux<IngressoDTO> listarTodos() {
        return repository.findAll().map(IngressoDTO::toDTO);
    }

    // Apenas usuários normais podem comprar ingressos
    public Flux<IngressoDTO> venderIngresso(Long eventoId, int quantidade, Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .flatMapMany(usuario -> {
                    if (usuario.getTipoUsuario() != TipoUsuario.USER) {
                        return Flux.error(new ResponseStatusException(HttpStatus.FORBIDDEN, 
                                "Apenas usuários normais podem comprar ingressos"));
                    }

                    return repository.findByEventoIdAndStatus(eventoId, TipoStatus.DISPONIVEL)
                            .collectList()
                            .flatMapMany(lista -> {
                                if (lista.size() < quantidade) {
                                    return Flux.error(new IllegalArgumentException("Ingressos insuficientes disponíveis."));
                                }

                                var ingressosParaVender = lista.stream()
                                        .limit(quantidade)
                                        .toList();

                                return Flux.fromIterable(ingressosParaVender)
                                        .flatMap(ingresso -> {
                                            ingresso.setStatus(TipoStatus.VENDIDO);
                                            return repository.save(ingresso);
                                        })
                                        .map(IngressoDTO::toDTO);
                            });
                });
    }

    public Mono<IngressoDTO> cancelarIngresso(Long ingressoId){
        return repository.findById(ingressoId).flatMap(ingresso ->{
            ingresso.setStatus(TipoStatus.DISPONIVEL);
            ingresso.setQuantidadeTotal(ingresso.getQuantidadeTotal()+1);
            return repository.save(ingresso);
        }).map(IngressoDTO::toDTO);
    }
}
