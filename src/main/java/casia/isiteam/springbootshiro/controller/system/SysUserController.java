package casia.isiteam.springbootshiro.controller.system;

import casia.isiteam.springbootshiro.controller.system.login.Format;
import casia.isiteam.springbootshiro.controller.system.login.Login;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysRole;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysUser;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysWhiteList;
import casia.isiteam.springbootshiro.properties.other.UrlConfig;
import casia.isiteam.springbootshiro.properties.result.HttpResult;
import casia.isiteam.springbootshiro.properties.secretkey.SecurityParameter;
import casia.isiteam.springbootshiro.service.systeam.*;
import casia.isiteam.springbootshiro.util.Validator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by casia.wzy on 2018/10/11.
 */
@RestController
@RequestMapping(value = "/sys_user" )
public class SysUserController {
    private final static Logger logger = LoggerFactory.getLogger(Login.class);
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private InstitutionService institutionService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private SysIpService sysIpService;
    @Autowired
    private UrlConfig urlConfig;

    @ApiOperation(value="用户信息", notes="用户信息")
    @SecurityParameter(outEncode = true)
    @RequestMapping(value = "getUserInfo",method={ RequestMethod.GET} )
    public Object getUserInfo( Integer user_id ) throws Exception{
        int userId = 0;
        if( StringUtils.isEmpty(user_id) ){
            Subject subject = SecurityUtils.getSubject();
            SysUser user = (SysUser)subject.getPrincipal();
            userId = user.getId();
        }else{
            userId = user_id;
        }
        SysUser user = userInfoService.findByUserId(userId);
        SysRole sysRole = roleService.searchRoleParm(user);
        JSONObject jsonObject = Format.formatUserInfo(user,sysRole);
        jsonObject.put("profile_photo", userInfoService.searchImage(user.getId()) );
        jsonObject.put("dep_name",user.getDepId() == -1 ? null : departmentService.queryDepartment(user.getDepId()) .getName() );
        jsonObject.put("inst_name",user.getInstId() == -1 ? null : institutionService.querInstitutionById2(user.getInstId()) .getName() );
        return HttpResult.success(  jsonObject );
    }

    @ApiOperation(value="用户列表", notes="用户列表")
    @SecurityParameter(inDecode = true,outEncode = true)
    @PostMapping(value = "getUsers")
    public Object getUsers( @RequestBody JSONObject jsonObject ) throws Exception{
        JSONObject rs = userInfoService.selectUsers(jsonObject);
        return HttpResult.success( rs );
    }
    @ApiOperation(value="删除用户", notes="删除用户")
    @SecurityParameter(inDecode = true)
    @DeleteMapping(value = "deleteUser")
    public Object deleteUser( @RequestBody JSONObject jsonObject ) throws Exception{
        userInfoService.delUser(jsonObject.getInteger("id"));
        return HttpResult.success();
    }
    @RequiresPermissions(value = "b:user:add")
    @SecurityParameter(inDecode = true,outEncode = true)
    @ApiOperation(value="新增用户", notes="新增用户")
    @PostMapping(value = "addUser")
    public Object addUser( @RequestBody JSONObject jsonObject) throws Exception{
        Integer user_id = userInfoService.saveUser(jsonObject);
        return HttpResult.success(user_id);
    }
    @ApiOperation(value="用户修改自身非权限信息", notes="用户修改自身非权限信息")
    @PatchMapping(value = "updateMe")
    public Object updateMe( Integer userId, String realName,String telephone,String email,String note) throws Exception{
        if( !Validator.check(userId) || !Validator.check(realName) ){
            return HttpResult.fail("参数不能空！");
        }
        userInfoService.updateMe(userId,realName,telephone,email,note);
        return HttpResult.success();
    }
    @ApiOperation(value="修改用户基本信息", notes="修改用户基本信息")
    @SecurityParameter(inDecode = true)
    @PatchMapping(value = "updateUser")
    public Object updateUser( @RequestBody JSONObject jsonObject ) throws Exception{
        userInfoService.updateUser(jsonObject);
        return HttpResult.success();
    }
    @ApiOperation(value="上传用户头像", notes="")
    @RequestMapping(value = "uploadImage",method={ RequestMethod.POST })
    public Object createAnalysisTask(@RequestParam("file")MultipartFile file,Integer id) {
        if( !Validator.check(id)) {
            return HttpResult.fail("参数不能为空");
        }
        return userInfoService.saveImage(file,id);
    }
    @ApiOperation(value="查询用户头像", notes="")
    @RequestMapping(value = "searchImage",method={ RequestMethod.POST })
    public Object searchImage(Integer userId) {
        if(!Validator.check(userId) ) {
            return HttpResult.fail("参数不能为空！");
        }
        return userInfoService.searchImage(userId);
    }
    @ApiOperation(value="删除用户头像", notes="")
    @SecurityParameter(inDecode = true)
    @RequestMapping(value = "deleteImage",method={ RequestMethod.DELETE })
    public Object deleteImage(@RequestBody JSONObject jsonObject) {
        if( !jsonObject.containsKey("userId") ) {
            return HttpResult.fail("参数不能为空！");
        }
        userInfoService.deleteImage(jsonObject.getInteger("userId"));
        return HttpResult.success();
    }
    @ApiOperation(value="修改用户角色", notes="修改用户角色")
    @SecurityParameter(inDecode = true)
    @PatchMapping(value = "updateUserRole")
    public Object updateUserRole( @RequestBody JSONObject jsonObject ) throws Exception{
        userInfoService.updateUserrole(jsonObject);
        return HttpResult.success();
    }

    @PostMapping(value = "userGP")//查询密码
    @SecurityParameter(inDecode = true,outEncode = true)
    public Object userGP( @RequestBody JSONObject jsonObject ) throws Exception{
        JSONObject json = userInfoService.saerchUserPassword(jsonObject);
        return HttpResult.success(json);
    }

    @PatchMapping(value = "updatePwd")//修改密码
    @SecurityParameter(inDecode = true)
    public Object updatePwd( @RequestBody JSONObject jsonObject ) throws Exception{
        userInfoService.updateUserPassword(jsonObject);
        return HttpResult.success();
    }
    @PatchMapping(value = "uppd")//修改密码
    @SecurityParameter(inDecode = true,outEncode = true)
    public Object uppd( @RequestBody JSONObject jsonObject ) throws Exception{
        Map<Integer ,String > rs = userInfoService.updateUserPassword2(jsonObject);
        if( rs.containsKey(1) ){
            return HttpResult.success(rs.get(1),rs.get(1));
        }else  if( rs.containsKey(2) ){
            return HttpResult.fail(rs.get(2),rs.get(2));
        }
        return HttpResult.fail(null);
    }
    @ApiOperation(value="查询用户是否存在", notes="查询用户是否存在")
    @GetMapping(value = "isUserName")
    public Object isUserName( String userName ) throws Exception{
        JSONObject jsonObject =new JSONObject();
        boolean rs = true;
        if( Validator.check(userName) ){
           rs = userInfoService.searchUserName(userName);
        };
        jsonObject.put("isUser",rs);
        return HttpResult.success(jsonObject);
    }
    @ApiOperation(value="查询用户登陆折线图数据", notes="查询用户登陆折线图数据")
    @GetMapping(value = "userIP/list")
    public Object userIPList( Integer id ,Integer grading) throws Exception{
        JSONObject json = new JSONObject();
        JSONObject lint = sysIpService.findLoginInfoByUserId(id,grading);
        json.put("line",lint);
        return HttpResult.success(json);
    }
    @ApiOperation(value="查询用户登陆扇形图数据", notes="查询用户登陆扇形图数据")
    @SecurityParameter(outEncode = true)
    @GetMapping(value = "userIP/group")
    public Object userIPGroup( Integer id ,Integer grading) throws Exception{
        JSONObject json = new JSONObject();
        JSONObject pie = sysIpService.findLoginIpByUserId(id,grading);
        json.put("pie",pie);
        return HttpResult.success(json);
    }
    @ApiOperation(value="查询用户登陆IP白名单", notes="查询用户登陆IP白名单")
    @SecurityParameter(outEncode = true)
    @PostMapping(value = "white/list")
    public Object userIPGroup( Integer userId) throws Exception{
        JSONArray json = new JSONArray();
        List<SysWhiteList> list = userInfoService.searchUserWhiteList(userId);
        if( Validator.check(list) ){
            json = JSONArray.parseArray(JSON.toJSONString(list));
        }
        return HttpResult.success(json);
    }
}
