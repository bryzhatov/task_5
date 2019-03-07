package ua.griddynamics.geekshop.util;

import ua.griddynamics.httpserver.api.config.HttpServerConfig;

import java.util.Properties;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-03-07
 */
public class AntonConfigAdapter {
    public static HttpServerConfig getConfig(Properties properties) {
        HttpServerConfig serverConfig = new HttpServerConfig();
        serverConfig.setHost(properties.getProperty("server.host"));
        serverConfig.setPort(Integer.parseInt(properties.getProperty("server.port")));
        return serverConfig;
    }
}
