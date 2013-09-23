package org.skibums.dandoug;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PointCategory {
	
	STARBUCKS("sb"),
	;

	private final String name;

	private PointCategory(String name) {
		this.name = name;
	}
	
	@JsonValue
	public String getName() {
		return name;
	}
	
	@JsonCreator
	public static PointCategory forValue(String v) {
		for (PointCategory p : PointCategory.values()) {
			if (p.getName().equals(v)) {
				return p;
			}
		}
		throw new IllegalArgumentException("Unknown category: "+v);
	}
}
