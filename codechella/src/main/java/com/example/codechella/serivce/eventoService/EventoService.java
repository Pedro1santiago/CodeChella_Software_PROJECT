package com.example.codechella.serivce.eventoService;

import com.example.codechella.models.evento.EventoDTO;
import com.example.codechella.models.evento.StatusEvento;
import com.example.codechella.models.evento.TipoEvento;
import com.example.codechella.models.users.TipoUsuario;
import com.example.codechella.models.users.UserAdmin;
import com.example.codechella.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EventoService {

    @Autowired
    private EventoRepository repository;

    private void verificaUser(UserAdmin userAdmin){
        if (userAdmin.getTipoUsuario() != TipoUsuario.ADMINISTRADOR){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Apenas administradores podem fazer essa alteração");
        }
    }

    public Flux<EventoDTO> listarTodos(){
        return repository.findAll().map(EventoDTO::toDto);
    }

    public Mono<EventoDTO> buscarPorId(Long id){
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(EventoDTO::toDto);
    }

    public Mono<EventoDTO> cadastrarEvento(UserAdmin userAdmin, EventoDTO dto) {
        verificaUser(userAdmin);
        var evento = dto.toEntity();
        evento.setStatusEvento(StatusEvento.ABERTO);

        return repository.save(evento).map(EventoDTO::toDto);
    }

    public Mono<EventoDTO> cancelarEvento(Long id, UserAdmin userAdmin) {
        verificaUser(userAdmin);
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(evento -> {
                    evento.setStatusEvento(StatusEvento.FECHADO);
                    return repository.save(evento);
                })
                .map(EventoDTO::toDto);
    }

    public Mono<EventoDTO> atualizarId(Long id, EventoDTO dto, UserAdmin userAdmin){
        verificaUser(userAdmin);
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(evento -> {
                    evento.setTipo(dto.tipo());
                    evento.setNome(dto.nome());
                    evento.setData(dto.data());
                    evento.setDescricao(dto.descricao());
                    return repository.save(evento);
                })
                .map(EventoDTO::toDto);
    }

    public Flux<EventoDTO> obterPorTipo(String tipo) {
        TipoEvento tipoEvento = TipoEvento.valueOf(tipo.toUpperCase());
        return repository.findByTipo(tipoEvento)
                .map(EventoDTO::toDto);
    }

    public Mono<Void> excluir(Long id, UserAdmin userAdmin) {
        verificaUser(userAdmin);

        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(evento -> repository.delete(evento));
    }
}
