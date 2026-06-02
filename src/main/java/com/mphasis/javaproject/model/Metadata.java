package com.mphasis.javaproject.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Metadata {
	
	private String source;
	private String batchId;

}
