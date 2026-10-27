package com.projecto.gestiondeadmisiones.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

// Cliente S3 para subir/leer los uploads de aspirantes (reemplaza el
// almacenamiento local en disco). Usa DefaultCredentialsProvider, que en la
// EC2 toma automaticamente las credenciales del IAM instance profile
// (aws_iam_instance_profile.ec2_profile en Terraform) sin necesidad de
// access keys hardcodeadas.
@Configuration
public class S3Config {

    @Value("${aws.region:us-west-2}")
    private String awsRegion;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
