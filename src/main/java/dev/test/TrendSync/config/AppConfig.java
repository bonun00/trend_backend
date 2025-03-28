package dev.test.TrendSync.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    private final String kakaoClientId;
    private final String kakaoClientSecret;
    private final String kakaoRedirectUri;
    private final String jwtSecret;

    public AppConfig(
            @Value("${kakao.client-id}") String kakaoClientId,
            @Value("${kakao.client-secret}") String kakaoClientSecret,
            @Value("${kakao.redirect-uri}") String kakaoRedirectUri,
            @Value("${jwt.secret}") String jwtSecret
    ) {
        this.kakaoClientId = kakaoClientId;
        this.kakaoClientSecret = kakaoClientSecret;
        this.kakaoRedirectUri = kakaoRedirectUri;
        this.jwtSecret = jwtSecret;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getKakaoClientId() {
        return kakaoClientId;
    }

    public String getKakaoClientSecret() {
        return kakaoClientSecret;
    }

    public String getKakaoRedirectUri() {
        return kakaoRedirectUri;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }
}