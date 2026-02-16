package org.songeun.petdongne_server.security.login;

import org.songeun.petdongne_server.security.FrontendUrlProperties;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginRedirectUrls {

    private final String successRedirectUrl;
    private final String failureRedirectUrlFormat;

    public OAuth2LoginRedirectUrls(FrontendUrlProperties frontendUrlProperties) {
        this.successRedirectUrl = frontendUrlProperties.getUrl() + "/oauth2/redirect";
        this.failureRedirectUrlFormat = frontendUrlProperties.getUrl() + "/login?error=";
    }

    public String getSuccessRedirectUrl() {
        return successRedirectUrl;
    }

    public String getFailureRedirectUrl(String errorCode){
        return failureRedirectUrlFormat + errorCode;
    }
}
