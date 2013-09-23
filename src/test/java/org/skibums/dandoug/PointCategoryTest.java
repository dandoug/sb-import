package org.skibums.dandoug;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PointCategoryTest {

	private static final String EXPECTED_JSON_VALUE = "{\"cat\":\"sb\"}";
	static ObjectMapper mapper = new ObjectMapper();
	TestBean bean;
	
	@Before
	public void setUp() throws Exception {
		bean = new TestBean(PointCategory.STARBUCKS);
	}

	@After
	public void tearDown() throws Exception {
		bean = null;
	}

	@Test
	public void testGetName() throws Exception {
		String json = mapper.writeValueAsString(bean);
		assertEquals(EXPECTED_JSON_VALUE,json);
	}

	@Test
	public void testForValue() throws Exception {
		TestBean b = mapper.readValue(EXPECTED_JSON_VALUE, TestBean.class);
		assertEquals(b.cat, PointCategory.STARBUCKS);
	}

	public static class TestBean {
		private final PointCategory cat;
		
		@JsonCreator
		public TestBean(@JsonProperty("cat") PointCategory c) {
			cat = c;
		}
		
		public PointCategory getCat() {
			return cat;
		}
			
	}
}
