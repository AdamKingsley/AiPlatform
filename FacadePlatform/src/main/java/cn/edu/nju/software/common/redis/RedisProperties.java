package cn.edu.nju.software.common.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by mengf on 2018/4/9 0009.
 */

@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {

    private int database;

    private String host ;

    private String password;

    private int port;

    private int timeout;

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
