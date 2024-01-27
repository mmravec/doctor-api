package com.bunch.of.ideas.doctorapi.service;

import com.bunch.of.ideas.doctorapi.config.DoctorConfig;
import jakarta.annotation.PreDestroy;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class LLMRequester {
    private OkHttpClient client;
    private final DoctorConfig doctorConfig;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public LLMRequester(DoctorConfig doctorConfig) {
        this.doctorConfig = doctorConfig;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public Future<String> get(String url) throws IOException {
        return executorService.submit(() -> {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", this.doctorConfig.getApiKey())
                    .addHeader(this.doctorConfig.getHeaderKey(), this.doctorConfig.getHeaderValue())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        });
    }

    public Future<String> post(String url, String jsonBody) throws IOException {
        return executorService.submit(() -> {
            MediaType mediaJson = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(jsonBody, mediaJson);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", this.doctorConfig.getApiKey())
                    .addHeader(this.doctorConfig.getHeaderKey(), this.doctorConfig.getHeaderValue())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        });
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Call PreDestroy");
        executorService.shutdown();
    }
}