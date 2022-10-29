package aneece.banoun;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import aneece.banoun.dto.Outage;
import aneece.banoun.dto.SiteDeviceOutage;

@Configuration
public class AppConfiguration {

	public final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Gson gson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Outage.class, Outage.outageDeserializer());
		gsonBuilder.registerTypeAdapter(SiteDeviceOutage.class, SiteDeviceOutage.siteDeviceOutageSerializer());
		return gsonBuilder.setPrettyPrinting().create();
	}
}
