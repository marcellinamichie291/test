package aneece.banoun.dto;

import static aneece.banoun.AppConfiguration.FORMATTER;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SiteDeviceOutage {

	private String id;
	private String name;
	private LocalDateTime begin;
	private LocalDateTime end;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getBegin() {
		return begin;
	}

	public void setBegin(LocalDateTime begin) {
		this.begin = begin;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public static JsonSerializer<SiteDeviceOutage> siteDeviceOutageSerializer() {
		return (SiteDeviceOutage siteDeviceOutage, Type typeOfSrc, JsonSerializationContext context) -> {
			JsonObject json = new JsonObject();
			json.addProperty("id", siteDeviceOutage.getId());
			json.addProperty("name", siteDeviceOutage.getName());
			json.addProperty("begin", siteDeviceOutage.getBegin().format(FORMATTER));
			json.addProperty("end", siteDeviceOutage.getEnd().format(FORMATTER));
			return json;
		};

	}

}
