package space.obminyashka.items_exchange.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import space.obminyashka.items_exchange.rest.api.ApiKey;
import space.obminyashka.items_exchange.rest.exception.handler.TokenAuthenticator;
import space.obminyashka.items_exchange.rest.exception.handler.OAuthLoginSuccessHandler;
import space.obminyashka.items_exchange.service.JwtTokenService;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig {

    public static final String HAS_ROLE_ADMIN = "hasRole('ROLE_ADMIN')";
    private final OAuthLoginSuccessHandler oauthLoginSuccessHandler;
    private final AccessDeniedHandler accessDeniedHandler;
    private final @Lazy JwtTokenService tokenService;
    private final TokenAuthenticator tokenAuthenticator;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(BCryptPasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${app.jwt.secret}") String secret) {
        final var jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKeySpec(secret)).build();
        jwtDecoder.setJwtValidator(tokenService);
        return jwtDecoder;
    }

    @Bean
    public JwtEncoder jwtEncoder(@Value("${app.jwt.secret}") String secret) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKeySpec(secret)));
    }

    private static SecretKeySpec getSecretKeySpec(String secret) {
        return new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/",
                        "/favicon.ico",
                        "/static/css/**",
                        "/static/js/**",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.ttf").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/error", "/manage/**").permitAll()
                .requestMatchers(HttpMethod.POST, ApiKey.OAUTH2, ApiKey.OAUTH2_LOGIN).permitAll()
                .requestMatchers(HttpMethod.POST, ApiKey.AUTH_LOGIN, ApiKey.AUTH_REGISTER,
                        ApiKey.AUTH_REFRESH_TOKEN, ApiKey.USER_SERVICE_RESET_PASSWORD).permitAll()
                .requestMatchers(HttpMethod.GET, ApiKey.NOT_IMPLEMENTED).permitAll()
                .requestMatchers(HttpMethod.GET,
                        ApiKey.FRONT_LOGIN,
                        ApiKey.FRONT_SIGN,
                        ApiKey.FRONT_USER,
                        ApiKey.FRONT_ADV_ADD,
                        ApiKey.FRONT_PRODUCT,
                        ApiKey.FRONT_FILTER).permitAll()
                .requestMatchers(HttpMethod.GET,
                        ApiKey.OAUTH2_SUCCESS,
                        ApiKey.ADV + "/**",
                        ApiKey.EMAIL + "/**",
                        ApiKey.IMAGE + "/**",
                        ApiKey.CATEGORY + "/**",
                        ApiKey.SUBCATEGORY + "/**",
                        ApiKey.LOCATION + "/**").permitAll()
                .requestMatchers(ApiKey.AUTH_OAUTH2_SUCCESS, ApiKey.AUTH_LOGOUT, ApiKey.USER_MY_INFO, ApiKey.USER_MY_ADV).authenticated()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .oauth2Login().successHandler(oauthLoginSuccessHandler)
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(tokenAuthenticator)
                .and()
                .build();
    }
}
