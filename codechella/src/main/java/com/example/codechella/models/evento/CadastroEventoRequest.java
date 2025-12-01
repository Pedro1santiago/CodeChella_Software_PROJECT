package com.example.codechella.models.evento;

import com.example.codechella.models.users.UserAdmin;

public record CadastroEventoRequest(UserAdmin userAdmin, EventoDTO eventoDTO) {
}
