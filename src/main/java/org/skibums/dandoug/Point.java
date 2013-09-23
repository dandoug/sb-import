package org.skibums.dandoug;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(NON_NULL)
public class Point {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public static class Location {
		public final double lat;
		public final double lon;
		
		@JsonCreator
		public Location(
				@JsonProperty("lat") double lat, 
				@JsonProperty("lon") double lon) {
			super();
			this.lat = lat;
			this.lon = lon;
		}
		
	}
	
	private final PointCategory cat;
	private final Location location;
	private final String name;
	private final String street;
	private final String city;
	private final String state;
	private final String zip;
	private final String phone;

	@JsonCreator
	public Point(
			@JsonProperty("cat") PointCategory cat, 
			@JsonProperty("location") Location location, 
			@JsonProperty("name") String name,
			@JsonProperty("street") String street, 
			@JsonProperty("city") String city, 
			@JsonProperty("state") String state, 
			@JsonProperty("zip") String zip, 
			@JsonProperty("phone") String phone) {
		this.cat = cat;
		this.location = location;
		this.name = name;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone = phone;
	}

	public PointCategory getCat() {
		return cat;
	}

	public Location getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getPhone() {
		return phone;
	}

	@Override
	public String toString() {
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return super.toString();
		}
	}
	
	
	
}
