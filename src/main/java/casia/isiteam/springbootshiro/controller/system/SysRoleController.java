package casia.isiteam.springbootshiro.controller.system;

import casia.isiteam.springbootshiro.constant.PermInfo;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysRolePerm;
import casia.isiteam.springbootshiro.model.vo.UpdateRolePermVo;
import casia.isiteam.springbootshiro.properties.result.HttpResult;
import casia.isiteam.springbootshiro.properties.secretkey.SecurityParameter;
import casia.isiteam.springbootshiro.service.systeam.RoleService;
import casia.isiteam.springbootshiro.util.Validator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by casia.wzy on 2018/10/28.
 */
@PermInfo(value = "系统角色模块")
@RestController
@RequestMapping("/sys_role")
public class SysRoleController {

    @Autowired
    private RoleService roleService;

    @ApiOperation(value="查询角色", notes="分页查询角色,条件有名称、排序、分页、单位。")
    @SecurityParameter(outEncode = true)
    @GetMapping("/list")
    public Object listSysRoles(String name,String sortField ,String grade, Integer page , Integer size ) throws Exception{
        JSONObject jsonObject = roleService.querRoleByPage(name,sortField,grade,page,size);
        return HttpResult.success(jsonObject);
    }

    @ApiOperation(value="查询角色权限", notes="根据角色ID查询权限")
    @SecurityParameter(outEncode = true)
    @GetMapping("/role_perms")
    public Object role_perms( Integer id ) throws Exception{
        if( !Validator.check(id)){
            return HttpResult.fail("参数不能为空！");
        }
        JSONObject jsonObject = roleService.searchRolePerms(id);
        return HttpResult.success(jsonObject);
    }

    @ApiOperation(value="新增角色", notes="新增角色")
    @SecurityParameter(inDecode = true, outEncode = true)
    @PostMapping("/addRole")
    public Object addRole(@RequestBody JSONObject jsonObject) throws Exception {
        roleService.saveRole(jsonObject);
        return HttpResult.success();
    }

    @ApiOperation(value="删除角色", notes="删除角色")
    @SecurityParameter(inDecode = true)
    @DeleteMapping("/delRole")
    public Object delRole(@RequestBody JSONObject body) throws Exception {
        roleService.deleteRole(body.getInteger("id") );
        return HttpResult.success();
    }

    @ApiOperation(value="更新角色", notes="更新角色")
    @SecurityParameter(inDecode = true)
    @PatchMapping("/updateRole")
    public Object updateRole(@RequestBody JSONObject body) throws Exception {
        roleService.flushRole(body);
        return HttpResult.success();
    }

    @ApiOperation(value="更新角色权限", notes="更新角色权限")
    @SecurityParameter(inDecode = true, outEncode = true)
    @PatchMapping("/updateRolePerms")
    public Object updateRolePerms(@RequestBody UpdateRolePermVo vo) throws Exception {
        if ( !Validator.check(vo.getId()) ) {
            return HttpResult.fail("无法更新角色的权限：参数为空（角色id）");
        }
        if ( !Validator.check( vo.getType() ) ) {
            return HttpResult.fail("无法更新角色的权限：参数为空（权限类型）");
        }
        roleService.updateRoleperm(vo);
        return HttpResult.success();
    }

    @ApiOperation(value="增加角色权限", notes="增加角色权限")
    @SecurityParameter(inDecode = true, outEncode = true)
    @PostMapping("/add_Roleperm")
    public Object addRolePerm(@RequestBody UpdateRolePermVo vo) throws Exception {
        if ( !Validator.check(vo.getId()) ) {
            return HttpResult.fail("无法增加角色的权限：参数为空（角色id）");
        }
        if ( !Validator.check( vo.getType() ) ) {
            return HttpResult.fail("无法增加角色的权限：参数为空（权限类型）");
        }
        roleService.updateRoleperm(vo);
        return HttpResult.success();
    }
    @ApiOperation(value="删除角色权限", notes="删除角色权限")
    @SecurityParameter(inDecode = true, outEncode = true)
    @DeleteMapping("/del_Roleperm")
    public Object delRoleperm(@RequestBody UpdateRolePermVo vo) throws Exception {
        if ( !Validator.check(vo.getId()) ) {
            return HttpResult.fail("无法删除角色的权限：参数为空（角色id）");
        }
        if ( !Validator.check( vo.getType() ) ) {
            return HttpResult.fail("无法删除角色的权限：参数为空（权限类型）");
        }
        roleService.delelteRoleperm(vo);
        return HttpResult.success();
    }
    @ApiOperation(value="获取自定义权限", notes="获取自定义权限")
    @SecurityParameter(outEncode = true)
    @GetMapping("/searchCustomPerm")
    public Object searchCustomPerm(Integer id,Integer type) throws Exception {
        if ( !Validator.check(id) ) {
            return HttpResult.fail("参数ID不能为空!");
        }
        if ( !Validator.check(type ) ) {
            return HttpResult.fail("参数类型不能为空!");
        }
        JSONObject jsonObject = roleService.getCustom(type,id);
        return HttpResult.success(jsonObject);
    }
    @ApiOperation(value="开启关闭自定义权限", notes="开启关闭自定义权限")
    @SecurityParameter(inDecode = true, outEncode = true)
    @PostMapping("/openCustomPerm")
    public Object openCustomPerm(@RequestBody JSONObject object ) throws Exception {
        if ( !object.containsKey("id") || !object.containsKey("type" ) || !object.containsKey("isOpen" ) ) {
            return HttpResult.fail("参数不能为空!");
        }
        JSONObject jsonObject = roleService.openCustom(object.getInteger("type"),object.getInteger("id"),object.getInteger("roleId"),object.getBoolean("isOpen"));
        return HttpResult.success(jsonObject);
    }
    @ApiOperation(value="更新自定义权限", notes="更新自定义权限")
    @SecurityParameter(inDecode = true, outEncode = true)
    @PostMapping("/updateCustomPerm")
    public Object updateCustomPerm(@RequestBody JSONObject jsonParam ) throws Exception {
        List<SysRolePerm> sysInstitution = JSON.parseArray(jsonParam.getString("sysRolePermList"), SysRolePerm.class);
        roleService.updateCustomPerm(sysInstitution,jsonParam.getInteger("roleId"));
        return HttpResult.success();
    }
}
