package com.bunch.of.ideas.doctorapi.controller;

import com.bunch.of.ideas.doctorapi.entity.Plan;
import com.bunch.of.ideas.doctorapi.entity.User;
import com.bunch.of.ideas.doctorapi.repository.PlanRepository;
import com.bunch.of.ideas.doctorapi.repository.UserRepository;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {

    private static String authorizationRequestBaseUri = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final OAuth2AuthorizedClientService authorizedClientService;

    private final UserRepository userRepository;

    private final PlanRepository planRepository;

    public LoginController(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService, UserRepository userRepository, PlanRepository planRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientService = authorizedClientService;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @GetMapping("/oauth_login")
    public String getLoginPage(Model model) {
        System.out.println("PAGE: /oauth_login");
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        model.addAttribute("urls", oauth2AuthenticationUrls);

        return "oauth_login";
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

    @GetMapping("/loginSuccess")
    public String getLoginInfo(Model model, OAuth2AuthenticationToken authentication) {
        System.out.println("PAGE: /loginSuccess");
        Map userAttributes = extractUserData(authentication);
        model.addAttribute("name", userAttributes.get("name"));
        model.addAttribute("email", userAttributes.get("email"));
        model.addAttribute("imageUrl", userAttributes.get("picture"));
        Optional<User> oUser = this.userRepository.findUserByEmail(userAttributes.get("email").toString());
        if (oUser.isPresent()){
            User user = oUser.get();
            user.setUpdateDate(LocalDateTime.now());
            this.userRepository.save(user);
        }else {
            this.userRepository.save(
                    new User(
                            userAttributes.get("email").toString(),
                            userAttributes.get("name").toString(),
                            userAttributes.get("picture").toString(),
                            LocalDateTime.now(),
                            LocalDateTime.now()
                            ));
            this.planRepository.save(new Plan(
                    userAttributes.get("email").toString(),
                    5,
                    "Free",
                    false,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1)
            ));
        }
        model.addAttribute("plan", this.planRepository.findPlanByEmail(userAttributes.get("email").toString()).get());
        return "loginSuccess";
    }

//    @GetMapping("/newSubscription")
//    public String getNewSubscription(Model model,OAuth2AuthenticationToken authentication) {
//        System.out.println("PAGE: /newSubscription");
//        Map userAttributes = extractUserData(authentication);
//        model.addAttribute("email", userAttributes.get("email"));
//        Product[] products = ProductDAO.products;
//        model.addAttribute("products", products);
//        model.addAttribute("plan", this.planRepository.findPlanByEmail(userAttributes.get("email").toString()).get());
//        return "newSubscription";
//    }

//    @GetMapping("/subscription-with-trial")
//    public String getNewSubscriptionWithTrial(Model model,OAuth2AuthenticationToken authentication) {
//        System.out.println("PAGE: /subscription-with-trial");
//        Map userAttributes = extractUserData(authentication);
//        model.addAttribute("email", userAttributes.get("email"));
//        Product[] products = ProductDAO.products;
//        model.addAttribute("products", products);
//        model.addAttribute("plan", this.planRepository.findPlanByEmail(userAttributes.get("email").toString()).get());
//        return "newSubscriptionWithTrial";
//    }

//    @GetMapping("/cancel-subscription")
//    public String getCancelSubscription(Model model,OAuth2AuthenticationToken authentication) {
//        System.out.println("PAGE: /cancel-subscription");
//        Map userAttributes = extractUserData(authentication);
//        model.addAttribute("email", userAttributes.get("email"));
//        model.addAttribute("plan", this.planRepository.findPlanByEmail(userAttributes.get("email").toString()).get());
//        return "cancelSubscription";
//    }

//    @GetMapping("/view-invoices")
//    public String getViewInvoices(Model model,OAuth2AuthenticationToken authentication) {
//        System.out.println("PAGE: /view-invoices");
//        Map userAttributes = extractUserData(authentication);
//        model.addAttribute("email", userAttributes.get("email"));
//        model.addAttribute("plan", this.planRepository.findPlanByEmail(userAttributes.get("email").toString()).get());
//        return "viewInvoices";
//    }

//    @GetMapping("/success")
//    public String getSuccess(Model model, OAuth2AuthenticationToken authentication) {
//        System.out.println("PAGE: /success");
//        Map userAttributes = extractUserData(authentication);
//        Optional<Plan> oPlan = this.planRepository.findPlanByEmail(userAttributes.get("email").toString());
//        if (oPlan.isPresent()){
//            Plan plan = oPlan.get();
//            plan.setPlanName("Paid");
//            plan.setUpdateDate(LocalDateTime.now());
//            plan.setRequests(15);
//            plan.setIsFree(false);
//            plan.setProductId("price_1OSGIFGs0IMxIVZSJvSbsnFC");
//            this.planRepository.save(plan);
//        }
//
//        model.addAttribute("plan", this.planRepository.findPlanByEmail(userAttributes.get("email").toString()).get());
//        return "success";
//    }

//    @GetMapping("/failure")
//    public String getFailure(Model model, OAuth2AuthenticationToken authentication) {
//        Map userAttributes = extractUserData(authentication);
//        model.addAttribute("plan", this.planRepository.findPlanByEmail(userAttributes.get("email").toString()).get());
//        System.out.println("PAGE: /failure");
//        return "failure";
//    }
}
