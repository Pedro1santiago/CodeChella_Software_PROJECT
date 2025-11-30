package com.example.codechella.serivce.superAdminService;

import com.example.codechella.models.users.SuperAdmin;
import com.example.codechella.models.users.TipoUsuario;
import com.example.codechella.models.users.UserAdmin;
import com.example.codechella.models.users.Usuario;
import com.example.codechella.models.users.UsuarioAdminDTO;
import com.example.codechella.models.users.UsuarioDTO;
import com.example.codechella.repository.EventoRepository;
import com.example.codechella.repository.SuperAdminRepository;
import com.example.codechella.repository.UsuarioAdminRepository;
import com.example.codechella.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SuperAdminService {

    @Autowired
    SuperAdminRepository superAdminRepository;

    @Autowired
    UsuarioAdminRepository usuarioAdminRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EventoRepository eventoRepository;

    private Mono<SuperAdmin> validarSuper(SuperAdmin superAdmin) {
        return superAdminRepository.findByEmail(superAdmin.getEmail())
                .filter(s -> s.getSenha().equals(superAdmin.getSenha()))
                .filter(s -> s.getTipoUsuario() == TipoUsuario.SUPER)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado")));
    }

    // Criar novo Admin
    public Mono<UsuarioAdminDTO> criarAdmin(SuperAdmin superAdmin, UsuarioAdminDTO adminDTO) {
        return validarSuper(superAdmin)
                .then(usuarioAdminRepository.save(adminDTO.toEntity()))
                .map(UsuarioAdminDTO::toDTO);
    }

    // Listar todos os Admins
    public Flux<UsuarioAdminDTO> listarAdmins() {
        return usuarioAdminRepository.findAll().map(UsuarioAdminDTO::toDTO);
    }

    // Remover Admin
    public Mono<Void> removerAdmin(Long id, SuperAdmin superAdmin) {
        return validarSuper(superAdmin)
                .then(usuarioAdminRepository.deleteById(id));
    }

    // Listar todos os Usuários Normais
    public Flux<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().map(UsuarioDTO::toDTO);
    }

    // Remover Usuário Normal
    public Mono<Void> removerUsuario(Long id, SuperAdmin superAdmin) {
        return validarSuper(superAdmin)
                .then(usuarioRepository.deleteById(id));
    }

    // Super Admin pode excluir qualquer evento
    public Mono<Void> excluirEventoQualquer(Long idEvento, SuperAdmin superAdmin) {
        return validarSuper(superAdmin)
                .then(eventoRepository.findById(idEvento))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado")))
                .flatMap(evento -> eventoRepository.delete(evento));
    }

    // Promover usuário para Admin
    public Mono<UsuarioDTO> promoverParaAdmin(Long idUsuario, SuperAdmin superAdmin) {
        return validarSuper(superAdmin)
                .then(usuarioRepository.findById(idUsuario))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .flatMap(usuario -> {
                    if (usuario.getTipoUsuario() != TipoUsuario.USER) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já é administrador"));
                    }
                    usuario.setTipoUsuario(TipoUsuario.ADMIN);
                    return usuarioRepository.save(usuario);
                })
                .map(UsuarioDTO::toDTO);
    }

    // Rebaixar Admin para User
    public Mono<UsuarioDTO> rebaixarParaUser(Long idUsuario, SuperAdmin superAdmin) {
        return validarSuper(superAdmin)
                .then(usuarioRepository.findById(idUsuario))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")))
                .flatMap(usuario -> {
                    if (usuario.getTipoUsuario() != TipoUsuario.ADMIN) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não é um administrador"));
                    }
                    usuario.setTipoUsuario(TipoUsuario.USER);
                    return usuarioRepository.save(usuario);
                })
                .map(UsuarioDTO::toDTO);
    }
}
