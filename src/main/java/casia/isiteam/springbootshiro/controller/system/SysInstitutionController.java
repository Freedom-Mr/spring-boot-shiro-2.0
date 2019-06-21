package casia.isiteam.springbootshiro.controller.system;

import casia.isiteam.springbootshiro.constant.PermInfo;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysInstitution;
import casia.isiteam.springbootshiro.properties.result.HttpResult;
import casia.isiteam.springbootshiro.properties.secretkey.SecurityParameter;
import casia.isiteam.springbootshiro.service.systeam.InstitutionService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by casia.wzy on 2018/10/24.
 * 单位管理模块
 */
@PermInfo(value = "单位管理模块")
@RestController
@RequestMapping("/sys_institution")
public class SysInstitutionController {

    @Autowired
    private InstitutionService institutionService;

    @ApiOperation(value="根据ID查询单位", notes="根据ID查询单位")
    @GetMapping("/id/list")
    @SecurityParameter(outEncode = true)
    public Object listSysInstitution(Integer id) throws Exception{
        JSONObject jsonObject = institutionService.querInstitutionById(id);
        return HttpResult.success(jsonObject);
    }

    @ApiOperation(value="查询单位", notes="分页查询单位")
    @SecurityParameter(outEncode = true)
    @GetMapping("/list")
    public Object listSysInstitution(String name,Integer page , Integer size, String sortField) throws Exception{
       JSONObject jsonObject = institutionService.querInstitutionByPage(name,page,size,sortField);
        return HttpResult.success(jsonObject);
    }

    @ApiOperation(value="删除单位", notes="删除单位")
    @SecurityParameter(inDecode = true)
    @DeleteMapping("/removeInstition")
    public Object removeInstition(@RequestBody JSONObject jsonObject) throws Exception {
        institutionService.deleteInstitution(jsonObject.getInteger("id"));
        return HttpResult.success();
    }

    @ApiOperation(value="新增单位", notes="新增单位")
    @SecurityParameter(inDecode = true)
    @PostMapping("/addInstitution")
    public Object addInstitution(@RequestBody String body) throws Exception {
        SysInstitution sysInstitution = JSON.parseObject(body, SysInstitution.class);
        institutionService.saveInstitution(sysInstitution);
        return HttpResult.success();
    }
    @ApiOperation(value="编辑单位", notes="编辑单位")
    @PostMapping("/updateIntitution")
    @SecurityParameter(inDecode = true)
    public Object updateIntitution(@RequestBody String body) throws Exception {
        SysInstitution sysInstitution = JSON.parseObject(body, SysInstitution.class);
        institutionService.saveInstitution(sysInstitution);
        return HttpResult.success();
    }
    @ApiOperation(value="查询当前用户可见的单位及其部门", notes="查询当前用户可见的单位及其部门")
    @SecurityParameter(outEncode = true)
    @GetMapping("/searchUserIntis")
    public Object searchUserIntis() throws Exception {
        JSONArray jsonArray = institutionService.queryUserIntis();
        return HttpResult.success(jsonArray);
    }
    @ApiOperation(value="查询当前用户可见的单位及其部门、用户", notes="查询当前用户可见的单位及其部门、用户")
    @SecurityParameter(outEncode = true)
    @GetMapping("/searchUserIntisDeps")
    public Object searchUserIntisDeps() throws Exception {
        JSONArray jsonArray = institutionService.queryUserIntisDeps();
        return HttpResult.success(jsonArray);
    }
}
