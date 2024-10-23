package com.git.repo.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.git.repo.dto.BranchDetails;
import com.git.repo.exception.DataInvalidException;

@Service
public class GitlabService {

	@Autowired
	EnvConfig config;
	
	@Autowired
	HttpClient client;
	 
	 public String getPrivateToken() {
	       String token = EnvConfig.PRIVATE_TOKEN;
	       return token;
	 }
	 

	public ResponseEntity<Object> createRepositoryWithBranches(String projectName, String description, String groupId,
			List<BranchDetails> branchDetails) throws Exception {
		
		
		 if (branchDetails.isEmpty() || branchDetails.get(0).getBranchName().isEmpty()) {
	            throw new DataInvalidException("Branch details cannot be empty");
	        }

	        if (groupId.isEmpty()) {
	            throw new DataInvalidException("Group ID cannot be empty");
	        }
	        
	        
	        String privateToken = getPrivateToken();
	        
	        Map<String, Object> data = new HashMap();
	        data.put("name", projectName);
	        data.put("description", description);
	        data.put("visibility", "internal");
	        data.put("default_branch", "master");
	        data.put("initialize_with_readme", true);
	        data.put("namespace_id", groupId);
	        
	       // HttpClient client = HttpClient.newHttpClient();
	        
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(EnvConfig.GITLAB_API_URL))
	                .header("Content-Type", "application/json")
	                .header("Authorization", "Bearer "+ privateToken)
	                .POST(HttpRequest.BodyPublishers.ofString(new JSONObject(data).toString()))
	                .build();
	        
	        
	        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

	        System.out.println(response.statusCode());
	        System.out.println(privateToken);
	        JSONObject jsonResponse = new JSONObject(response.body());
	        int projectId;
	        
	        if (response.statusCode() == 201) {
	            projectId = jsonResponse.getInt("id");
	            System.out.println("Repository creation response: " + jsonResponse.toString()); // Log the full response
	        } else if (response.statusCode() == 401) {
	            throw new Exception("Git access token is not valid");
	        } else {
	            throw new Exception("Failed to create repository: " + jsonResponse.getString("message"));
	        }

	        // Create branches for the project
	        for (BranchDetails branchDetail : branchDetails) {
	          //  createBranchForProject(privateToken, projectId, branchDetail.getBranchName());
	        }

	        // Prepare response
	        JSONObject repoCreated = new JSONObject();
	        repoCreated.put("project_id", projectId);
	        repoCreated.put("project_name", jsonResponse.getString("name"));
	        repoCreated.put("html_url", jsonResponse.getString("html_url"));
	        repoCreated.put("status", "created");

	        return ResponseEntity.status(HttpStatus.CREATED).body(repoCreated.toMap());
	    }

	
	
	
	
	    private void createBranchForProject(String privateToken, int projectId, String branchName) throws Exception {
	        HttpClient client = HttpClient.newHttpClient();
	        String branchCreateUrl = config.GITLAB_API_URL+ "/projects/" + projectId + "/repository/branches?branch=" + branchName + "&ref=master";

	        HttpRequest branchRequest = HttpRequest.newBuilder()
	                .uri(URI.create(branchCreateUrl))
	                .header("Private-Token", privateToken)
	                .POST(HttpRequest.BodyPublishers.noBody())
	                .build();

	        HttpResponse<String> branchResponse = client.send(branchRequest, HttpResponse.BodyHandlers.ofString());

	        if (branchResponse.statusCode() != 201) {
	            throw new Exception("Failed to create branch: " + branchResponse.body());
	        }
	    }

}
