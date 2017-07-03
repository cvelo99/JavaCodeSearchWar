package cc.jcsw.security;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import cc.springsecurity.authentication.AbstractCcUserDetailsAuthenticationProvider;
import cc.springsecurity.authentication.providers.HardCodedAuthenticationProvider;
import cc.springsecurity.authentication.providers.HardCodedAuthenticationProvider.UsernamePasswordGroupsList;
import cc.springsecurity.authentication.sso.HardCodedCookieInformation;
import cc.springsecurity.authentication.sso.ServletContextBasedSsoStorage;
import cc.springsecurity.authentication.sso.SsoCookieInformation;
import cc.springsecurity.authentication.sso.SsoStorage;
import cc.springsecurity.authorization.RolesFinder;
import cc.springsecurity.authorization.rolematchers.MatcherRolesContainer;
import cc.springsecurity.authorization.rolematchers.RequestMatcherContainer;
import cc.springsecurity.config.AbstractSecurityConfig;

/**
 * Security configuration.
 * 
 * @author Chris Carcel
 *
 */
@EnableWebSecurity
public class SecurityConfig extends AbstractSecurityConfig {

    @Value("${cookie_domain}")
    private String cookieDomain;

    @Autowired
    private ServletContext ctx;

    @Autowired
    private JcswRolesFinder rolesFinder;

    @Override
    protected List<AbstractCcUserDetailsAuthenticationProvider> authenticationProviders() {

        UsernamePasswordGroupsList build = HardCodedAuthenticationProvider.UsernamePasswordGroupsBuilder.getInstance()
                .createUser(1, "FirstName", "LastName", "email_address@email.com", "hashed_pw", Sets.newHashSet("Admins")).build();

        HardCodedAuthenticationProvider auth = new HardCodedAuthenticationProvider(build);

        return Collections.singletonList(auth);
    }

    @Override
    public RolesFinder rolesFinder() {
        return rolesFinder;
    }

    @Override
    public RequestMatcherContainer ignoringUrls() {
        return new RequestMatcherContainer().antPatterns("/img/**", "/css/**", "/fonts/**", "/js/**", "/manage/**");
    }

    @Override
    public SsoCookieInformation cookieInformation() {
        String domain = Strings.emptyToNull(cookieDomain);
        return new HardCodedCookieInformation("jcsw", domain);
    }

    @Override
    public MatcherRolesContainer rolesContainer() {
        return new MatcherRolesContainer().antPath("/data/**", "ADMIN").antPath("/display/**", "ADMIN").antPath("/search/**", "ADMIN");
    }

    @Override
    public SsoStorage ssoStorage() {
        return new ServletContextBasedSsoStorage(ctx);
    }

    @Override
    protected String formLoginPage() {
        return "/Login/Login.jsp";
    }

    @Override
    protected String formLoginProcessingUrl() {
        return "/Login/j_security_check";
    }

    @Override
    protected String formLoginPasswordParameter() {
        return "j_password";
    }

    @Override
    protected String formLoginUserNameParameter() {
        return "j_username";
    }

}
