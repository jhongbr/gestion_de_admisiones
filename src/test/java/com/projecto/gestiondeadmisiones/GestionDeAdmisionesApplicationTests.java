package com.projecto.gestiondeadmisiones;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = {
        "JWT_SECRET=clave-de-prueba-solo-para-tests-minimo-32-bytes",
        "POSTGRES_PASSWORD=test",
        "spring.flyway.enabled=false"
})
class GestionDeAdmisionesApplicationTests {

    @Test
    void contextLoads() {
    }

}
