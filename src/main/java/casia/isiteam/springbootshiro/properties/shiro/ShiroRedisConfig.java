package casia.isiteam.springbootshiro.properties.shiro;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by wzy on 2018/1/30.
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "redis")
public class ShiroRedisConfig {
    private final static Logger logger = LoggerFactory.getLogger(ShiroRedisConfig.class);
    //取redis连接配置
    private String host;
    private int port;
    private String password;
    private int timeout;
    private boolean isRedisCache;
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
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

    public boolean getIsRedisCache() {
        return isRedisCache;
    }

    public void setIsRedisCache(boolean isRedisCache) {
        this.isRedisCache = isRedisCache;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 配置shiro redisManager
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(getHost());
        redisManager.setPort(getPort());
        redisManager.setPassword(getPassword());
        redisManager.setExpire(getTimeout());
        return redisManager;
    }

    @Bean
    public SessionDAO sessionDAO() {
        if (true == getIsRedisCache() ) {
            RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
            redisSessionDAO.setRedisManager(redisManager());
            logger.info("设置redisSessionDAO");
            return redisSessionDAO;
        } else {
            MemorySessionDAO sessionDAO = new MemorySessionDAO();
            logger.info("设置MemorySessionDAO");
            return sessionDAO;
        }
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
//        listeners.add(new BDSessionListener());
//        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionDAO(sessionDAO());
        return sessionManager;
    }

    /**
     * cacheManager 缓存 redis实现
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }
}
