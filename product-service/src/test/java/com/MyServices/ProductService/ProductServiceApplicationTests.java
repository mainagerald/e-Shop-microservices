package com.MyServices.ProductService;

import com.MyServices.ProductService.DTO.ProductRequest;
import com.MyServices.ProductService.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ProductServiceApplicationTests {
	@Container
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	@LocalServerPort
	private Integer port;
	@BeforeEach
	void setup(){
		RestAssured.baseURI = "http://localhost:" + port;
	}
	static {
		mongoDBContainer.start();
	}
	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = new ObjectMapper().writeValueAsString(productRequest);
		RestAssured.given().contentType("application/json").body(productRequestString)
				.when().post("/api/product").then().statusCode(201)
				.body("id", org.hamcrest.Matchers.notNullValue())
				.body("name", org.hamcrest.Matchers.equalTo(productRequest.getName()))
				.body("description", org.hamcrest.Matchers.equalTo(productRequest.getDescription()))
				.body("price", org.hamcrest.Matchers.equalTo(productRequest.getPrice().intValue()));
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("phone")
				.description("iphone 13")
				.price(BigDecimal.valueOf(1000))
				.build();
	}
}