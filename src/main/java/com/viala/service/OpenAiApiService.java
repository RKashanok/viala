package com.viala.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAiApiService {

    private final OpenAiService openAiService;

    public OpenAiApiService(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey);
    }

    public String getDrugCompatibility(String drugList) {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Check compatibility for the following drugs: " + drugList)
                .model("text-davinci-003")
                .maxTokens(2048)
                .build();
        return openAiService.createCompletion(completionRequest).getChoices().get(0).getText();
    }

    public String getDrugSelectionForSymptoms(String symptoms, String currentMedications) {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("Suggest a drug for the following symptoms: " + symptoms + ". Current medications: " + currentMedications)
                .model("text-davinci-003")
                .maxTokens(2048)
                .build();
        return openAiService.createCompletion(completionRequest).getChoices().get(0).getText();
    }
}
