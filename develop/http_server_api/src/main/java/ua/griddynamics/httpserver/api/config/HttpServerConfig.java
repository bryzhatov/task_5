package ua.griddynamics.httpserver.api.config;

import lombok.Data;

import java.util.Properties;

/**
 * @author Dmitry Bryzhatov
 * @since 2019-02-12
 */
@Data
public class HttpServerConfig extends Properties {

    public HttpServerConfig() {
    }

    public HttpServerConfig(Properties properties) {
        putAll(properties);
    }

    public void setHost(String host) {
        this.setProperty(ServerKeysConfig.HOST, host);
    }

    public void setPort(int port) {
        this.setProperty(ServerKeysConfig.PORT, Integer.toString(port));
    }

    public void setVisibleRequest(boolean visibleRequest) {
        this.setProperty(ServerKeysConfig.VISIBLE_REQUEST, String.valueOf(visibleRequest));
    }

    public void setKeepAlive(boolean keepAlive) {
        this.setProperty(ServerKeysConfig.KEEP_ALIVE, String.valueOf(keepAlive));
    }

    /**
     * @param keepAliveTimeOut is time in second
     */
    public void setKeepAliveTimeOut(int keepAliveTimeOut) {
        this.setProperty(ServerKeysConfig.KEEP_ALIVE_TIME, String.valueOf(keepAliveTimeOut));
    }

    public void setKeepAliveTransactional(int countTransactional) {
        this.setProperty(ServerKeysConfig.KEEP_ALIVE_TRANSACTIONAL, String.valueOf(countTransactional));
    }

    public void setEmptyRequestTimeOut(int emptyRequestTimeOut) {
        this.setProperty(ServerKeysConfig.EMPTY_REQUEST_TIME, String.valueOf(emptyRequestTimeOut));
    }

    public void setPoolType(String poolType) {
        this.setProperty(ServerKeysConfig.POOL_TYPE, poolType);
    }

    // Request Pool

    public void setCoreRequestPoolSize(int coreRequestPoolSize) {
        this.setProperty(ServerKeysConfig.CORE_REQUEST_POOL_SIZE, String.valueOf(coreRequestPoolSize));
    }

    public void setMaxRequestPoolSize(int maxRequestPoolSize) {
        this.setProperty(ServerKeysConfig.MAX_REQUEST_POOL_SIZE, String.valueOf(maxRequestPoolSize));
    }

    public void setTimeIdleRequestPool(int timeIdleRequestPool) {
        this.setProperty(ServerKeysConfig.TIME_IDLE_REQUEST_POOL, String.valueOf(timeIdleRequestPool));
    }

    // KeepAlive Pool

    public void setCoreKeepAlivePoolSize(int coreKeepAlivePoolSize) {
        this.setProperty(ServerKeysConfig.CORE_KEEP_ALIVE_POOL_SIZE, String.valueOf(coreKeepAlivePoolSize));
    }

    public void setMaxKeepAlivePoolSize(int maxKeepAlivePoolSize) {
        this.setProperty(ServerKeysConfig.MAX_KEEP_ALIVE_POOL_SIZE, String.valueOf(maxKeepAlivePoolSize));
    }

    public void setTimeIdleKeepAlivePool(int timeIdleKeepAlivePool) {
        this.setProperty(ServerKeysConfig.TIME_IDLE_KEEP_ALIVE_POOL, String.valueOf(timeIdleKeepAlivePool));
    }
}
