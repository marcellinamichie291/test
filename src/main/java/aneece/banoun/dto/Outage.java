package aneece.banoun.dto;
 
import static aneece.banoun.AppConfiguration.FORMATTER;


import java.lang.reflect.Type;
import java.time.LocalDateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Outage {

	private String id;
	private LocalDateTime begin;
	private LocalDateTime end;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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


	public static JsonDeserializer<Outage> outageDeserializer() {
		return (JsonElement json, Type typeOfT, JsonDeserializationContext context) -> {
			Outage outage = new Outage();
			JsonObject jsonObject = json.getAsJsonObject();
			outage.setId(jsonObject.get("id").getAsString());
			LocalDateTime begin = LocalDateTime.parse(jsonObject.get("begin").getAsString(), FORMATTER);
			outage.setBegin(begin);
			LocalDateTime end = LocalDateTime.parse(jsonObject.get("end").getAsString(), FORMATTER);
			outage.setEnd(end);

			return outage;
		};

	}


}
