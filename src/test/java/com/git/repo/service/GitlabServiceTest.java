package com.git.repo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.git.repo.dto.BranchDetails;

public class GitlabServiceTest {
	
	@InjectMocks
	GitlabService gitlabService;
	
	@Mock
	EnvConfig config;
	
	@Mock
	HttpClient client;
	
	@Mock
	HttpResponse<Object> httpResponse;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void getPrivateTokenTest() throws Exception {
		
		List<BranchDetails> list = new ArrayList<>();
		BranchDetails branchDetails = new BranchDetails("bhanu");
		list.add(branchDetails);
		String token = "mockToken";
		
		config.PRIVATE_TOKEN=token;
		
//		when(config.PRIVATE_TOKEN).thenReturn(token);
		
		String repToken  = gitlabService.getPrivateToken();
		
		
		String mockResponseBody= "{\r\n"
				+ "    \"id\": 876927594,\r\n"
				+ "    \"html_url\": \"https://github.com/Bhanu2112/test1234567\",\r\n"
				+ "    \"name\": \"test1234567\",\r\n"
				+ "    \"status\": \"created\"\r\n"
				+ "}";
		
		
		
        
        // Mock the HttpClient's send method
        when(client.send(any(HttpRequest.class), any())).thenReturn(httpResponse);

		//when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
		when(httpResponse.statusCode()).thenReturn(201);
		when(httpResponse.body()).thenReturn(mockResponseBody);
		
		ResponseEntity<Object> response = gitlabService.createRepositoryWithBranches("nhg", "ghng", "fgf", list);
		
		System.out.println(response);
		
		assertEquals(token, repToken);
		
		
	}

}
