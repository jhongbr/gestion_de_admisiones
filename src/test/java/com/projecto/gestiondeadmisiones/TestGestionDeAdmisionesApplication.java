package com.projecto.gestiondeadmisiones;

import org.springframework.boot.SpringApplication;

public class TestGestionDeAdmisionesApplication {

    public static void main(String[] args) {
        SpringApplication.from(GestionDeAdmisionesApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
