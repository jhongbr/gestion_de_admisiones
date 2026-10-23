CREATE TABLE personal_administrativo (
                             id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             nombres VARCHAR(150) NOT NULL,
                             apellidos VARCHAR(150) NOT NULL,
                             correo_electronico VARCHAR(150) NOT NULL UNIQUE,
                             password_hash VARCHAR(255) NOT NULL,
                             rol VARCHAR(30) NOT NULL,
                             fecha_creacion_cuenta TIMESTAMP NOT NULL
);
