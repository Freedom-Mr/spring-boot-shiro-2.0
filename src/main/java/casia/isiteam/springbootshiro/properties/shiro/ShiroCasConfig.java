package casia.isiteam.springbootshiro.properties.shiro;

import casia.isiteam.springbootshiro.properties.cas.SpringCasConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wzy on 2018/1/26.
 */
@Configuration
public class ShiroCasConfig {
    @Autowired
    SpringCasConfig casConfig;
    private static boolean casEnabled  = true;
    public ShiroCasConfig() {
    }

    @Bean
    public SpringCasConfig getSpringCasConfig(){
        return new SpringCasConfig();
    }


}
