package casia.isiteam.springbootshiro.controller.system.login;

import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysRole;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysUser;
import casia.isiteam.springbootshiro.model.vo.AuthVo;
import casia.isiteam.springbootshiro.util.Validator;
import com.alibaba.fastjson.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by casia.wzy on 2018/10/11.
 */
public class Format {

    /**
     * 格式化用户信息
     * @param user
     */
    public static JSONObject formatUserInfo(SysUser user, SysRole sysRole){
        JSONObject jsonObject = new JSONObject();

        if( Validator.check(sysRole) ){
            Set<AuthVo> permissions = new HashSet<>();
            Set<AuthVo> pers =sysRole.getPerms().stream().map( ss-> new AuthVo(ss.getName(),ss.getVal()) ).collect(Collectors.toSet());
            permissions.addAll(pers);
            user.setPerms(permissions);
        }
        if( Validator.check(user) && Validator.check(sysRole) ){
            Set<AuthVo> roles = new HashSet<>();
            roles.add( new AuthVo( sysRole.getName(), user.getPart()+"*"+user.getInstId()+"*"+user.getDepId()  ) );
            user.setRoles(roles);
        }
        jsonObject.put("user_id",user.getId());
        jsonObject.put("roles",JSONObject.toJSON( user.getRoles() ) );
        jsonObject.put("perms",JSONObject.toJSON( user.getPerms() ) );
        jsonObject.put("account",user.getUsername());
        jsonObject.put("real_name",user.getRealName());
        jsonObject.put("email",user.getEmail());
        jsonObject.put("part",user.getPart());
        jsonObject.put("inst_id",user.getInstId());
        jsonObject.put("dep_id",user.getDepId());
        jsonObject.put("deadline",user.getDeadline());
        jsonObject.put("note",user.getNote());
        jsonObject.put("telephone",user.getTelephone());
        jsonObject.put("unique_login",user.getUniqueLogin());
        return jsonObject;
    }
}
