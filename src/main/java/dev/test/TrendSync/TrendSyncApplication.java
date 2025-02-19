package dev.test.TrendSync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TrendSyncApplication {

	public static void main(String[] args) {
//		SpringApplication.run(TrendSyncApplication.class, args);
		ApplicationContext context = SpringApplication.run(TrendSyncApplication.class, args);

		// NaverNewsService 빈 가져오기
		dev.test.TrendSync.NaverNewsService newsService = context.getBean(dev.test.TrendSync.NaverNewsService.class);

		// 뉴스 API 호출 및 출력
		String newsData = newsService.fetchITNews();
		System.out.println("🔥 IT 뉴스 데이터:\n" + newsData);




	}



}
