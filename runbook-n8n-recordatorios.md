# Runbook: n8n + Recordatorios de Inscripción (UNAC)

Todo lo que hicimos hoy, en orden, para poder repetirlo rápido si se pierde el workflow o tienes que reiniciar todo antes de la entrega.

## 0. Arquitectura general

- **n8n**: corre en Docker (contenedor `n8n`), en `http://localhost:5678`.
- **Backend (GestionDeAdmisiones)**: proyecto Gradle/Spring Boot (WebFlux + R2DBC), corre **fuera de Docker**, directo en Windows, en `http://localhost:8080`.
- **Postgres** y **ClamAV**: corren en Docker (`docker-compose.yml` del backend), contenedores `gestion-admisiones-db` y `gestion-admisiones-clamav`.
- Como n8n está en un contenedor y el backend corre directo en Windows, n8n **no puede** llamarlo como `http://backend:8080` (no existe ese host). Hay que usar `http://host.docker.internal:8080` en los nodos HTTP del workflow.

## 1. Levantar Postgres y ClamAV

Desde la carpeta del backend (`C:\Users\John\Documents\GestionDeAdmisiones`), donde está el `docker-compose.yml`:

```bash
docker compose up -d postgres clamav
```

Verifica con `docker ps` que `gestion-admisiones-db` esté `Up` y `gestion-admisiones-clamav` llegue a `healthy` (tarda 1-2 min la primera vez, descarga firmas de virus).

Requiere un archivo `.env` en esa misma carpeta con `POSTGRES_PASSWORD` (ya lo tienes).

## 2. Levantar el backend

En PowerShell, desde la carpeta del backend:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:POSTGRES_PASSWORD="el_valor_de_tu_.env"
$env:JWT_SECRET="el_valor_de_tu_.env"
$env:ADMIN_SEED_EMAIL="<tu correo real usado>"
$env:ADMIN_SEED_PASSWORD="<tu contraseña real usada>"
.\gradlew.bat bootRun
```

Espera en el log:
```
Netty started on port 8080 (http)
Started GestionDeAdmisionesApplication in X seconds
```
Y si es la primera vez que corre con `ADMIN_SEED_EMAIL`/`ADMIN_SEED_PASSWORD` seteados:
```
Usuario administrativo inicial creado para el correo <tu correo>
```

### Por qué hacen falta `ADMIN_SEED_EMAIL` / `ADMIN_SEED_PASSWORD`

El backend tiene una clase `AdminSeedInitializer` (en `infrastructure/config/`) que crea la primera cuenta de personal administrativo (rol `ADMISIONES`) al arrancar, **solo si** esas dos variables de entorno no están vacías. Si ya existe un usuario con ese correo, no hace nada (seguro repetir el comando).

Esto es necesario porque el endpoint `POST /api/personal/registro` requiere estar **ya autenticado** con rol `ADMISIONES` (`SecurityConfig.java`) — no hay forma de crear la primera cuenta por API sin este seed. Rutas públicas (sin login): `/api/aspirantes/registro`, `/api/aspirantes/login`, `/api/personal/login`.

Esa cuenta (`ADMIN_SEED_EMAIL` / `ADMIN_SEED_PASSWORD`) es la que debes usar como "cuenta de automatización" en n8n.

## 3. Configurar el workflow en n8n

### 3.1 Si hay que reimportarlo
1. Menú **"⋯"** junto al nombre del workflow → **Import from File**.
2. Selecciona `recordatorio-inscripcion-incompleta.json`.
3. Verifica los 5 nodos conectados: `Cron Diario (8am) → Login Cuenta de Automatización → Listar Inscripciones Pendientes → Filtro: PENDIENTE y más de 3 días → Enviar Recordatorio`.
4. **Guarda de inmediato** (botón "Save" arriba a la derecha, o Ctrl+S). Si no guardas antes de navegar a otra pantalla o reiniciar el contenedor, se pierde aunque el volumen de Docker esté bien montado.
5. Confirma que quedó guardado recargando la página (F5) — si los nodos siguen ahí, está persistido.

> Nota: el volumen de n8n (`n8n_data` → `/home/node/.n8n`) **sí está correctamente montado**, eso no es el problema. El problema anterior fue simplemente no haber guardado a tiempo.

### 3.2 Nodo "Login Cuenta de Automatización"
- URL: cambiar de `http://backend:8080/api/personal/login` a:
  ```
  http://host.docker.internal:8080/api/personal/login
  ```
- `jsonBody`: correo y contraseña = los mismos de `ADMIN_SEED_EMAIL` / `ADMIN_SEED_PASSWORD`.

### 3.3 Nodo "Listar Inscripciones Pendientes"
- URL: cambiar de `http://backend:8080/api/admin/inscripciones/pendientes` a:
  ```
  http://host.docker.internal:8080/api/admin/inscripciones/pendientes
  ```

### 3.4 Nodo "Enviar Recordatorio" (credencial SMTP con Gmail)

1. Abre el nodo → **Connect to SMTP** → **Create New**.
2. Datos:
   - **Host:** `smtp.gmail.com`
   - **Port:** `465`
   - **SSL/TLS:** activado
   - **User:** tu correo `@unac.edu.co` (o el que uses)
   - **Password:** contraseña de aplicación de 16 caracteres (NO la contraseña normal) — se genera en [myaccount.google.com/apppasswords](https://myaccount.google.com/apppasswords), requiere verificación en 2 pasos activa.
3. Guarda la credencial.
4. Cambia el campo **From Email** del nodo (trae el placeholder `inscripciones@tu-dominio.edu`) por el mismo correo usado como `User` — Gmail rechaza/reescribe el remitente si no coincide con la cuenta autenticada.

## 4. Probar nodo por nodo

1. **Login Cuenta de Automatización** → "Execute step" → debe devolver un `token`.
2. **Listar Inscripciones Pendientes** → "Execute step" → debe traer inscripciones reales.
3. **Filtro: PENDIENTE y más de 3 días** → revisa que deje pasar lo esperado.
4. **Enviar Recordatorio** → prueba con `set mock data` (correo tuyo) antes de depender de datos reales, para validar que el envío por Gmail funciona:
   ```json
   {
     "correoAspirante": "tu-correo-personal@gmail.com",
     "nombresAspirante": "Juan",
     "apellidosAspirante": "Pérez",
     "numeroInscripcion": "TEST-001"
   }
   ```

## 5. Checklist final para la entrega

- [ ] Postgres y ClamAV arriba (`docker ps`, ClamAV en `healthy`).
- [ ] Backend arrancado con `ADMIN_SEED_EMAIL`/`ADMIN_SEED_PASSWORD` seteados y log de "Usuario administrativo inicial creado" (o cuenta ya existente de una corrida anterior).
- [ ] Workflow importado y **guardado** (confirmado con F5).
- [ ] URLs de los nodos HTTP apuntando a `host.docker.internal:8080` (no `backend:8080`).
- [ ] Nodo Login con las credenciales del admin seed.
- [ ] Credencial SMTP de Gmail configurada (contraseña de aplicación, no la normal) y sin triángulo de advertencia.
- [ ] From Email del nodo "Enviar Recordatorio" coincide con el User de la credencial SMTP.
- [ ] Prueba nodo por nodo exitosa (Login devuelve token, envío de correo de prueba llega).
- [ ] (Opcional) Frontend levantado para generar una inscripción `PENDIENTE` real y correr el workflow completo de punta a punta.
- [ ] Captura de pantalla o grabación corta del workflow ejecutándose, por si la necesitas para la entrega.
