package com.wudgaby.sample.oauth2server.controller;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/10 0010 11:32
 * @desc :
 */
@Controller
@RequiredArgsConstructor
public class AuthController {
    private final RegisteredClientRepository registeredClientRepository;
    private final OAuth2AuthorizationConsentService authorizationConsentService;

    @GetMapping("/")
    public String index(Model model) {
        Map<String, Object> map = new HashMap<>(2);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        map.put("name", auth.getName());

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        List<? extends GrantedAuthority> authoritiesList = authorities.stream().collect(Collectors.toList());
        map.put("authorities", authoritiesList);

        model.addAttribute("user", JSONUtil.toJsonStr(map));
        return "index";
    }
    @GetMapping("/ssologin")
    public String ssologin(HttpSession session, Model model) {
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            String errorMsg = (ex != null) ? ex.getMessage() : "Invalid credentials";
            model.addAttribute("errorMsg", errorMsg);
        }
        return "ssologin";
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello(Principal principal){
        System.out.println(principal.toString());
        return "hello "+principal.getName();
    }

    @GetMapping(value = "/oauth2/consent")
    public String consent(Principal principal, Model model,
                          @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                          @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                          @RequestParam(OAuth2ParameterNames.STATE) String state) {

        // 要批准的范围和以前批准的范围
        Set<String> scopesToApprove = new HashSet<>();
        Set<String> previouslyApprovedScopes = new HashSet<>();
        // 查询 clientId 是否存在
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        // 查询当前的授权许可
        OAuth2AuthorizationConsent currentAuthorizationConsent =
                this.authorizationConsentService.findById(registeredClient.getId(), principal.getName());

        // 已授权范围
        Set<String> authorizedScopes;
        if (currentAuthorizationConsent != null) {
            authorizedScopes = currentAuthorizationConsent.getScopes();
        } else {
            authorizedScopes = Collections.emptySet();
        }
        for (String requestedScope : StringUtils.delimitedListToStringArray(scope, " ")) {
            if (OidcScopes.OPENID.equals(requestedScope)) {
                continue;
            }
            // 如果已授权范围包含了请求范围，则添加到以前批准的范围的 Set, 否则添加到要批准的范围
            if (authorizedScopes.contains(requestedScope)) {
                previouslyApprovedScopes.add(requestedScope);
            } else {
                scopesToApprove.add(requestedScope);
            }
        }

        model.addAttribute("clientId", clientId);
        model.addAttribute("state", state);
        model.addAttribute("scopes", withDescription(scopesToApprove));
        model.addAttribute("previouslyApprovedScopes", withDescription(previouslyApprovedScopes));
        model.addAttribute("principalName", principal.getName());

        return "consent";
    }

    private static Set<ScopeWithDescription> withDescription(Set<String> scopes) {
        Set<ScopeWithDescription> scopeWithDescriptions = new HashSet<>();
        for (String scope : scopes) {
            scopeWithDescriptions.add(new ScopeWithDescription(scope));
        }
        return scopeWithDescriptions;
    }

    public static class ScopeWithDescription {
        private static final String DEFAULT_DESCRIPTION = "未知范围 - 我们无法提供有关此权限的信息, 请在授予此权限时谨慎";
        private static final Map<String, String> scopeDescriptions = new HashMap<>();
        static {
            scopeDescriptions.put(
                    OidcScopes.PROFILE,
                    "读取您的个人资料信息"
            );
            scopeDescriptions.put(
                    OidcScopes.ADDRESS,
                    "读取您的个人住址"
            );
            scopeDescriptions.put(
                    OidcScopes.EMAIL,
                    "读取您的个人邮件地址"
            );
            scopeDescriptions.put(
                    OidcScopes.PHONE,
                    "读取您的个人手机号"
            );
            scopeDescriptions.put(
                    "read",
                    "此应用程序将能够读取您的信息"
            );
            scopeDescriptions.put(
                    "write",
                    "此应用程序将能够添加新信息, 它还可以编辑和删除现有信息"
            );
            scopeDescriptions.put(
                    "other.scope",
                    "这是范围描述的另一个范围示例"
            );
        }

        public final String scope;
        public final String description;

        ScopeWithDescription(String scope) {
            this.scope = scope;
            this.description = scopeDescriptions.getOrDefault(scope, DEFAULT_DESCRIPTION);
        }
    }
}
