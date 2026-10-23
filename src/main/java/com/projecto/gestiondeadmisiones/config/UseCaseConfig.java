package com.projecto.gestiondeadmisiones.config;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.AspiranteRepository;
import com.projecto.gestiondeadmisiones.domain.model.aspirante.gateway.PasswordEncoderPort;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.InscripcionRepository;
import com.projecto.gestiondeadmisiones.domain.model.personal.gateway.PersonalAdministrativoRepository;
import com.projecto.gestiondeadmisiones.domain.usecase.aspirante.IniciarSesionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.aspirante.RegistrarAspiranteUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.documentos.AdjuntarDocumentosUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.ConsultarEstadoInscripcionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.EvaluarInscripcionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.InscripcionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.ListarInscripcionesPendientesUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.ObtenerDetalleInscripcionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.RegistrarPagoUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.ValidarPagoUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.personaladministrativo.IniciarSesionPersonalUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.personaladministrativo.RegistrarPersonalUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegistrarAspiranteUseCase registrarAspiranteUseCase(AspiranteRepository aspiranteRepository,
                                                               PasswordEncoderPort passwordEncoderPort) {
        return new RegistrarAspiranteUseCase(aspiranteRepository, passwordEncoderPort);
    }

    @Bean
    public IniciarSesionUseCase iniciarSesionUseCase(AspiranteRepository aspiranteRepository,
                                                     PasswordEncoderPort passwordEncoderPort) {
        return new IniciarSesionUseCase(aspiranteRepository, passwordEncoderPort);
    }

    @Bean
    public InscripcionUseCase crearInscripcionUseCase(InscripcionRepository inscripcionRepository) {
        return new InscripcionUseCase(inscripcionRepository);
    }

    @Bean
    public AdjuntarDocumentosUseCase adjuntarDocumentosUseCase(InscripcionRepository inscripcionRepository) {
        return new AdjuntarDocumentosUseCase(inscripcionRepository);
    }

    @Bean
    public RegistrarPagoUseCase registrarPagoUseCase(InscripcionRepository inscripcionRepository) {
        return new RegistrarPagoUseCase(inscripcionRepository);
    }

    @Bean
    public ConsultarEstadoInscripcionUseCase consultarEstadoInscripcionUseCase(InscripcionRepository inscripcionRepository) {
        return new ConsultarEstadoInscripcionUseCase(inscripcionRepository);
    }

    @Bean
    public ListarInscripcionesPendientesUseCase listarInscripcionesPendientesUseCase(
            InscripcionRepository inscripcionRepository, AspiranteRepository aspiranteRepository) {
        return new ListarInscripcionesPendientesUseCase(inscripcionRepository, aspiranteRepository);
    }

    @Bean
    public ObtenerDetalleInscripcionUseCase obtenerDetalleInscripcionUseCase(
            InscripcionRepository inscripcionRepository, AspiranteRepository aspiranteRepository) {
        return new ObtenerDetalleInscripcionUseCase(inscripcionRepository, aspiranteRepository);
    }

    @Bean
    public ValidarPagoUseCase validarPagoUseCase(InscripcionRepository inscripcionRepository) {
        return new ValidarPagoUseCase(inscripcionRepository);
    }

    @Bean
    public EvaluarInscripcionUseCase evaluarInscripcionUseCase(InscripcionRepository inscripcionRepository) {
        return new EvaluarInscripcionUseCase(inscripcionRepository);
    }

    @Bean
    public RegistrarPersonalUseCase registrarPersonalUseCase(PersonalAdministrativoRepository personalAdministrativoRepository,
                                                               PasswordEncoderPort passwordEncoderPort) {
        return new RegistrarPersonalUseCase(personalAdministrativoRepository, passwordEncoderPort);
    }

    @Bean
    public IniciarSesionPersonalUseCase iniciarSesionPersonalUseCase(PersonalAdministrativoRepository personalAdministrativoRepository,
                                                                      PasswordEncoderPort passwordEncoderPort) {
        return new IniciarSesionPersonalUseCase(personalAdministrativoRepository, passwordEncoderPort);
    }
}
