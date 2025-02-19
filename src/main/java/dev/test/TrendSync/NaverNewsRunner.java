package dev.test.TrendSync;


import dev.test.TrendSync.NaverNewsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class NaverNewsRunner implements CommandLineRunner {
    private final NaverNewsService naverNewsService;

    public NaverNewsRunner(NaverNewsService naverNewsService) {
        this.naverNewsService = naverNewsService;
    }

    @Override
    public void run(String... args) {
        String newsData = naverNewsService.fetchITNews();
        System.out.println("🔥 IT 뉴스 데이터:\n" + newsData);
    }
}