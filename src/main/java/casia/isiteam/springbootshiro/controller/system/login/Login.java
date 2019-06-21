package casia.isiteam.springbootshiro.controller.system.login;

import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysRole;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysUser;
import casia.isiteam.springbootshiro.model.vo.EventListVo;
import casia.isiteam.springbootshiro.properties.other.LoginConfig;
import casia.isiteam.springbootshiro.properties.result.HttpState;
import casia.isiteam.springbootshiro.properties.result.HttpResult;
import casia.isiteam.springbootshiro.properties.secretkey.SecurityParameter;
import casia.isiteam.springbootshiro.properties.shiro.ShiroPwdCustom;
import casia.isiteam.springbootshiro.service.systeam.RoleService;
import casia.isiteam.springbootshiro.service.systeam.UserInfoService;
import casia.isiteam.springbootshiro.util.IpUtil;
import casia.isiteam.springbootshiro.util.TimeUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.perms;
import static org.apache.shiro.web.filter.mgt.DefaultFilter.user;

/**
 * Author wzy
 * Date 2017/7/27 21:07
 */
@RestController
@RequestMapping(value = "login" )
public class Login {
    private final static Logger logger = LoggerFactory.getLogger(Login.class);
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private LoginConfig config;
    @Autowired
    private SessionDAO sessionDAO;

    @RequestMapping(value = "blacklist")
    public Object blacklist() throws Exception{
        return HttpResult.error( HttpState.IP_BLACkLIST.code(),HttpState.IP_BLACkLIST.annotation() );
    }

    @RequestMapping(value = "home",method={ RequestMethod.GET } )
    public Object home() throws Exception{
        Subject subject = SecurityUtils.getSubject();
        if ( !subject.isAuthenticated() /**登录认证是否成功(敏感信息)**/
            /* && !subject.isRemembered()*/ /**部分认证信息是否成功(非敏感信息)**/){
            return HttpResult.error( HttpState.HOME.code() ,HttpState.HOME.annotation() ,null );
        }
        return HttpResult.success();
    }

    /**
     * 登出
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value="/userlogout" ,method={RequestMethod.POST, RequestMethod.GET})
    public Object logout(RedirectAttributes redirectAttributes ){
        try {
            //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
            Subject subject = SecurityUtils.getSubject();
            SysUser sysUser = (SysUser)subject.getPrincipal();
            logger.info("用户[{}]登出！",sysUser.getUsername());
            subject.logout();
            redirectAttributes.addFlashAttribute("message", "您已安全退出");

        }catch (Exception e){
            logger.error("非法登出");
            return HttpResult.success("非法请求！");
        }
        return HttpResult.success( redirectAttributes.getFlashAttributes());
    }
    /**
     * 登录
     * @param request
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @SecurityParameter(inDecode = true,outEncode = true)
    @ApiOperation(value="Login", notes="Login")
    @ApiImplicitParams({@ApiImplicitParam (name = "account", value = "账号", required = true, dataType = "String"),
            @ApiImplicitParam (name = "password", value = "密码", required = true, dataType = "String")} )
    @RequestMapping(value = "userlogin",method={RequestMethod.POST})
    public Object login(HttpServletRequest request, HttpServletResponse response, @RequestBody JSONObject jsonObject) throws Exception{
        if( !jsonObject.containsKey("username") || StringUtils.isEmpty(jsonObject.getString("username")) ||
                !jsonObject.containsKey("password")  || StringUtils.isEmpty(jsonObject.getString("password")) ){
            return HttpResult.error( HttpState.NOT_EMPTY.code(),HttpState.NOT_EMPTY.annotation() );
        }
        String account = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String logIp = IpUtil.LoginIP( request, account );
        logger.info("用户["+account+"]正在尝试登录，来源IP："+logIp+" !");

        Subject subject = SecurityUtils.getSubject();
        if( subject.isAuthenticated() ){
            SysUser sysUser = userInfoService.findByUsername( account );
            if( sysUser == null ){
                logger.info("用户[" + account + "]已登录会话与数据库信息不匹配，准备登出会话，请重新登录!");
                SecurityUtils.getSubject().logout();
                return HttpResult.error( HttpState.HOME.code(), "用户[" + account + "]已登录会话与数据库信息不匹配，准备登出会话，请重新登录!"  );
            }
            logger.info("用户[" + account + "]已登录!");
            SysRole sysRole = roleService.searchRoleParm(sysUser);
            JSONObject rs =  Format.formatUserInfo( sysUser,sysRole );
            rs.put("token", UUID.randomUUID().toString() );
            return HttpResult.success("您已登录，请勿重复登录！", rs );
        }
        UsernamePasswordToken token  = new UsernamePasswordToken(account, ShiroPwdCustom.encrypt(password) ,false ,logIp );

        try {
            subject.login(token);
            Session session = subject.getSession();
            session.setAttribute("subject",user);//放入会话信息
            session.setAttribute("account",account);//放入账号
            session.setTimeout( config.sessionOutTime());
            SysUser user = (SysUser)subject.getPrincipal();

            if( config.isLoginIpSave() ){
                userInfoService.saveIp( logIp , user.getId() );
            }

            // 剔除此账号在其它地方登录
            if( user.getUniqueLogin() > 0 ){
                removeLoginedSession( user.getUniqueLogin() );
            }

            logger.info("用户[" + account + "]登录成功!");
            SysRole sysRole = roleService.searchRoleParm(user);
            JSONObject rs = Format.formatUserInfo(user,sysRole);
            rs.put("token", UUID.randomUUID().toString() );
            return HttpResult.success( rs );
        }catch(UnknownAccountException uae){
            logger.error(HttpState.UNKNOWN_ACCOUNT.annotation(account));
            return   HttpResult.error(HttpState.UNKNOWN_ACCOUNT.code() , HttpState.UNKNOWN_ACCOUNT.annotation(account));
        }catch(IncorrectCredentialsException ice){
            logger.error(HttpState.PASSWORD_ERROR.annotation(account,ice.getMessage()));
            return   HttpResult.error(HttpState.PASSWORD_ERROR.code(), HttpState.PASSWORD_ERROR.annotation(account,"您还有"+ ice.getMessage()+"次机会! "));
        }catch(LockedAccountException lae){
            logger.error(HttpState.LOCKED_ACCOUNT.annotation(account));
            return   HttpResult.error(HttpState.LOCKED_ACCOUNT.code(), HttpState.LOCKED_ACCOUNT.annotation(account) );
        }catch(ExcessiveAttemptsException eae){
            logger.error(HttpState.EXCESSIVE_ATTEMPTS.annotation(account));
            return   HttpResult.error(HttpState.EXCESSIVE_ATTEMPTS.code(), HttpState.EXCESSIVE_ATTEMPTS.annotation(account) );
        }catch(DisabledAccountException sae){
            logger.error(HttpState.DISABLED_ACCOUNT.annotation(account));
            return   HttpResult.error(HttpState.DISABLED_ACCOUNT.code(), HttpState.DISABLED_ACCOUNT.annotation(account) );
        }catch(AccountException hae){
            logger.error(HttpState.IP_SECURITY.annotation(account));
            return   HttpResult.error(HttpState.IP_SECURITY.code(), HttpState.IP_SECURITY.annotation(account) );
        } catch(AuthenticationException ae){
            logger.error(HttpState.LOGIN_ERROR.annotation(account));
            return   HttpResult.error(HttpState.DISABLED_ACCOUNT.code(), HttpState.LOGIN_ERROR.annotation(account) );
        }
    }

    /**
     * 遍历同一个账户的session,剔除限制session(redis时会有问题)
     */
    private void removeLoginedSession( int uniqueLogin ) {
        Collection<Session> list = sessionDAO.getActiveSessions();

        if( list.size() > uniqueLogin  ){
            int num = 0;
            for (Session session : list) {
                if ( (list.size() - uniqueLogin) - num  == 0  ){return;}
                if ( (!StringUtils.isEmpty( session.getAttribute("account") ) && !session.getAttribute("account").equals( SecurityUtils.getSubject().getSession().getAttribute("account") ) )
                        || SecurityUtils.getSubject().getSession().getId().equals( session.getId() ) ){ continue; }
                num++;
//                sessionDAO.delete(session);
                session.setTimeout(0);
                session.stop();
            }
        }
    }

    /**
     * 定时任务测试
     *
     */
    @Configuration
    @EnableScheduling
    public class SchedulingConfig {
        @Scheduled(cron = "0/60 * * * * ?") // 每60秒执行一次
        public void scheduler() {
            System.out.println(">>>>>>>>> "+ TimeUtil.sdf_1.format(new Date()));
        }
    }
}
