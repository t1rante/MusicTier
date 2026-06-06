package com.musictier.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// Avisa o Spring Boot que este é um arquivo de configuração de infraestrutura
@Configuration
public class CorsGlobalConfig {

    // O @Bean diz ao Spring para carregar este filtro logo que o servidor ligar
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Autoriza expressamente o React, tanto via localhost quanto pelo IP numérico
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://127.0.0.1:5173");
        
        // Permite o envio de qualquer formato de dado (como o nosso JSON)
        config.addAllowedHeader("*");
        
        // Permite todos os verbos HTTP
        config.addAllowedMethod("*");
        
        // Aplica este "crachá VIP" para absolutamente todas as rotas da nossa API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}