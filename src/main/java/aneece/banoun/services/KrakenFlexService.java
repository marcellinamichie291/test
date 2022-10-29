package aneece.banoun.services;

import static aneece.banoun.AppConfiguration.FORMATTER;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import aneece.banoun.dto.Outage;
import aneece.banoun.dto.SiteDeviceOutage;
import aneece.banoun.dto.SiteInfo;

@Service
public class KrakenFlexService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private Gson gson;
	
	
	@Value("${kraken.x.api.key}")
	private String xApiKey;

	@Value("${kraken.outages.url}")
	private String outagesUrl;

	@Value("${kraken.site.info.url}")
	private String siteInfoUrl;

	@Value("${kraken.site.outages.url}")
	private String siteOutagesUrl;

	public List<Outage> outages() {
		ResponseEntity<String> response = outagesCall();
		Type listType = new TypeToken<List<Outage>>() {
		}.getType();
		String json = response.getBody();
		List<Outage> outages = gson.fromJson(json, listType);
		return outages;
	}

	public SiteInfo siteInfo(String siteId) {
		ResponseEntity<String> response = siteInfoCall(siteId);
		String json = response.getBody();
		SiteInfo siteInfo = gson.fromJson(json, SiteInfo.class);
		return siteInfo;
	}

	public ResponseEntity<String> outagesCall() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", xApiKey);
		Map<String, String> param = new HashMap<>();
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(outagesUrl, HttpMethod.GET, requestEntity, String.class, param);
		} catch (HttpStatusCodeException e) {
			response = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
					.body(e.getResponseBodyAsString());
		}

		return response;
	}

	public ResponseEntity<String> siteInfoCall(String siteId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", xApiKey);
		Map<String, String> param = new HashMap<>();
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = null;

		try {

			response = restTemplate.exchange(siteInfoUrl.replace("SITE_ID", siteId), HttpMethod.GET, requestEntity,
					String.class, param);
		} catch (HttpStatusCodeException e) {
			response = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
					.body(e.getResponseBodyAsString());
		}

		return response;
	}

	public ResponseEntity<String> siteOutages(String siteId, String beforeDate) {
		List<Outage> outages = outages();
		SiteInfo siteInfo = siteInfo(siteId);
		String jsonRequestBody = getSiteOutagesRequestBody(outages, siteInfo, beforeDate);
		return siteOutagesCall(siteId, jsonRequestBody);
	}

	public String getSiteOutagesRequestBody(List<Outage> outages, SiteInfo siteInfo, String beforeDate) {
		Map<String, String> deviceMap = new HashMap<>();
		siteInfo.getDevices().forEach(device -> deviceMap.put(device.getId(), device.getName()));

		outages = outages.stream().filter(out -> {
			boolean siteDevice = deviceMap.keySet().contains(out.getId());
			boolean isAfterDate = out.getBegin().isAfter(LocalDateTime.parse(beforeDate, FORMATTER));
			return siteDevice && isAfterDate;
		}).collect(Collectors.toList());

		List<SiteDeviceOutage> siteOutages = outages.stream().map(outage -> {
			SiteDeviceOutage siteDeviceOutage = new SiteDeviceOutage();
			siteDeviceOutage.setId(outage.getId());
			siteDeviceOutage.setBegin(outage.getBegin());
			siteDeviceOutage.setEnd(outage.getEnd());
			siteDeviceOutage.setName(deviceMap.get(outage.getId()));
			return siteDeviceOutage;
		}).collect(Collectors.toList());
		return gson.toJson(siteOutages);
	}

	public ResponseEntity<String> siteOutagesCall(String siteId, String jsonRequestBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", xApiKey);
		HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);
		ResponseEntity<String> response = null;

		try {
			response = restTemplate.postForEntity(siteOutagesUrl.replace("SITE_ID", siteId), requestEntity,
					String.class);
		} catch (HttpStatusCodeException e) {
			response = ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
					.body(e.getResponseBodyAsString());
		}

		return response;
	}

}
