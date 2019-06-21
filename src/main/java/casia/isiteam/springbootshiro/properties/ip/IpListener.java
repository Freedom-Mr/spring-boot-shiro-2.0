package casia.isiteam.springbootshiro.properties.ip;

import casia.isiteam.springbootshiro.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by casia.wzy on 2019/1/31
 * IP监听器
 */
@Component
@ConfigurationProperties(prefix = "sys-security-ip")
@WebListener
public class IpListener implements ServletContextListener {
    private Logger logger = LoggerFactory.getLogger(IpListener.class);
    /**
     * 是否（单位：ms）
     */
    private boolean ip_filter_open;
    /**
     * 限制时间（单位：s）
     */
    private long limited_time;
    /**
     * 用户连续访问最高阀值，超过该值则认定为恶意操作的IP，进行限制
     */
    private int limit_number;
    /**
     * 用户访问最小安全时间，在该时间内如果访问次数大于阀值，则记录为恶意IP，否则视为正常访问（单位：s）
     */
    private int min_safe_time;
    /**
     * 启动注入自定义IP黑名单
     */
    private String ip_blacklist;

    public boolean isIp_filter_open() {
        return ip_filter_open;
    }

    public void setIp_filter_open(boolean ip_filter_open) {
        this.ip_filter_open = ip_filter_open;
    }

    public long getLimited_time() {
        return limited_time;
    }

    public void setLimited_time(long limited_time) {
        this.limited_time = limited_time;
    }

    public int getLimit_number() {
        return limit_number;
    }

    public void setLimit_number(int limit_number) {
        this.limit_number = limit_number;
    }

    public int getMin_safe_time() {
        return min_safe_time;
    }

    public void setMin_safe_time(int min_safe_time) {
        this.min_safe_time = min_safe_time;
    }

    public String getIp_blacklist() {
        return ip_blacklist;
    }

    public void setIp_blacklist(String ip_blacklist) {
        this.ip_blacklist = ip_blacklist;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("恶意访问安全组{}！",isIp_filter_open()?"开启":"关闭" );
        if( isIp_filter_open() ){
            logger.info("IpListener 开始初始化 ......");
            ServletContext context = sce.getServletContext();

            // IP存储器
            Map<String, Long[]> IpStoreMap = new HashMap<String, Long[]>();
            context.setAttribute("IpStoreMap", IpStoreMap);

            // 限制IP存储器：存储被限制的IP信息
            Map<String, Long> limitedIpMap = new HashMap<String, Long>();
            context.setAttribute("limitedIpMap", limitedIpMap);

            // 注入自定义IP黑名单
            List<String> IpBlacklist = new ArrayList<>();
            if( Validator.check( getIp_blacklist() ) ){
               String[] iplist = getIp_blacklist().trim().split(";|；");
               for ( int i=0; i<iplist.length; i++ ){
                   IpBlacklist.add(iplist[i]);
               }
            }
            context.setAttribute("IpBlacklist", IpBlacklist);

            logger.info("IpBlacklist：" + IpBlacklist.toString()+" 初始化成功！");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub
    }
}
