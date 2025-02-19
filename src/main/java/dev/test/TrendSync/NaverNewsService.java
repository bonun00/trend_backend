package dev.test.TrendSync;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class NaverNewsService {


    private final RestTemplate restTemplate;


    @Value("${naver.api.client-id}")
    private String clientId;

    @Value("${naver.api.client-secret}")
    private String clientSecret;

    @Value("${naver.api.url}")
    private String apiUrl;

    // 컨트롤러에서 호출하지 않고, 서비스에서 직접 실행
    public String fetchITNews() {
        try {
            // **임의로 검색어를 설정 (컨트롤러 입력 없이)**
            String query = "IT 개발 OR 프로그래밍 OR 인공지능";
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            // 요청 URL 생성
            String requestUrl = apiUrl + "?query=" + encodedQuery + "&display=10&sort=date";

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Naver-Client-Id", clientId);
            headers.set("X-Naver-Client-Secret", clientSecret);

            // API 요청 실행
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred while fetching IT news.";
        }
    }
}