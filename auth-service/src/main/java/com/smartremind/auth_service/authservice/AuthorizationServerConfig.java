package com.smartremind.auth_service.authservice;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class AuthorizationServerConfig {





    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder , JdbcTemplate jdbcTemplate){

        JdbcRegisteredClientRepository repository = new JdbcRegisteredClientRepository(jdbcTemplate);

        RegisteredClient webClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("smartremind-web")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:8080/login/oauth2/code/smartremind-web")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("read")
                .scope("write")
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(15))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .build()).build();


        if (repository.findByClientId("smartremind-web")==null) {
            repository.save(webClient);
        }
        return repository;

    }

// User details needed by downstream services
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext>oAuth2TokenCustomizer(){

        return context -> {

            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())){

                Authentication principal = context.getPrincipal();

                Collection<? extends GrantedAuthority>
                        authorities =
                        principal.getAuthorities();

                Set<String> roles =
                        authorities.stream()

                                .map(
                                        GrantedAuthority::getAuthority
                                )

                                .collect(Collectors.toSet());

                context.getClaims().claim("roles",roles);


            }


        };

    }



    @Bean
    public JWKSource<SecurityContext>jwkSource(){

        KeyPair keyPair = generateRsaKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        com.nimbusds.jose.jwk.RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return  new ImmutableJWKSet<>(jwkSet);


    }

    private static KeyPair generateRsaKey(){

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();

        }catch(Exception e){
            throw new IllegalStateException(e);
        }

    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext>jwkSource){
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);

    }


    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:8081")
                .build();

    }

}
