package com.example.codechella.controller.superAdmin;

import com.example.codechella.models.users.UsuarioAdminDTO;
import com.example.codechella.models.users.UsuarioResponseDTO;
import com.example.codechella.serivce.superAdminService.SuperAdminService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/super-admin")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    @PostMapping("/criar/admin")
    public Mono<UsuarioAdminDTO> criarAdmin(
            @RequestBody UsuarioAdminDTO adminDTO,
            @RequestHeader("super-admin-id") Long superAdminId) {
        return superAdminService.criarAdmin(superAdminId, adminDTO);
    }

    @GetMapping(value = "/listar/admins", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UsuarioAdminDTO> listarAdmins() {
        return superAdminService.listarAdmins();
    }

    @DeleteMapping("/remover/admin/{id}")
    public Mono<Void> removerAdmin(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        return superAdminService.removerAdmin(id, superAdminId);
    }

    @GetMapping(value = "/listar/usuarios", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UsuarioResponseDTO> listarUsuarios() {
        return superAdminService.listarUsuarios();
    }

    @DeleteMapping("/remover/usuario/{id}")
    public Mono<Void> removerUsuario(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        return superAdminService.removerUsuario(id, superAdminId);
    }

    @DeleteMapping("/eventos/{id}")
    public Mono<Void> excluirEventoQualquer(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        return superAdminService.excluirEventoQualquer(id, superAdminId);
    }

    @PutMapping("/promover/admin/{id}")
    public Mono<UsuarioResponseDTO> promoverParaAdmin(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        return superAdminService.promoverParaAdmin(id, superAdminId);
    }

    @PutMapping("/rebaixar/user/{id}")
    public Mono<UsuarioResponseDTO> rebaixarParaUser(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        return superAdminService.rebaixarParaUser(id, superAdminId);
    }
}
