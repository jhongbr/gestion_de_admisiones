CREATE TABLE aspirante (
                           id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                           nombres VARCHAR(150) NOT NULL,
                           apellidos VARCHAR(150) NOT NULL,
                           tipo_documento VARCHAR(30) ,
                           numero_documento VARCHAR(30) ,
                           fecha_nacimiento DATE,
                           genero VARCHAR(30),
                           correo_electronico VARCHAR(150) NOT NULL UNIQUE,
                           password_hash VARCHAR(255) NOT NULL,
                           telefono VARCHAR(30),
                           ciudad_residencia VARCHAR(100),
                           institucion_bachillerato VARCHAR(200),
                           ciudad_colegio VARCHAR(100),
                           anio_graduacion INT,
                           resultado_icfes INT,
                           fecha_creacion_cuenta TIMESTAMP NOT NULL
);