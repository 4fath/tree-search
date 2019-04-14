package com.holidu.interview.assignment.service;

import com.holidu.interview.assignment.model.SearchRequestDto;
import com.holidu.interview.assignment.model.TreeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

	private static final String BASE_URL = "https://data.cityofnewyork.us/resource/nwxe-4ae8.json";
	private static final String BASE_Q = BASE_URL + "?$query= SELECT latitude, longitude, spc_common WHERE "
			+ "(spc_common IS NOT NULL) AND "
			+ "(latitude between %s and %s) AND "
			+ "(longitude between %s and %s)";

	@Autowired
	private RestTemplate restTemplate;

	public Map<String, Integer> search(SearchRequestDto dto) {
		String url = buildUrl(dto);

		ResponseEntity<List<TreeDto>> exchange = restTemplate
				.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<TreeDto>>() {});

		List<TreeDto> treeList = exchange.getBody();

		Map<String, Integer> responseMap = new HashMap<>();

		treeList.stream()
				.filter(treeDto -> isInRadius(treeDto, dto))
				.forEach(treeDto -> responseMap.compute(treeDto.getSpecCommon(), (k, v) -> v == null ? 1 : v + 1));

		return responseMap;
	}

	private boolean isInRadius(TreeDto treeDto, SearchRequestDto dto) {
		int radius = dto.getRadius();

		double latitudeDiff = Math.abs(treeDto.getLatitude() - dto.getXCo());
		double longitudeDiff = Math.abs(treeDto.getLongitude() - dto.getYCo());

		return radius >= (Math.sqrt(latitudeDiff * latitudeDiff + longitudeDiff * longitudeDiff));
	}

	private String buildUrl(SearchRequestDto dto) {

		// 1km in degree = 1 / 111km =~ 0.0089
		// 1m in degree = 0.0089 / 1000 =~ 0.0000089
		double diffX = dto.getRadius() * 0.0000089;

		double xForward = dto.getXCo() + diffX;
		double xBackward = dto.getXCo() - diffX;

		// pi / 180 = 0.018
		double diffY = diffX / Math.cos(dto.getXCo() * 0.018);
		double yForward = dto.getYCo() + diffY;
		double yBackward = dto.getYCo() - diffY;

		return String.format(BASE_Q, xBackward, xForward, yBackward, yForward);
	}

}
