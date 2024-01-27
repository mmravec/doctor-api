package com.bunch.of.ideas.doctorapi.service;

import com.bunch.of.ideas.doctorapi.config.DoctorConfig;
import com.bunch.of.ideas.doctorapi.entity.*;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class DoctorService {

    private final LLMRequester llmRequester;
    private final DoctorConfig doctorConfig;
    private final Gson gson;


    public DoctorService(LLMRequester llmRequester, DoctorConfig doctorConfig) {
        this.llmRequester = llmRequester;
        this.doctorConfig = doctorConfig;
        this.gson = new Gson();
    }

    public String registerThread(){
        String url = doctorConfig.getBaseUrl();
        try {
            Future<String> futureResponse = llmRequester.post(url, "");

            // Do other tasks...
            String threadStr = futureResponse.get(); // This will block until the result is available
            ThreadEntity threadEntity = this.gson.fromJson(threadStr, ThreadEntity.class);
            return threadEntity.getId();
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String addMessage(String text, String threadName){
        String url = doctorConfig.getBaseUrl() + "/" + threadName + "/messages";
        try {
            Map<String,String> body = new HashMap<>();
            body.put("role", "user");
            body.put("content", text);
            Future<String> futureResponse = llmRequester.post(url, this.gson.toJson(body));
            return futureResponse.get();
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public RunEntity getStatus(String threadName, String runName){
        String url = doctorConfig.getBaseUrl() + "/" + threadName + "/runs/" + runName;
        try {
            Future<String> futureResponse = llmRequester.get(url);
            String statusStr = futureResponse.get();
            return this.gson.fromJson(statusStr, RunEntity.class);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public RunEntity runThread(String threadName){
        String url = doctorConfig.getBaseUrl() + "/" + threadName + "/runs";
        try {
            Map<String,String> body = new HashMap<>();
            body.put("assistant_id", this.doctorConfig.getAssistantId());
            Future<String> futureResponse = llmRequester.post(url, this.gson.toJson(body));
            String threadStr = futureResponse.get();
            return this.gson.fromJson(threadStr, RunEntity.class);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ListEntity getMessage(String threadName){
        String url = doctorConfig.getBaseUrl() + "/" + threadName + "/messages";
        try {
            Future<String> futureResponse = llmRequester.get(url);
            String msgStr = futureResponse.get();
            return this.gson.fromJson(msgStr, ListEntity.class);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> extractTextValues(ListEntity threadEntity) {
        List<String> textValues = new ArrayList<>();
        List<Message> data = threadEntity.getData();
        if ( data != null) {
            for (Message message : data) {
                if (message.getContent() != null) {
                    for (Content content : message.getContent()) {
                        if (content.getText() != null) {
                            textValues.add(content.getText().getValue());
                        }
                    }
                }
            }
        }
        return textValues;
    }

    public String runWorkflow(String inputText, String threadName) {
        try {
            String postResponse = addMessage(inputText, threadName);
            RunEntity runThread = runThread(threadName);

            String runName = runThread.getId();

            boolean isDone = false;
            while (!isDone) {
                RunEntity status = getStatus(threadName, runName);
                if ("completed".equals(status.getStatus())) {
                    isDone = true;
                } else if ("failed".equals(status.getStatus())) {
                    throw new RuntimeException("Thread failed");
                }
                // Čakanie pred ďalšou kontrolou
                Thread.sleep(1000); // 1 sekunda
            }

            // Získanie správ z threadu
            ListEntity messagesResponse = getMessage(threadName);
            // Spracujte odpoveď podľa potreby
            List<String> data = extractTextValues(messagesResponse);
            if (!data.isEmpty()) {
                String finalText = data.get(0);
                return finalText;
//                return this.gson.fromJson(finalText, TextResponse.class);
            }else {
                return "";
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
