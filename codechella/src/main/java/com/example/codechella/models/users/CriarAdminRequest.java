package com.example.codechella.models.users;

public record CriarAdminRequest(
        SuperAdmin superAdmin,
        UserAdmin admin
) {}