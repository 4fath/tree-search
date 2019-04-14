package com.holidu.interview.assignment.resource;

import com.holidu.interview.assignment.model.SearchRequestDto;
import com.holidu.interview.assignment.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private SearchService searchService;

	@GetMapping(value = "/")
	public Map search(@RequestParam("point") String point, @RequestParam("radius") Integer radius) {

		String[] coordinatesStr = point.split(",");
		String xStr = coordinatesStr[0];
		String yStr = coordinatesStr[1];

		double xCo = Double.parseDouble(xStr);
		double yCo = Double.parseDouble(yStr);

		return searchService.search(SearchRequestDto.builder()
				.xCo(xCo)
				.yCo(yCo)
				.radius(radius).build());
	}
}
