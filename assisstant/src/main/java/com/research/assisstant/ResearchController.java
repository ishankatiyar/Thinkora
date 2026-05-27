package com.research.assisstant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/research")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ResearchController {
    private final ResearchService researchService;

    @PostMapping("/process") 
    public ResponseEntity<String> processContent(@RequestBody ResearchRequest researchRequest) {
        String result = researchService.processContent(researchRequest);
        return ResponseEntity.ok(result);
    }


}