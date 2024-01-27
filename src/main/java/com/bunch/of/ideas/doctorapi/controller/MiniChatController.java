package com.bunch.of.ideas.doctorapi.controller;

import com.bunch.of.ideas.doctorapi.entity.MentalHistory;
import com.bunch.of.ideas.doctorapi.entity.MiniChatHistory;
import com.bunch.of.ideas.doctorapi.entity.User;
import com.bunch.of.ideas.doctorapi.repository.MentalHistoryRepository;
import com.bunch.of.ideas.doctorapi.repository.UserRepository;
import com.bunch.of.ideas.doctorapi.service.DoctorService;
import com.bunch.of.ideas.doctorapi.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class MiniChatController {

    private final DoctorService doctorService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserRepository userRepository;
    private final UserMessageService userMessageService;
    private final MentalHistoryRepository mentalHistoryRepository;

    private Map<String, MiniChatHistory> miniChatHistories = new ConcurrentHashMap<>();

    public MiniChatController(DoctorService doctorService, OAuth2AuthorizedClientService authorizedClientService, UserRepository userRepository, UserMessageService userMessageService, MentalHistoryRepository mentalHistoryRepository) {
        this.doctorService = doctorService;
        this.authorizedClientService = authorizedClientService;
        this.userRepository = userRepository;
        this.userMessageService = userMessageService;
        this.mentalHistoryRepository = mentalHistoryRepository;
    }

    @PostMapping("/mini-chat")
    public ResponseEntity<?> sendMessage(@RequestBody String message, OAuth2AuthenticationToken authentication) {
        String userEmail = getUserEmail(authentication);
        System.out.println("/mini-chat, email: " + userEmail);

        MiniChatHistory chatHistory = getOrCreateMiniChatHistory(userEmail);
        chatHistory.addResponse(Integer.parseInt(message));

        String threadName = this.userRepository.findUserByEmail(userEmail)
                .map(User::getThreadId)
                .orElseGet(() -> {
                    String newThreadName = this.doctorService.registerThread();
                    User user = new User();
                    user.setEmail(userEmail);
                    user.setThreadId(newThreadName);
                    this.userRepository.save(user);
                    return newThreadName;
                });

        if (chatHistory.isComplete()) {
            double averageScore = chatHistory.calculateAverageScore();
            saveMentalHistory(userEmail, averageScore);
            // Reset or remove the MiniChatHistory object as it's no longer needed
            miniChatHistories.remove(userEmail);
        } else {
            // Store the updated chatHistory object for future use
        }

        String responseMsg = this.doctorService.runWorkflow(message, threadName);
        this.userMessageService.saveMessage(userEmail, message, responseMsg);
        return ResponseEntity.ok(responseMsg);
    }

    private void saveMentalHistory(String userEmail, double score) {
        MentalHistory history = new MentalHistory();
        history.setEmail(userEmail);
        history.setScore(score);
        history.setCreateDate(LocalDateTime.now());
        history.setUpdateDate(LocalDateTime.now());
        // Save history to the database
        this.mentalHistoryRepository.save(history);
    }

    private MiniChatHistory getOrCreateMiniChatHistory(String userEmail) {
        // Logic to retrieve existing MiniChatHistory or create a new one
        return miniChatHistories.computeIfAbsent(userEmail, k -> new MiniChatHistory(userEmail));
    }

    private String getUserEmail(OAuth2AuthenticationToken authentication) {
        Map<String, Object> userAttributes = extractUserData(authentication);
        return userAttributes.get("email").toString();
    }

    Map extractUserData(OAuth2AuthenticationToken authentication){
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity<Map> response = restTemplate
                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();
            return response.getBody();
        }
        return new HashMap<>();
    }
}
