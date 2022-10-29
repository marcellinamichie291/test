package aneece.banoun;

import java.time.format.DateTimeFormatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SpringBootApplication
public class KrakenFlexApplication {

	public static void main(String[] args) {
		SpringApplication.run(KrakenFlexApplication.class, args);
	}

}
