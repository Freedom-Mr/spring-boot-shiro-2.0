package casia.isiteam.springbootshiro.controller.system;

import casia.isiteam.springbootshiro.constant.PermInfo;
import casia.isiteam.springbootshiro.constant.PermType;
import casia.isiteam.springbootshiro.controller.system.login.Login;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysPerm;
import casia.isiteam.springbootshiro.properties.result.HttpResult;
import casia.isiteam.springbootshiro.properties.secretkey.SecurityParameter;
import casia.isiteam.springbootshiro.service.systeam.PermService;
import casia.isiteam.springbootshiro.util.Validator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by casia.wzy on 2018/10/16.
 * 系统权限模块
 */
//@RequiresPermissions("a:sys:perm")
@PermInfo(value = "系统权限模块")
@RestController
@RequestMapping("/sys_perm")
public class SysPermController {
    private final static Logger logger = LoggerFactory.getLogger(Login.class);

    @Autowired
    private PermService permService;

    @ApiOperation(value="查询所有权限", notes="查询所有权限")
    @SecurityParameter(outEncode = true)
    @GetMapping("/list/all")
    public Object listAllPermisson() throws Exception{
        List<SysPerm> list = permService.querAllPerm();
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        if ( Validator.check(list) ){
            Map<Integer, List<SysPerm>> permMap = list.stream().collect(Collectors.groupingBy(SysPerm::getType));
            List<SysPerm> buttonPermList = permMap.get(PermType.BUTTON); // 按钮权限值
            Map<String, List<SysPerm>> buttonsGroupedByParent = new HashMap<>();
            if( Validator.check(buttonPermList) ){
                buttonsGroupedByParent = buttonPermList.stream().collect(Collectors.groupingBy(SysPerm::getParent));
            }

            permMap.forEach( (k,v)->{
                JSONArray jsonArray1 = new JSONArray();
                v.forEach( s->{
                    JSONObject jsonObject1_1 = new JSONObject();
                    jsonObject1_1.put("val",s.getVal());
                    jsonObject1_1.put("parent",s.getParent());
                    jsonObject1_1.put("name",s.getName());
                    jsonObject1_1.put("type",s.getType());
                    jsonObject1_1.put("leaf",s.getLeaf());
                    jsonArray1.add(jsonObject1_1);
                });
                jsonObject1.put( k+"" ,jsonArray1);
            } );

            buttonsGroupedByParent.forEach( (k,v)->{
                JSONArray jsonArray2 = new JSONArray();
                v.forEach( s->{
                    JSONObject jsonObject2_1 = new JSONObject();
                    jsonObject2_1.put("val",s.getVal());
                    jsonObject2_1.put("parent",s.getParent());
                    jsonObject2_1.put("name",s.getName());
                    jsonObject2_1.put("type",s.getType());
                    jsonObject2_1.put("leaf",s.getLeaf());
                    jsonArray2.add(jsonObject2_1);
                });
                jsonObject2.put( k+"" ,jsonArray2);
            } );
            jsonObject.put("permMap",jsonObject1);
            jsonObject.put("btnPermMap",jsonObject2);
            return HttpResult.success(jsonObject);
        }
        jsonObject.put("permMap",jsonObject1);
        jsonObject.put("btnPermMap",jsonObject2);
        return HttpResult.success();
    }

    @ApiOperation(value="同步菜单权限", notes="同步菜单权限")
    @SecurityParameter(inDecode = true,outEncode = true)
    @PostMapping("/sync/menu")
    public Object syncMenuPermission(@RequestBody String body) throws Exception{
        List<SysPerm> notSyncedPerms = JSONObject.parseArray(body, SysPerm.class);
        if ( Validator.check(notSyncedPerms) ){
            permService.updatePerm(notSyncedPerms);
        }
        return HttpResult.success();
    }
    @ApiOperation(value="增加权限", notes="增加权限")
    @SecurityParameter(inDecode = true,outEncode = true)
    @PostMapping("/add_perm")
    public Object addPerm(@RequestBody String body){
        SysPerm perm = JSON.parseObject(body, SysPerm.class);
        if ( !Validator.check(perm.getVal() ) ) {
            return HttpResult.fail("权限值不能为空!");
        }
        SysPerm is_perm =  permService.findByVal( perm.getVal() );
        if( Validator.check( is_perm ) ){
            String msg = "权限值已存在: " + is_perm.getName() + "（" + is_perm.getVal() + "）!";
            return HttpResult.fail(msg);
        }
        perm.setCreateTime(new Date());
        perm.setUpdateTime(new Date());
        SysPerm save_perm =  permService.savePerm(perm);
        if( Validator.check( save_perm ) ){
            return HttpResult.success("保存成功!");
        }else {
            return HttpResult.fail("保存失败!");
        }
    }
    @ApiOperation(value="更新权限", notes="更新权限")
    @SecurityParameter(inDecode = true,outEncode = true)
    @PatchMapping("/update_perm")
    public Object updatePerm(@RequestBody String body){
        SysPerm perm = JSON.parseObject(body, SysPerm.class);

        if ( !Validator.check(perm.getVal() ) ) {
            return HttpResult.fail("权限值不能为空!");
        }
        SysPerm is_perm =  permService.findByVal( perm.getVal() );
        if( !Validator.check( is_perm ) ){
            String msg = "权限值不存在!";
            return HttpResult.fail(msg);
        }
        perm.setCreateTime(is_perm.getCreateTime());
        perm.setUpdateTime(new Date());
        SysPerm save_perm =  permService.savePerm(perm);
        if( Validator.check( save_perm ) ){
            return HttpResult.success("更新成功!");
        }else {
            return HttpResult.fail("更新失败!");
        }
    }
    @ApiOperation(value="删除权限", notes="删除权限")
    @SecurityParameter(inDecode = true,outEncode = true)
    @DeleteMapping("/del_perm")
    public Object delPerm(@RequestBody String body){
        SysPerm perm = JSON.parseObject(body, SysPerm.class);
        if ( !Validator.check(perm.getVal() ) ) {
            return HttpResult.fail("权限值不能为空!");
        }
        long delete_count =  permService.deletePermByVal( perm.getVal() );
        return HttpResult.success("删除成功!");
    }

    @ApiOperation(value="同步接口权限", notes="同步接口权限")
    @SecurityParameter(inDecode = true,outEncode = true)
    @PostMapping("/sync/api")
    public Object syncPermApi(@RequestBody String body){
        List<SysPerm> perm = JSONArray.parseArray(body, SysPerm.class);
        if( !Validator.check(perm) ){
            HttpResult.success("未有接口权限可以同步!") ;}
        permService.savePerm(perm);
        return HttpResult.success("接口权限同步成功!");
    }

    @Autowired
    private ApplicationContext context;
    @ApiOperation(value="查询接口权限元数据(暂未开放)", notes="查询接口权限元数据(暂未开放)")
    @SecurityParameter(outEncode = true)
    @GetMapping("/meta/api")
    public Object metaApi(){
        final String basicPackage = ClassUtils.getPackageName(this.getClass());
        Map<String, Object> map = context.getBeansWithAnnotation(Controller.class);
        Collection<Object> beans = map.values();
        List<SysPerm> apiList = beans.stream().filter(b -> org.apache.commons.lang3.StringUtils.equals(basicPackage, ClassUtils.getPackageName(b.getClass())))
                .map(bean -> {
                    Class<?> clz = bean.getClass();
                    SysPerm moduleApiPerm = getModulePerm(clz);
                    List<SysPerm> methodApiPerm = getApiPerm(clz, moduleApiPerm.getVal());
                    moduleApiPerm.getChildren().addAll(methodApiPerm);
                    return moduleApiPerm;
                }).collect(Collectors.toList());
        JSONObject json = new JSONObject();
//        json.put("apiList",apiList);
        return HttpResult.success(json);
    }
    @SecurityParameter(outEncode = true)
    @GetMapping("/list/btn_perm_map")
    public Object listButtonPermMapGroupByParent() {
        List<SysPerm> buttonPermList = permService.findByTypes(new Integer[]{PermType.BUTTON});

        Map<String, List<SysPerm>> buttonsGroupedByParent = new HashMap<>();
        if (buttonPermList!=null&&!buttonPermList.isEmpty()){
            buttonsGroupedByParent = buttonPermList.stream().collect(Collectors.groupingBy(SysPerm::getParent));
        }
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        buttonsGroupedByParent.forEach((k,v)->{
            JSONArray jsonArray2 = new JSONArray();
            v.forEach( s->{
                JSONObject jsonObject2_1 = new JSONObject();
                jsonObject2_1.put("val",s.getVal());
                jsonObject2_1.put("parent",s.getParent());
                jsonObject2_1.put("name",s.getName());
                jsonObject2_1.put("type",s.getType());
                jsonObject2_1.put("leaf",s.getLeaf());
                jsonArray2.add(jsonObject2_1);
            });
            jsonObject1.put( k+"" ,jsonArray2);
        });
        jsonObject.put("btnPermMap",jsonObject1);
        return HttpResult.success(jsonObject);
    }


    /**
     * 获取控制器上的方法上的注释，生成后台接口权限的信息
     *
     * @param clz
     * @return
     */
    private List<SysPerm> getApiPerm(Class<?> clz, final String parentPval) {
        //获取clz类上有RequiresPermissions注解的所有方法
        List<Method> apiMethods = MethodUtils.getMethodsListWithAnnotation(clz.getSuperclass(), RequiresPermissions.class);
        return apiMethods.stream().map(method -> {
            //pname首选
            //获取method方法上的PermInfo注解的元数据
            PermInfo piAnno = AnnotationUtils.getAnnotation(method, PermInfo.class);
            String pnamePrimary = piAnno!=null?piAnno.value():null;
            //pname备选
            String pnameSub = method.getName();
            //pval值
            //获取method方法上的RequiresPermissions注解的元数据
            RequiresPermissions rpAnno = AnnotationUtils.getAnnotation(method, RequiresPermissions.class);
            SysPerm perm = new SysPerm();
            if (org.apache.commons.lang3.StringUtils.isNotBlank(pnamePrimary)){
                perm.setName(pnamePrimary);
            }else{
                perm.setName(pnameSub);
            }
            perm.setParent(parentPval);
            perm.setType(PermType.API);
            perm.setVal(rpAnno.value()[0]);
            return perm;
        }).collect(Collectors.toList());
    }
    /**
     * 获取控制器上的注释，生成后台接口模块权限的信息
     *
     * @param clz
     * @return
     */
    public SysPerm getModulePerm(Class<?> clz) {
        SysPerm perm = new SysPerm();
        //首选值
        PermInfo piAnno = AnnotationUtils.getAnnotation(clz, PermInfo.class);
        if (piAnno == null) {
            //由于使用了RequiresPermissions注解的类在运行时会使用动态代理，即clz在运行时是一个动态代理，所以需要getSuperClass获取实际的类型
            piAnno = AnnotationUtils.getAnnotation(clz.getSuperclass(), PermInfo.class);
        }
        String pnamePrimary = null;
        String pvalPrimary = null;
        String pvalPrimary2 = null;
        if (piAnno != null && piAnno.value() != null) {
            pnamePrimary = piAnno.value();
            pvalPrimary = piAnno.pval();
        }

        //备选值1
        RequiresPermissions rpAnno = AnnotationUtils.getAnnotation(clz, RequiresPermissions.class);
        if (rpAnno == null) {
            rpAnno = AnnotationUtils.getAnnotation(clz.getSuperclass(), RequiresPermissions.class);
        }
        if (rpAnno != null) {
            pvalPrimary2 = rpAnno.value()[0];
        }

        //备选值2
        String pnameSub = ClassUtils.getShortName(clz);
        RequestMapping rmAnno = AnnotationUtils.getAnnotation(clz, RequestMapping.class);
        if (rmAnno == null) {
            rmAnno = AnnotationUtils.getAnnotation(clz.getSuperclass(), RequestMapping.class);
        }
        String pvalSub = rmAnno.value()[0];
        //赋值
        if (org.apache.commons.lang3.StringUtils.isNotBlank(pnamePrimary)) {
            perm.setName(pnamePrimary);
        } else {
            perm.setName(pnameSub);
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(pvalPrimary)) {
            perm.setVal(pvalPrimary);
        }else if(org.apache.commons.lang3.StringUtils.isNotBlank(pvalPrimary2)){
            perm.setVal(pvalPrimary2);
        } else {
            perm.setVal("a:"+pvalSub.substring(1).replace("/",":"));
        }
        perm.setType(PermType.API);
        return perm;
    }
}
