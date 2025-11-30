package com.example.codechella.controller.superAdmin;

import com.example.codechella.models.users.SuperAdmin;
import com.example.codechella.models.users.UsuarioAdminDTO;
import com.example.codechella.models.users.UsuarioDTO;
import com.example.codechella.models.users.CriarAdminRequest;
import com.example.codechella.serivce.superAdminService.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/super-admin")
public class SuperAdminController {

    @Autowired
    private SuperAdminService superAdminService;

    // Criar novo Admin
    @PostMapping("/criar/admin")
    public Mono<UsuarioAdminDTO> criarAdmin(
            @RequestBody UsuarioAdminDTO adminDTO,
            @RequestHeader("super-admin-id") Long superAdminId) {
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setIdSuperAdmin(superAdminId);
        return superAdminService.criarAdmin(superAdmin, adminDTO);
    }

    // Listar todos os Admins
    @GetMapping(value = "/listar/admins", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UsuarioAdminDTO> listarAdmins() {
        return superAdminService.listarAdmins();
    }

    // Remover Admin
    @DeleteMapping("/remover/admin/{id}")
    public Mono<Void> removerAdmin(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setIdSuperAdmin(superAdminId);
        return superAdminService.removerAdmin(id, superAdmin);
    }

    // Listar todos os Usuários Normais
    @GetMapping(value = "/listar/usuarios", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UsuarioDTO> listarUsuarios() {
        return superAdminService.listarUsuarios();
    }

    // Remover Usuário Normal
    @DeleteMapping("/remover/usuario/{id}")
    public Mono<Void> removerUsuario(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setIdSuperAdmin(superAdminId);
        return superAdminService.removerUsuario(id, superAdmin);
    }

    // Super Admin exclui qualquer evento
    @DeleteMapping("/eventos/{id}")
    public Mono<Void> excluirEventoQualquer(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setIdSuperAdmin(superAdminId);
        return superAdminService.excluirEventoQualquer(id, superAdmin);
    }

    // Promover usuário para Admin
    @PutMapping("/promover/admin/{id}")
    public Mono<UsuarioDTO> promoverParaAdmin(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setIdSuperAdmin(superAdminId);
        return superAdminService.promoverParaAdmin(id, superAdmin);
    }

    // Rebaixar Admin para User
    @PutMapping("/rebaixar/user/{id}")
    public Mono<UsuarioDTO> rebaixarParaUser(
            @PathVariable Long id,
            @RequestHeader("super-admin-id") Long superAdminId) {
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setIdSuperAdmin(superAdminId);
        return superAdminService.rebaixarParaUser(id, superAdmin);
    }
}

