package com.cloudinfo.hogwartsartifact.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${api.endpoint.base-url}")
    private String baseurl;

    @Value("${app.jwt.secret}")
    private String signKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(jwtConfigurer-> jwtConfigurer.decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                //.addFilterBefore(jwtFilterChain, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth->
                   auth.requestMatchers(HttpMethod.GET, baseurl+"/artifacts/**").permitAll()
                    .requestMatchers(HttpMethod.POST,baseurl+"/login").permitAll()
                    .requestMatchers( HttpMethod.POST,baseurl+"/register").permitAll()
                    .requestMatchers( HttpMethod.POST,baseurl+"/introspect").permitAll()
                    .requestMatchers(HttpMethod.GET, baseurl+"/wizards/**").permitAll()


                    .requestMatchers(HttpMethod.GET, baseurl+"/accounts/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, baseurl+"/accounts").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, baseurl+"/courses").hasRole("USER")
                    .requestMatchers(HttpMethod.GET, baseurl+"/courses").hasAuthority("ROLE_USER")
                    .requestMatchers(HttpMethod.DELETE, baseurl+"/courses/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, baseurl+"/accounts/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, baseurl+"/accounts/**").hasRole("ADMIN")
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                    .anyRequest().authenticated())
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))//access h2-console ui
                .httpBasic(Customizer.withDefaults())

                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter= new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter= new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec= new SecretKeySpec(signKey.getBytes(), "HS256");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }




}
