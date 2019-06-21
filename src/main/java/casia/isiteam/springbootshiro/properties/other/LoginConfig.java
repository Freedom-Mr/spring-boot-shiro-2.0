package casia.isiteam.springbootshiro.properties.other;

import casia.isiteam.springbootshiro.properties.result.HttpState;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Author wzy
 * Date 2018/1/16 10:17
 */
@Component
@ConfigurationProperties(prefix = "login-config")
public class LoginConfig {
    /**
     * 是否保存用户登录IP,默认false
     */
    private boolean loginIpSave = false;
    /**
     * 会话时效--ms,默认是30分钟，设置负数表示永不过期
     */
    private long sessionTimeout = 30;
    /**
     * 限制登录
     */
    private Map<String,Object> preventImpact = new HashMap<>();

    public boolean isLoginIpSave() {
        return loginIpSave;
    }

    public void setLoginIpSave(boolean loginIpSave) {
        this.loginIpSave = loginIpSave;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Map<String, Object> getPreventImpact() {
        return preventImpact;
    }

    public void setPreventImpact(Map<String, Object> preventImpact) {
        this.preventImpact = preventImpact;
    }
    @Bean
    public long sessionOutTime(){
        return  getSessionTimeout()*60000;
    }
    @Bean
    public Map<String,Object> preventImpact(){
        this.preventImpact.put(HttpState.PREVENTIMPACT_SWITCH.reasonPhrase(),this.preventImpact.get(HttpState.PREVENTIMPACT_SWITCH.reasonPhrase()) == null ? HttpState.PREVENTIMPACT_SWITCH.reasonPhrase() : this.preventImpact.get(HttpState.PREVENTIMPACT_SWITCH.reasonPhrase()));
        this.preventImpact.put(HttpState.PREVENTIMPACT_NUMBER.reasonPhrase(),this.preventImpact.get(HttpState.PREVENTIMPACT_NUMBER.reasonPhrase()) == null ? HttpState.PREVENTIMPACT_NUMBER.reasonPhrase() : this.preventImpact.get(HttpState.PREVENTIMPACT_NUMBER.reasonPhrase()));
        this.preventImpact.put(HttpState.PREVENTIMPACT_OUTTIME.reasonPhrase(),this.preventImpact.get(HttpState.PREVENTIMPACT_OUTTIME.reasonPhrase()) == null ? HttpState.PREVENTIMPACT_OUTTIME.reasonPhrase() : this.preventImpact.get(HttpState.PREVENTIMPACT_OUTTIME.reasonPhrase()));
        return this.preventImpact;
    }
}
