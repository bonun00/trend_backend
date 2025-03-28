package dev.test.TrendSync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;



@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TrendSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrendSyncApplication.class, args);






	}



}
