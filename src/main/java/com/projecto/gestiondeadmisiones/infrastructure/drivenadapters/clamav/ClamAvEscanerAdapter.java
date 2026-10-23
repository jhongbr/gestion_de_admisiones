package com.projecto.gestiondeadmisiones.infrastructure.drivenadapters.clamav;

import com.projecto.gestiondeadmisiones.domain.model.exceptions.EscaneoArchivoNoDisponibleException;
import com.projecto.gestiondeadmisiones.domain.model.inscripcion.gateway.EscanerArchivosPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class ClamAvEscanerAdapter implements EscanerArchivosPort {

    private static final byte[] COMANDO_INSTREAM = "zINSTREAM\0".getBytes(StandardCharsets.US_ASCII);
    private static final int TAMANO_CHUNK = 8192;

    private final String host;
    private final int puerto;
    private final int timeoutMs;

    public ClamAvEscanerAdapter(@Value("${app.clamav.host}") String host,
                                 @Value("${app.clamav.port}") int puerto,
                                 @Value("${app.clamav.timeout-ms:5000}") int timeoutMs) {
        this.host = host;
        this.puerto = puerto;
        this.timeoutMs = timeoutMs;
    }

    @Override
    public Mono<Boolean> esSeguro(byte[] contenido) {
        return Mono.fromCallable(() -> escanear(contenido))
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofMillis(timeoutMs))
                .onErrorMap(e -> !(e instanceof EscaneoArchivoNoDisponibleException),
                        e -> new EscaneoArchivoNoDisponibleException());
    }

    private boolean escanear(byte[] contenido) {
        try (Socket socket = new Socket(host, puerto)) {
            socket.setSoTimeout(timeoutMs);
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            salida.write(COMANDO_INSTREAM);

            int offset = 0;
            while (offset < contenido.length) {
                int longitud = Math.min(TAMANO_CHUNK, contenido.length - offset);
                salida.writeInt(longitud);
                salida.write(contenido, offset, longitud);
                offset += longitud;
            }
            salida.writeInt(0);
            salida.flush();

            try (BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII))) {
                String respuesta = entrada.readLine();
                if (respuesta == null) {
                    throw new EscaneoArchivoNoDisponibleException();
                }
                return respuesta.contains("OK") && !respuesta.contains("FOUND");
            }
        } catch (IOException e) {
            throw new EscaneoArchivoNoDisponibleException();
        }
    }
}
