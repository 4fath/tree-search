package com.holidu.interview.assignment.model;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class SearchRequestDto {

	private double xCo;
	private double yCo;
	private int radius;
}
