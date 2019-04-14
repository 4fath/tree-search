package com.holidu.interview.assignment.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TreeDto implements Serializable {

	private static final long serialVersionUID = -7081147407956226321L;

	private double latitude;
	private double longitude;

	@JsonProperty("spc_common")
	private String specCommon;
}
