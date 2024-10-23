package com.git.repo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.git.repo.dto.BranchDetails;
import com.git.repo.service.GitlabService;

@RestController
public class RepositoryController {
	
	@Autowired
    private GitlabService gitlabService;

    @PostMapping("/create")
    public ResponseEntity<Object> createRepositoryWithBranches(
            @RequestParam String projectName,
            @RequestParam String description,
            @RequestParam String groupId,
            @RequestBody List<BranchDetails> branchDetailsList) throws Exception {

        return gitlabService.createRepositoryWithBranches(projectName, description, groupId, branchDetailsList);
    }

}
