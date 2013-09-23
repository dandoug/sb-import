package org.skibums.dandoug;

import java.util.HashMap;
import java.util.Map;


public final class Utils { 
	
	private static final Map<String,String> state_abbreviations = new HashMap<>();
	
	private static final String[][] STATES = {
		{"ALABAMA", "AL"},
		{"ALASKA", "AK"},
		{"AMERICAN SAMOA", "AS"},
		{"ARIZONA", "AZ"},
		{"ARKANSAS", "AR"},
		{"CALIFORNIA", "CA"},
		{"COLORADO", "CO"},
		{"CONNECTICUT", "CT"},
		{"DELAWARE", "DE"},
		{"DISTRICT OF COLUMBIA", "DC"},
		{"FEDERATED STATES OF MICRONESIA", "FM"},
		{"FLORIDA", "FL"},
		{"GEORGIA", "GA"},
		{"GUAM GU", "GU"},
		{"HAWAII", "HI"},
		{"IDAHO", "ID"},
		{"ILLINOIS", "IL"},
		{"INDIANA", "IN"},
		{"IOWA", "IA"},
		{"KANSAS", "KS"},
		{"KENTUCKY", "KY"},
		{"LOUISIANA", "LA"},
		{"MAINE", "ME"},
		{"MARSHALL ISLANDS", "MH"},
		{"MARYLAND", "MD"},
		{"MASSACHUSETTS", "MA"},
		{"MICHIGAN", "MI"},
		{"MINNESOTA", "MN"},
		{"MISSISSIPPI", "MS"},
		{"MISSOURI", "MO"},
		{"MONTANA", "MT"},
		{"NEBRASKA", "NE"},
		{"NEVADA", "NV"},
		{"NEW HAMPSHIRE", "NH"},
		{"VNEW JERSEY", "NJ"},
		{"NEW MEXICO", "NM"},
		{"NEW YORK", "NY"},
		{"NORTH CAROLINA", "NC"},
		{"NORTH DAKOTA", "ND"},
		{"NORTHERN MARIANA ISLANDS", "MP"},
		{"OHIO", "OH"},
		{"OKLAHOMA", "OK"},
		{"OREGON", "OR"},
		{"PALAU", "PW"},
		{"PENNSYLVANIA", "PA"},
		{"PUERTO RICO", "PR"},
		{"RHODE ISLAND", "RI"},
		{"SOUTH CAROLINA", "SC"},
		{"SOUTH DAKOTA", "SD"},
		{"TENNESSEE", "TN"},
		{"TEXAS", "TX"},
		{"UTAH", "UT"},
		{"VERMONT", "VT"},
		{"VIRGIN ISLANDS", "VI"},
		{"VIRGINIA", "VA"},
		{"WASHINGTON", "WA"},
		{"WEST VIRGINIA", "WV"},
		{"WISCONSIN", "WI"},
		{"WYOMING", "WY"},
	};
	
	static {
		for (String[] st : STATES) {
			state_abbreviations.put(st[0], st[1]);
		}
	}

	public static String abbreviateState(String state) {
		if (state == null) {
			return null;
		}
		return state_abbreviations.get(state.toUpperCase().trim());
	}


	private Utils() {} // no instances
}
