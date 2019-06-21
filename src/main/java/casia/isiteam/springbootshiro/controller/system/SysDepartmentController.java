package casia.isiteam.springbootshiro.controller.system;

import casia.isiteam.springbootshiro.constant.PermInfo;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysDepartment;
import casia.isiteam.springbootshiro.properties.result.HttpResult;
import casia.isiteam.springbootshiro.properties.secretkey.SecurityParameter;
import casia.isiteam.springbootshiro.service.systeam.DepartmentService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by casia.wzy on 2018/10/25.
 */
@PermInfo(value = "部门管理模块")
@RestController
@RequestMapping("/sys_department")
public class SysDepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value="查询单位", notes="分页查询单位,条件有名称、排序、分页、单位。")
    @SecurityParameter(outEncode = true)
    @GetMapping("/list")
    public Object listSysInstitution(String name,String sortField ,Integer instId, Integer page , Integer size ) throws Exception{
        JSONObject jsonObject = departmentService.querDepartmentByPage(name,sortField,instId,page,size);
        return HttpResult.success(jsonObject);
    }
    @ApiOperation(value="查询部门", notes="查询部门")
    @SecurityParameter(outEncode = true)
    @GetMapping("/searchDepartment")
    public Object searchDepartment(Integer id) throws Exception {
        SysDepartment sysDepartment = departmentService.queryDepartment(id);
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(sysDepartment));
        return HttpResult.success(json);
    }
    @ApiOperation(value="删除部门", notes="删除部门")
    @DeleteMapping("/removeDepartment")
    @SecurityParameter(inDecode = true)
    public Object removeDepartment(@RequestBody JSONObject jsonObject) throws Exception {
        departmentService.deleteDepartment(jsonObject.getInteger("id"));
        return HttpResult.success();
    }

    @ApiOperation(value="新增部门", notes="新增部门")
    @SecurityParameter(inDecode = true)
    @PostMapping("/addDepartment")
    public Object addDepartment(@RequestBody String body) throws Exception {
        SysDepartment sysDepartment = JSON.parseObject(body, SysDepartment.class);
        departmentService.saveDepartment(sysDepartment);
        return HttpResult.success();
    }
    @ApiOperation(value="编辑部门", notes="编辑部门")
    @SecurityParameter(inDecode = true)
    @PostMapping("/updateDepartment")
    public Object updateDepartment(@RequestBody String body) throws Exception {
        SysDepartment sysDepartment = JSON.parseObject(body, SysDepartment.class);
        departmentService.saveDepartment(sysDepartment);
        return HttpResult.success();
    }

}
