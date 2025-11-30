package com.example.codechella.controller.usuarioAdmin;

import com.example.codechella.models.evento.EventoDTO;
import com.example.codechella.models.ingresso.IngressoDTO;
import com.example.codechella.models.users.CadastroEventoRequest;
import com.example.codechella.models.users.UserAdmin;
import com.example.codechella.models.users.UsuarioAdminDTO;
import com.example.codechella.serivce.eventoService.EventoService;
import com.example.codechella.serivce.ingressoService.IngressoService;
import com.example.codechella.serivce.usuarioAdmin.UsuarioAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuario/admin")
public class UsuarioAdminController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private UsuarioAdminService usuarioAdminService;

    @Autowired
    private IngressoService ingressoService;

    @PostMapping("/cadastrar/evento")
    public Mono<EventoDTO> cadastrar(@RequestBody CadastroEventoRequest request) {
        return eventoService.cadastrarEvento(request.userAdmin(), request.eventoDTO());
    }

    @GetMapping("/eventos")
    public Flux<EventoDTO> listarEventos() {
        return eventoService.listarTodos();
    }

    @GetMapping("/usuarios")
    public Flux<UsuarioAdminDTO> listarUsuarios() {
        return usuarioAdminService.listarTodos();
    }

    @GetMapping("/ingressos")
    public Flux<IngressoDTO> listarIngressos() {
        return ingressoService.listarTodos();
    }

    @DeleteMapping("/eventos/{id}")
    public Mono<Void> excluirEvento(@PathVariable Long id, @RequestBody(required = false) UserAdmin userAdmin) {
        return eventoService.excluir(id, userAdmin);
    }

    @PutMapping("/ingressos/cancelar/{id}")
    public Mono<IngressoDTO> cancelarIngresso(@PathVariable Long id) {
        return ingressoService.cancelarIngresso(id);
    }
}
