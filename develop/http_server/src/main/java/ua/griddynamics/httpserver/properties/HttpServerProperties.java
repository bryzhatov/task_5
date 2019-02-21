package ua.griddynamics.httpserver.properties;

import java.util.Properties;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-01-21
 */
public class HttpServerProperties extends ServerProperties {

    public HttpServerProperties(Properties httpConfig) {
        mergeOuterInInner(httpConfig);
        new DefaultServerProperties().mergeInnerInOuter(innerProperty);
    }
}
