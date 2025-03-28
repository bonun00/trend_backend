package dev.test.TrendSync.controller;

import dev.test.TrendSync.config.AppConfig;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoTokenController {

    private final AppConfig appConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoTokenController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> getAccessToken(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");
        System.out.println("🔄 Spring Boot: /oauth/kakao/token 요청 받음");
        if (code == null || code.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "카카오 인증 코드가 없습니다."));
        }

        // ✅ 카카오 API 요청 로그 추가
        System.out.println("✅ 카카오 API 요청 시작");
        System.out.println("카카오 Client ID: " + appConfig.getKakaoClientId());
        System.out.println("카카오 Redirect URI: " + appConfig.getKakaoRedirectUri());
        System.out.println("카카오 인증 코드: " + code);

        // 카카오 API 요청
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", appConfig.getKakaoClientId());
        params.add("client_secret", appConfig.getKakaoClientSecret());
        params.add("redirect_uri", appConfig.getKakaoRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            // ✅ 카카오 API 응답 로그 추가
            System.out.println("🔄 카카오 API 응답 상태 코드: " + response.getStatusCode());
            System.out.println("📄 카카오 API 응답 본문: " + response.getBody());

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("카카오 인증 실패");
            }

            String kakaoAccessToken = (String) response.getBody().get("access_token");

            // ✅ 최종 응답 로그 추가
            System.out.println("✅ 카카오 액세스 토큰 발급 성공: " + kakaoAccessToken);

            return ResponseEntity.ok(Map.of("access_token", kakaoAccessToken));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "카카오 로그인 실패: " + e.getMessage()));
        }
    }
//    @PostMapping("/token")
//    public ResponseEntity<Map<String, String>> getAccessToken(@RequestBody Map<String, String> payload) {
//        String code = payload.get("code");
//
//        // 카카오에 access_token 요청
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", appConfig.getKakaoClientId());
//        params.add("client_secret", appConfig.getKakaoClientSecret());
//        params.add("redirect_uri", appConfig.getKakaoRedirectUri());
//        params.add("code", code);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.exchange(
//                    "https://kauth.kakao.com/oauth/token",
//                    HttpMethod.POST,
//                    request,
//                    Map.class
//            );
//
//            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
//                throw new RuntimeException("카카오 인증 실패");
//            }
//
//            String kakaoAccessToken = (String) response.getBody().get("access_token");
//
//            // 카카오에서 사용자 정보 요청
//            HttpHeaders userHeaders = new HttpHeaders();
//            userHeaders.setBearerAuth(kakaoAccessToken);
//            HttpEntity<?> userRequest = new HttpEntity<>(userHeaders);
//
//            ResponseEntity<Map> userResponse = restTemplate.exchange(
//                    "https://kapi.kakao.com/v2/user/me",
//                    HttpMethod.GET,
//                    userRequest,
//                    Map.class
//            );
//
//            if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null) {
//                throw new RuntimeException("사용자 정보 요청 실패");
//            }
//
//            Map<String, Object> kakaoAccount = (Map<String, Object>) userResponse.getBody().get("kakao_account");
//            String email = (String) kakaoAccount.get("email");
//
//            // JWT 발급
//            String jwtToken = generateJwtToken(email);
//
//            Map<String, String> jwtResponse = new HashMap<>();
//            jwtResponse.put("access_token", jwtToken);
//            return ResponseEntity.ok(jwtResponse);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "카카오 로그인 실패: " + e.getMessage()));
//        }
//    }

    private String generateJwtToken(String email) {
        return Jwts.builder()
                .setSubject(Base64.getEncoder().encodeToString(email.getBytes())) // 이메일을 Base64 인코딩
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 유효
                .signWith(SignatureAlgorithm.HS256, appConfig.getJwtSecret()) // AppConfig에서 JWT Secret 가져옴
                .compact();
    }
}