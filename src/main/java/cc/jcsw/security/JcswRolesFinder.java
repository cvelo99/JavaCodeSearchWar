package cc.jcsw.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.google.common.base.Strings;

import cc.springsecurity.authentication.HostServletContext;
import cc.springsecurity.authorization.AbstractHardCodedRolesFinder;
import cc.springsecurity.authorization.RolesFinder;

/**
 * Finds the roles for the given {@link HostServletContext} .
 * 
 * @author Chris Carcel
 *
 */
@Component
public class JcswRolesFinder extends AbstractHardCodedRolesFinder implements RolesFinder, ServletContextAware {

    @Value("${cookie_domain}")
    private String cookieDomain;

    private ServletContext ctx;

    /**
     * virtual host / context --> role --> list of groups
     */
    private Map<HostServletContext, Map<String, Set<String>>> map;

    @PostConstruct
    private void setup() {

        String domain = Strings.emptyToNull(cookieDomain);

        map = new HashMap<>();

        Map<String, Set<String>> m = new TreeMap<>();
        m.put("ADMIN", Stream.of("Java Admins").collect(Collectors.toSet()));
        map.put(new HostServletContext(domain, ctx.getContextPath()), m);
    }

    @Override
    protected Map<HostServletContext, Map<String, Set<String>>> getRoleGroupMap() {
        return map;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.ctx = servletContext;
    }
}
