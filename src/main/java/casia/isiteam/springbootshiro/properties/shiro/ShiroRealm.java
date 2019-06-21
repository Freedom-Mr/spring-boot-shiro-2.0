package casia.isiteam.springbootshiro.properties.shiro;


import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysRole;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysUser;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysWhiteList;
import casia.isiteam.springbootshiro.model.vo.AuthVo;
import casia.isiteam.springbootshiro.service.systeam.RoleService;
import casia.isiteam.springbootshiro.service.systeam.UserInfoService;
import casia.isiteam.springbootshiro.util.Validator;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.HostUnauthorizedException;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Author wzy
 * Date 2017/7/30 20:22
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RoleService roleService;
    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) throws AuthenticationException{
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        SysUser userInfo = (SysUser) getAvailablePrincipal(principalCollection);
        authorizationInfo.setRoles( userInfo.getRoles().stream().map(AuthVo::getVal).collect(Collectors.toSet()) );
        authorizationInfo.setStringPermissions( userInfo.getPerms().stream().map(AuthVo::getVal).collect(Collectors.toSet()) );
//        authorizationInfo.setObjectPermissions( new Set );
        return authorizationInfo;
    }

    /**
     * 登陆认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        SysUser user = userInfoService.findByUsername(token.getUsername());

        if (user == null) {
            throw new UnknownAccountException();
        }else if ( user.getStatus() == 0 ){
            throw new DisabledAccountException();//禁止登陆
        }else if ( user.getDeadline() != null && user.getDeadline().getTime()< System.currentTimeMillis() ){
            throw new LockedAccountException();//已锁定（过期）
        }

        //IP限制
        List<SysWhiteList> whiteLists = userInfoService.searchUserWhiteList(user.getId());
        if( Validator.check(whiteLists) ){
           if( !whiteLists.stream().filter(s-> s.getIp().equals(token.getHost())).findFirst().isPresent() ){
               throw new AccountException();
           }
        }

        Set<AuthVo> permissions = new HashSet<>();
        Set<AuthVo> roles = new HashSet<>();

        // 查询权限及其角色
        SysRole sysRole = roleService.searchRoleParm(user);
        if( Validator.check(user) && Validator.check(sysRole)){
            roles.add( new AuthVo( sysRole.getName(), user.getPart()+"*"+user.getInstId()+"*"+user.getDepId()  ) );
            Set<AuthVo> pers =sysRole.getPerms().stream().map(ss-> new AuthVo(ss.getName(),ss.getVal()) ).collect(Collectors.toSet());
            permissions.addAll(pers);
        }
        user.setPerms(permissions);
        user.setRoles(roles);

        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，可以自定义实现
        return new SimpleAuthenticationInfo(user, // 用户
                user.getPassword(), // 密码
                ByteSource.Util.bytes(user.getPassword()),
                getName() // realm name
        );
    }
}
