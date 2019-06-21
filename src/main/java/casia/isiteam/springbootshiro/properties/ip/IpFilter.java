package casia.isiteam.springbootshiro.properties.ip;

import casia.isiteam.springbootshiro.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by casia.wzy on 2019/1/31
 * 自定义IP过滤器
 * 判断请求方的IP在单位时间内访问次数到达设定阈值
 * 如果违反规定将限制IP访问，N小时后自动解除限制
 */
@Configuration
@WebFilter(urlPatterns="/*")
public class IpFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(IpFilter.class);

    @Autowired
    private IpListener ipListener;
    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) {
        this.config = filterConfig;    //设置属性filterConfig
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if( ipListener.isIp_filter_open() ){
            String ip = request.getRemoteHost();
            ServletContext context = config.getServletContext();

            if( !Validator.check(ip) ){
                return;
            }

            //自定义IP黑名单
            List<String> IpBlacklist =  ( List<String> ) context.getAttribute("IpBlacklist");
            if( IpBlacklist.contains(ip) ){
                logger.error("当前访问IP已列入黑名单=>：{}" , ip);
                request.getRequestDispatcher("/login/blacklist").forward(request, response);
                return;
            }

            Map<String, Long> limitedIpMap = (Map<String, Long>) context.getAttribute("limitedIpMap");
            filterLimitedIpMap(limitedIpMap);

            // 判断是否是动态被限制的IP，如果是则跳转到访问限制提示
            if ( limitedIpMap.containsKey(ip) ) {
                long limitedTime = limitedIpMap.get(ip) - System.currentTimeMillis();
                request.setAttribute("remainingTime", ((limitedTime / 1000) + (limitedTime % 1000 > 0 ? 1 : 0)));
                logger.error("IP访问过于频繁，进入限制访问阶段=>：{} , 解禁剩余时间{}s ！" , ip ,limitedTime/1000 );
                request.getRequestDispatcher("/login/blacklist").forward(request, response);
                return;
            }

            // 获取动态IP存储器
            Map<String, Long[]> IpStoreMap = (Map<String, Long[]>) context.getAttribute("IpStoreMap");
            // 判断存储器中是否存在当前IP，如果没有则为初次访问，初始化该ip
            // 如果存在当前ip，则验证当前ip的访问次数
            // 如果大于限制阀值，判断达到阀值的时间，如果不大于[用户访问最小安全时间]则视为恶意访问，跳转到访问限制提示
            if ( IpStoreMap.containsKey(ip) ) {
                Long[] ipInfo = IpStoreMap.get(ip);
                ipInfo[0] = ipInfo[0] + 1;
                if (ipInfo[0] > ipListener.getLimit_number()) {
                    Long ipAccessTime = ipInfo[1];
                    Long currentTimeMillis = System.currentTimeMillis();
                    if ( currentTimeMillis - ipAccessTime <= ipListener.getMin_safe_time() * 1000 ) {
                        limitedIpMap.put(ip, currentTimeMillis + ipListener.getLimited_time() * 1000 );
                        IpStoreMap.remove(ip);
                        request.setAttribute("remainingTime", (( ipListener.getLimited_time() > 0 ? 1 : 0)));
                        logger.error("IP访问过于频繁，进入限制访问阶段=>：{}" , ip);
                        request.getRequestDispatcher("/login/blacklist").forward(request, response);
                        return;
                    } else {
                        initIpVisitsNumber(IpStoreMap, ip);
                    }
                }
            } else {
                initIpVisitsNumber(IpStoreMap, ip);
            }
            context.setAttribute("IpStoreMap", IpStoreMap);
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @param limitedIpMap
     * @Description 过滤受限的IP，剔除已经到期的限制IP
     */
    private void filterLimitedIpMap(Map<String, Long> limitedIpMap) {
        if ( !Validator.check(limitedIpMap ) ) {
            return;
        }
        Set<String> keys = limitedIpMap.keySet();
        Iterator<String> keyIt = keys.iterator();
        long currentTimeMillis = System.currentTimeMillis();
        while (keyIt.hasNext()) {
            long expireTimeMillis = limitedIpMap.get(keyIt.next());
            if (expireTimeMillis <= currentTimeMillis) {
                keyIt.remove();
            }
        }
    }
    /**
     * 初始化用户访问次数和访问时间
     *
     * @param ipMap
     * @param ip
     */
    private void initIpVisitsNumber(Map<String, Long[]> ipMap, String ip) {
        Long[] ipInfo = new Long[2];
        ipInfo[0] = 0L;// 访问次数
        ipInfo[1] = System.currentTimeMillis();// 初次访问时间
        ipMap.put(ip, ipInfo);
    }
}
