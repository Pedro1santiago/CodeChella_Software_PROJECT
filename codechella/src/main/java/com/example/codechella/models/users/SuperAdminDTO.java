package com.example.codechella.models.users;

public record SuperAdminDTO(
        Long id,
        String nome,
        String email,
        String senha,
        TipoUsuario tipoUsuario
) {

    public static SuperAdminDTO toDTO(SuperAdmin superAdmin) {
        return new SuperAdminDTO(
                superAdmin.getIdSuperAdmin(),
                superAdmin.getNome(),
                superAdmin.getEmail(),
                superAdmin.getSenha(),
                superAdmin.getTipoUsuario()
        );
    }

    public SuperAdmin toEntity() {
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setIdSuperAdmin(this.id);
        superAdmin.setNome(this.nome);
        superAdmin.setEmail(this.email);
        superAdmin.setSenha(this.senha);
        superAdmin.setTipoUsuario(this.tipoUsuario);
        return superAdmin;
    }
}
