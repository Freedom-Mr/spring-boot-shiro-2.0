package casia.isiteam.springbootshiro.properties.shiro;

import casia.isiteam.springbootshiro.properties.other.LoginConfig;
import casia.isiteam.springbootshiro.properties.result.HttpState;
import casia.isiteam.springbootshiro.util.EncodeUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.apache.commons.codec.binary.Base64;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author wzy
 * Date 2017/7/31 19:04
 */
public class ShiroPwdCustom  extends SimpleCredentialsMatcher {

    //集群中可能会导致出现验证多过5次的现象，因为AtomicInteger只能保证单节点并发
    private Cache<String, AtomicLong> passwordRetryCache;

    public ShiroPwdCustom(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = (String)token.getPrincipal();

        String usertime = "timeOut-"+username;
        AtomicLong retryCount = passwordRetryCache.get(username);
        AtomicLong cursorTime = passwordRetryCache.get(usertime);

        if(null == retryCount) {
            retryCount = new AtomicLong(0);
            cursorTime = new AtomicLong(-1);
            passwordRetryCache.put(username, retryCount);
            passwordRetryCache.put(usertime, cursorTime);
        }
        Long retrycount = retryCount.incrementAndGet();
        Long cursortime = cursorTime.longValue();
        if(retrycount > 5 ) {
            if( cursortime != -1 && System.currentTimeMillis() - cursortime > HttpState.PREVENTIMPACT_OUTTIME.code()*1000 ){
                passwordRetryCache.remove(username);
                passwordRetryCache.remove(usertime);
                retryCount = new AtomicLong(0);
                retrycount = 1L;
                passwordRetryCache.put(username, retryCount);
            }else{
                throw new ExcessiveAttemptsException("username: " + username + " tried to login more than 5 times in period");
            }
        }
        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            passwordRetryCache.remove(username); //clear retry data
            passwordRetryCache.remove(usertime);
        }else {
            cursorTime = new AtomicLong(System.currentTimeMillis());
            passwordRetryCache.put(usertime, cursorTime);
            throw new IncorrectCredentialsException(String.valueOf( HttpState.PREVENTIMPACT_NUMBER.code() - retrycount ));
        }
        return  matches;
    }

    //加密
    public static String encrypt(String data) {
//        String sha384Hex = new Sha384Hash(data).toBase64();//sha348
        String base64 = new Base64().encodeToString(new StringBuilder(data).reverse().toString().getBytes() ); //base64
        return base64;
    }
}
