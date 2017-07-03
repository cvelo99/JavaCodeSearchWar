package cc.jcsw.security;

import javax.servlet.ServletContext;

import cc.springsecurity.config.utils.HostDbServiceProperties;

public class JcswHostDbServiceProperties implements HostDbServiceProperties {

    private static final String SERVICE = "-default-";

    @Override
    public String getServiceForHost() {
        return SERVICE;
    }

    @Override
    public String getServiceForHost(String host) {
        return SERVICE;
    }

    @Override
    public String getServiceForHost(String host, ServletContext context) {
        return SERVICE;
    }

}
