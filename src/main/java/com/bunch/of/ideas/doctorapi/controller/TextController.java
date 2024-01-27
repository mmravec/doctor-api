package com.bunch.of.ideas.doctorapi.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TextController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public TextController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/chat")
    public String handleSubmit(Model model, OAuth2AuthenticationToken authentication) {
        Map userAttributes = extractUserData(authentication);
        System.out.println("/chat, email: " +userAttributes.get("email").toString());

        model.addAttribute("email", userAttributes.get("email"));
        model.addAttribute("messageList", new ArrayList<>());
        return "chat";
    }

    @GetMapping("/mini-chat")
    public String handleMiniChat(Model model, OAuth2AuthenticationToken authentication) {
        Map userAttributes = extractUserData(authentication);
        System.out.println("/mini-chat, email: " +userAttributes.get("email").toString());

        model.addAttribute("email", userAttributes.get("email"));
        model.addAttribute("messageList", new ArrayList<>());
        return "miniChat";
    }

    @GetMapping("/history")
    public String handleHistory(Model model, OAuth2AuthenticationToken authentication) {
        Map userAttributes = extractUserData(authentication);
        System.out.println("/history, email: " +userAttributes.get("email").toString());

        model.addAttribute("email", userAttributes.get("email"));
        model.addAttribute("messageList", new ArrayList<>());
        return "history";
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
