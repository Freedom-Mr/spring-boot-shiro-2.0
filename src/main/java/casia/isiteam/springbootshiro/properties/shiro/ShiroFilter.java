package casia.isiteam.springbootshiro.properties.shiro;

import casia.isiteam.springbootshiro.properties.ip.IpFilter;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
/**
 * Author wzy
 * Date 2017/7/30 20:10
 * shiro 配置
 */
@Configuration
public class ShiroFilter extends ShiroRedisConfig {
    @Autowired
    private ShiroCasConfig shiroCasConfig;
    @Autowired
    private IpFilter ipFilter;

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);//必须设置

        shiroFilterFactoryBean.setLoginUrl("/login/home");

//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");//未授权界面;

        // 配置不会被拦截的链接 顺序判断 authc:必须认证通过; anon:可以匿名访问
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login/blacklist", "anon");
        filterChainDefinitionMap.put("/login/userlogin", "anon");
        filterChainDefinitionMap.put("/login/userlogout", "anon");
        filterChainDefinitionMap.put("/**", "authc");
//        filterChainDefinitionMap.put("/**", "anon");

        //自定义拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        filtersMap.put("ip",ipFilter);
        shiroFilterFactoryBean.setFilters(filtersMap);


        //添加cas规则
//        filterChainDefinitionMap.put(shiroCasConfig.casFilterUrlPattern, "casFilter");
        // 放行 swagger
//        filterChainDefinitionMap.put("/swagger-ui.html","anon");
//      		filterChainDefinitionMap.put("/swagger/**","anon");
//      		filterChainDefinitionMap.put("/webjars/**", "anon");
//      		filterChainDefinitionMap.put("/swagger-resources/**","anon");
//      		filterChainDefinitionMap.put("/v2/**","anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 创建缓存
     * @return
     */
    @Bean
    public CacheManager getEhCacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    @Bean
    public SecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm);
        // 自定义缓存实现
        securityManager.setCacheManager(getEhCacheManager());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        //CAS
//        securityManager.setSubjectFactory(new CasSubjectFactory());
//        //注入记住我管理器;
//        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public ShiroRealm Realm() {
        ShiroRealm shiroRealm = new ShiroRealm();
//        shiroRealm.setCredentialsMatcher(new HashedCredentialsMatcher("md5"));//密码加密方式
        shiroRealm.setCredentialsMatcher(new ShiroPwdCustom(new MemoryConstrainedCacheManager()));
        return shiroRealm;
    }


    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager) {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        bean.setArguments(new Object[]{securityManager});
        return bean;
    }

    // Shiro生命周期处理器
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    // 启用注解
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
