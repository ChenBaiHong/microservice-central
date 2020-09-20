package com.baimicro.central.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @project: microservice-central
 * @author: chen.baihoo
 * @date: 2020/7/16
 * @Description: TODO
 * version 0.1
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public ModelMap home(ModelMap modelMap, Authentication authentication) {
        OAuth2Authentication oauth2Authentication = (OAuth2Authentication)authentication;
        modelMap.put("username", oauth2Authentication.getName());
        modelMap.put("authorities", oauth2Authentication.getAuthorities());
        modelMap.put("clientId", oauth2Authentication.getOAuth2Request().getClientId());
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)oauth2Authentication.getDetails();
        modelMap.put("token", details.getTokenValue());
        return modelMap;
    }
}
