package com.projecto.gestiondeadmisiones.infrastructure.entrypoints;

import com.projecto.gestiondeadmisiones.domain.model.aspirante.Aspirante;


import com.projecto.gestiondeadmisiones.domain.model.enums.EstadoInscripcion;
import com.projecto.gestiondeadmisiones.domain.model.enums.RolPersonal;
import com.projecto.gestiondeadmisiones.domain.model.exceptions.*;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.Inscripcion;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.EscanerArchivosPort;
import com.projecto.gestiondeadmisiones.domain.model.personal.PersonalAdministrativo;
import com.projecto.gestiondeadmisiones.domain.usecase.aspirante.IniciarSesionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.aspirante.RegistrarAspiranteUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.documentos.AdjuntarDocumentosUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.ConsultarEstadoInscripcionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.EvaluarInscripcionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.InscripcionConAspirante;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.InscripcionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.ListarInscripcionesPendientesUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.ObtenerDetalleInscripcionUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.RegistrarPagoUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.incripcion.ValidarPagoUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.personaladministrativo.IniciarSesionPersonalUseCase;
import com.projecto.gestiondeadmisiones.domain.usecase.personaladministrativo.RegistrarPersonalUseCase;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.aspirante.dtoResponse.AspiranteResponse;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.aspirante.dtoRequest.LoginRequest;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.aspirante.dtoResponse.LoginResponse;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.aspirante.dtoRequest.ResgitroAspiranteRequest;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion.EstadoInscripcionResponseDTO;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion.EvaluarInscripcionRequestDTO;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion.InscripcionDetalleResponseDTO;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion.InscripcionPendienteResponseDTO;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion.InscripcionRequestDTO;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion.InscripcionResponseDTO;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion.RegistrarPagoRequestDTO;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.inscripcion.ValidarPagoRequestDTO;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.personal.dtoRequest.LoginPersonalRequest;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.personal.dtoRequest.RegistroPersonalRequest;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.personal.dtoResponse.LoginPersonalResponse;
import com.projecto.gestiondeadmisiones.infrastructure.entrypoints.dto.personal.dtoResponse.PersonalResponse;
import com.projecto.gestiondeadmisiones.infrastructure.helpers.jwt.JwtProvider;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {

    private final RegistrarAspiranteUseCase registrarAspiranteUseCase;
    private final IniciarSesionUseCase iniciarSesionUseCase;
    private final JwtProvider jwtProvider;
    private final Validator validator;
    private final InscripcionUseCase crearInscripcionUseCase;
    private final AdjuntarDocumentosUseCase adjuntarDocumentosUseCase;
    private final RegistrarPagoUseCase registrarPagoUseCase;
    private final ConsultarEstadoInscripcionUseCase consultarEstadoInscripcionUseCase;
    private final ListarInscripcionesPendientesUseCase listarInscripcionesPendientesUseCase;
    private final ObtenerDetalleInscripcionUseCase obtenerDetalleInscripcionUseCase;
    private final ValidarPagoUseCase validarPagoUseCase;
    private final EvaluarInscripcionUseCase evaluarInscripcionUseCase;
    private final RegistrarPersonalUseCase registrarPersonalUseCase;
    private final IniciarSesionPersonalUseCase iniciarSesionPersonalUseCase;
    private final EscanerArchivosPort escanerArchivosPort;

    // ---------- Aspirante: registro ----------

    public Mono<ServerResponse> registrarAspirante(ServerRequest request) {
        return request.bodyToMono(ResgitroAspiranteRequest.class)
                .flatMap(this::validar)
                .flatMap(dto -> registrarAspiranteUseCase.ejecutar(aAspirante(dto), dto.getContrasena()))
                .flatMap(aspirante -> ServerResponse
                        .ok()
                        .bodyValue(aRespuesta(aspirante)));
    }

    // ---------- Aspirante: login ----------

    public Mono<ServerResponse> loginAspirante(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(this::validar)
                .flatMap(dto -> iniciarSesionUseCase.ejecutar(dto.getCorreoElectronico(), dto.getContrasena()))
                .flatMap(aspirante -> {
                    String token = jwtProvider.generarTokenAspirante(aspirante.getId(), aspirante.getCorreoElectronico());
                    return ServerResponse.ok().bodyValue(
                            LoginResponse.builder().token(token).tipo("Bearer").build());
                });
    }

    // ---------- Personal administrativo: registro (protegido por rol ADMISIONES) ----------

    public Mono<ServerResponse> registrarPersonal(ServerRequest request) {
        return request.bodyToMono(RegistroPersonalRequest.class)
                .flatMap(this::validar)
                .flatMap(dto -> {
                    PersonalAdministrativo personal;
                    try {
                        personal = aPersonal(dto);
                    } catch (IllegalArgumentException e) {
                        return Mono.error(e);
                    }
                    return registrarPersonalUseCase.ejecutar(personal, dto.getContrasena());
                })
                .flatMap(personal -> ServerResponse.ok().bodyValue(aRespuestaPersonal(personal)));
    }

    // ---------- Personal administrativo: login unificado por rol ----------

    public Mono<ServerResponse> loginPersonal(ServerRequest request) {
        return request.bodyToMono(LoginPersonalRequest.class)
                .flatMap(this::validar)
                .flatMap(dto -> iniciarSesionPersonalUseCase.ejecutar(dto.getCorreoElectronico(), dto.getContrasena()))
                .flatMap(personal -> {
                    String token = jwtProvider.generarTokenPersonal(
                            personal.getId(), personal.getCorreoElectronico(), personal.getRol().name());
                    return ServerResponse.ok().bodyValue(
                            LoginPersonalResponse.builder()
                                    .token(token)
                                    .tipo("Bearer")
                                    .rol(personal.getRol().name())
                                    .build());
                });
    }

    // ---------- Inscripción: crear ----------

    public Mono<ServerResponse> crearInscripcion(ServerRequest request) {
        return request.bodyToMono(InscripcionRequestDTO.class)
                .flatMap(this::validar)
                .zipWith(obtenerAspiranteIdAutenticado())
                .flatMap(tupla -> crearInscripcionUseCase.ejecutar(
                        aInscripcion(tupla.getT1(), tupla.getT2())))
                .flatMap(inscripcion -> ServerResponse.status(201).bodyValue(aRespuestaInscripcion(inscripcion)));
    }

    public Mono<ServerResponse> adjuntarDocumentos(ServerRequest request) {
        return obtenerAspiranteIdAutenticado()
                .flatMap(aspiranteId -> request.multipartData()
                        .flatMap(partes -> {
                            Part documentoIdentidad = partes.getFirst("documentoIdentidad");
                            Part foto = partes.getFirst("foto");

                            if (!(documentoIdentidad instanceof FilePart) || !(foto instanceof FilePart)) {
                                return Mono.error(new IllegalArgumentException(
                                        "Debe adjuntar el documento de identidad y la foto 3x4"));
                            }

                            return Mono.zip(
                                    guardarArchivo((FilePart) documentoIdentidad, aspiranteId, "documento"),
                                    guardarArchivo((FilePart) foto, aspiranteId, "foto")
                            ).flatMap(rutas -> adjuntarDocumentosUseCase.ejecutar(
                                    aspiranteId, rutas.getT1(), rutas.getT2()));
                        }))
                .flatMap(inscripcion -> ServerResponse.ok().bodyValue(aRespuestaInscripcion(inscripcion)));
    }

    private Mono<String> guardarArchivo(FilePart archivo, Long aspiranteId, String tipo) {
        validarArchivo(archivo);

        return DataBufferUtils.join(archivo.content())
                .flatMap(buffer -> {
                    byte[] contenido = new byte[buffer.readableByteCount()];
                    buffer.read(contenido);
                    DataBufferUtils.release(buffer);
                    return escanerArchivosPort.esSeguro(contenido)
                            .flatMap(seguro -> {
                                if (!seguro) {
                                    return Mono.error(new ArchivoInseguroException());
                                }
                                return escribirArchivo(contenido, archivo.filename(), aspiranteId, tipo);
                            });
                });
    }

    private Mono<String> escribirArchivo(byte[] contenido, String nombreOriginal, Long aspiranteId, String tipo) {
        String extension = obtenerExtension(nombreOriginal);
        String nombreArchivo = tipo + "_" + aspiranteId + "_" + System.currentTimeMillis() + extension;
        Path carpeta = Paths.get("uploads/inscripciones");
        Path destino = carpeta.resolve(nombreArchivo);

        return Mono.fromCallable(() -> {
                    Files.createDirectories(carpeta);
                    Files.write(destino, contenido);
                    return "/uploads/inscripciones/" + nombreArchivo;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(IOException.class,
                        e -> new RuntimeException("No se pudo almacenar el archivo", e));
    }

    private void validarArchivo(FilePart archivo) {
        String nombre = archivo.filename().toLowerCase();
        boolean extensionValida = nombre.endsWith(".pdf") || nombre.endsWith(".jpg")
                || nombre.endsWith(".jpeg") || nombre.endsWith(".png");
        if (!extensionValida) {
            throw new IllegalArgumentException("Solo se aceptan archivos PDF, JPG o PNG");
        }

        long tamano = archivo.headers().getContentLength();
        if (tamano > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("El archivo no debe superar los 5 MB");
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        int puntoIndex = nombreArchivo.lastIndexOf('.');
        return puntoIndex >= 0 ? nombreArchivo.substring(puntoIndex) : "";
    }

    // ---------- Inscripción: registrar pago ----------

    public Mono<ServerResponse> registrarPago(ServerRequest request) {
        return request.bodyToMono(RegistrarPagoRequestDTO.class)
                .flatMap(this::validar)
                .zipWith(obtenerAspiranteIdAutenticado())
                .flatMap(tupla -> {
                    RegistrarPagoRequestDTO dto = tupla.getT1();
                    Long aspiranteId = tupla.getT2();
                    return registrarPagoUseCase.ejecutar(
                            aspiranteId, dto.getReferenciaPago(), dto.getFechaPago(), dto.getBancoPago());
                })
                .flatMap(inscripcion -> ServerResponse.ok().bodyValue(aRespuestaInscripcion(inscripcion)));
    }

    // ---------- Inscripción: consultar estado ----------

    public Mono<ServerResponse> consultarEstadoInscripcion(ServerRequest request) {
        return obtenerAspiranteIdAutenticado()
                .flatMap(consultarEstadoInscripcionUseCase::ejecutar)
                .flatMap(inscripcion -> ServerResponse.ok().bodyValue(aRespuestaEstado(inscripcion)));
    }

    // ---------- Inscripción: listar pendientes (personal administrativo) ----------

    public Mono<ServerResponse> listarInscripcionesPendientes(ServerRequest request) {
        return listarInscripcionesPendientesUseCase.ejecutar()
                .map(this::aRespuestaPendiente)
                .collectList()
                .flatMap(lista -> ServerResponse.ok().bodyValue(lista));
    }

    // ---------- Inscripción: detalle (Admisiones, Tesorería, Evaluación) ----------

    public Mono<ServerResponse> obtenerDetalleInscripcion(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return obtenerDetalleInscripcionUseCase.ejecutar(id)
                .map(this::aRespuestaDetalle)
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto));
    }

    // ---------- Inscripción: validar pago (Tesorería) ----------

    public Mono<ServerResponse> validarPago(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(ValidarPagoRequestDTO.class)
                .flatMap(this::validar)
                .flatMap(dto -> validarPagoUseCase.ejecutar(id, dto.getAprobado(), dto.getObservacion()))
                .flatMap(inscripcion -> ServerResponse.ok().bodyValue(aRespuestaInscripcion(inscripcion)));
    }

    // ---------- Inscripción: evaluar resultado de admisión (Evaluación) ----------

    public Mono<ServerResponse> evaluarInscripcion(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(EvaluarInscripcionRequestDTO.class)
                .flatMap(this::validar)
                .flatMap(dto -> {
                    EstadoInscripcion decision;
                    try {
                        decision = EstadoInscripcion.valueOf(dto.getDecision().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return Mono.<Inscripcion>error(new IllegalArgumentException(
                                "La decisión debe ser APROBADO o RECHAZADO"));
                    }
                    return evaluarInscripcionUseCase.ejecutar(id, decision, dto.getObservacion());
                })
                .flatMap(inscripcion -> ServerResponse.ok().bodyValue(aRespuestaInscripcion(inscripcion)));
    }

    private <T> Mono<T> validar(T dto) {
        Set<ConstraintViolation<T>> violaciones = validator.validate(dto);
        if (!violaciones.isEmpty()) {
            String mensaje = violaciones.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            return Mono.error(new IllegalArgumentException(mensaje));
        }
        return Mono.just(dto);
    }

    private Mono<Long> obtenerAspiranteIdAutenticado() {
        return ReactiveSecurityContextHolder.getContext()
                .map(contexto -> (Long) contexto.getAuthentication().getPrincipal());
    }

    private Aspirante aAspirante(ResgitroAspiranteRequest dto) {
        return Aspirante.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .correoElectronico(dto.getCorreoElectronico())
                .build();
    }

    private AspiranteResponse aRespuesta(Aspirante aspirante) {
        return AspiranteResponse.builder()
                .id(aspirante.getId())
                .nombres(aspirante.getNombres())
                .apellidos(aspirante.getApellidos())
                .correoElectronico(aspirante.getCorreoElectronico())
                .build();
    }

    private PersonalAdministrativo aPersonal(RegistroPersonalRequest dto) {
        RolPersonal rol;
        try {
            rol = RolPersonal.valueOf(dto.getRol().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El rol debe ser ADMISIONES, TESORERIA o EVALUACION");
        }
        return PersonalAdministrativo.builder()
                .nombres(dto.getNombres())
                .apellidos(dto.getApellidos())
                .correoElectronico(dto.getCorreoElectronico())
                .rol(rol)
                .build();
    }

    private PersonalResponse aRespuestaPersonal(PersonalAdministrativo personal) {
        return PersonalResponse.builder()
                .id(personal.getId())
                .nombres(personal.getNombres())
                .apellidos(personal.getApellidos())
                .correoElectronico(personal.getCorreoElectronico())
                .rol(personal.getRol().name())
                .build();
    }

    private Inscripcion aInscripcion(InscripcionRequestDTO dto, Long aspiranteId) {
        return Inscripcion.builder()
                .aspiranteId(aspiranteId)
                .cumpleTituloBachiller(dto.getCumpleTituloBachiller())
                .cumpleIcfes(dto.getCumpleIcfes())
                .tipoDocumento(dto.getTipoDocumento())
                .numeroDocumento(dto.getNumeroDocumento())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(dto.getGenero())
                .telefono(dto.getTelefono())
                .ciudadResidencia(dto.getCiudadResidencia())
                .institucionBachillerato(dto.getInstitucionBachillerato())
                .municipioColegio(dto.getMunicipioColegio())
                .anioGraduacion(dto.getAnioGraduacion())
                .codigoResultadoIcfes(dto.getCodigoResultadoIcfes())
                .puntajeGlobalIcfes(dto.getPuntajeGlobalIcfes())
                .facultad(dto.getFacultad())
                .programa(dto.getPrograma())
                .semestre(dto.getSemestre())
                .build();
    }

    private InscripcionResponseDTO aRespuestaInscripcion(Inscripcion inscripcion) {
        return InscripcionResponseDTO.builder()
                .id(inscripcion.getId())
                .numeroInscripcion(inscripcion.getNumeroInscripcion())
                .estado((inscripcion.getEstadoInscripcion().name()))
                .programa(inscripcion.getPrograma())
                .semestre(inscripcion.getSemestre())
                .build();
    }

    private EstadoInscripcionResponseDTO aRespuestaEstado(Inscripcion inscripcion) {
        return EstadoInscripcionResponseDTO.builder()
                .id(inscripcion.getId())
                .numeroInscripcion(inscripcion.getNumeroInscripcion())
                .estado(inscripcion.getEstadoInscripcion().name())
                .programa(inscripcion.getPrograma())
                .semestre(inscripcion.getSemestre())
                .fechaCreacion(inscripcion.getFechaCreacion())
                .documentoIdentidadCargado(inscripcion.getDocumentoIdentidadUrl() != null)
                .fotoCargada(inscripcion.getFotoUrl() != null)
                .referenciaPago(inscripcion.getReferenciaPago())
                .fechaPago(inscripcion.getFechaPago())
                .build();
    }

    private InscripcionPendienteResponseDTO aRespuestaPendiente(InscripcionConAspirante item) {
        return InscripcionPendienteResponseDTO.builder()
                .inscripcionId(item.inscripcion().getId())
                .numeroInscripcion(item.inscripcion().getNumeroInscripcion())
                .estado(item.inscripcion().getEstadoInscripcion().name())
                .correoAspirante(item.aspirante().getCorreoElectronico())
                .nombresAspirante(item.aspirante().getNombres())
                .apellidosAspirante(item.aspirante().getApellidos())
                .fechaCreacion(item.inscripcion().getFechaCreacion())
                .build();
    }

    private InscripcionDetalleResponseDTO aRespuestaDetalle(InscripcionConAspirante item) {
        Inscripcion inscripcion = item.inscripcion();
        return InscripcionDetalleResponseDTO.builder()
                .id(inscripcion.getId())
                .numeroInscripcion(inscripcion.getNumeroInscripcion())
                .estado(inscripcion.getEstadoInscripcion().name())
                .observacion(inscripcion.getObservacion())
                .correoAspirante(item.aspirante().getCorreoElectronico())
                .nombresAspirante(item.aspirante().getNombres())
                .apellidosAspirante(item.aspirante().getApellidos())
                .tipoDocumento(inscripcion.getTipoDocumento())
                .numeroDocumento(inscripcion.getNumeroDocumento())
                .fechaNacimiento(inscripcion.getFechaNacimiento())
                .genero(inscripcion.getGenero())
                .telefono(inscripcion.getTelefono())
                .ciudadResidencia(inscripcion.getCiudadResidencia())
                .institucionBachillerato(inscripcion.getInstitucionBachillerato())
                .municipioColegio(inscripcion.getMunicipioColegio())
                .anioGraduacion(inscripcion.getAnioGraduacion())
                .codigoResultadoIcfes(inscripcion.getCodigoResultadoIcfes())
                .puntajeGlobalIcfes(inscripcion.getPuntajeGlobalIcfes())
                .facultad(inscripcion.getFacultad())
                .programa(inscripcion.getPrograma())
                .semestre(inscripcion.getSemestre())
                .documentoIdentidadUrl(inscripcion.getDocumentoIdentidadUrl())
                .fotoUrl(inscripcion.getFotoUrl())
                .referenciaPago(inscripcion.getReferenciaPago())
                .fechaPago(inscripcion.getFechaPago())
                .bancoPago(inscripcion.getBancoPago())
                .fechaCreacion(inscripcion.getFechaCreacion())
                .build();
    }
}
