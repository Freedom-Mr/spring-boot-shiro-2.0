package casia.isiteam.springbootshiro.service.systeam;

import casia.isiteam.springbootshiro.constant.PermType;
import casia.isiteam.springbootshiro.model.po.sysuser.rep.*;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.*;
import casia.isiteam.springbootshiro.model.vo.UpdateRolePermVo;
import casia.isiteam.springbootshiro.util.TimeUtil;
import casia.isiteam.springbootshiro.util.Validator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by casia.wzy on 2018/10/28.
 */
@Service
public class RoleService {
    private final static Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Resource
    private SysRoleRepository sysRoleRepository;
    @Resource
    private SysInstitutionRepository  sysInstitutionRepository;
    @Resource
    private SysDepartmentRepository sysDepartmentRepository;
    @Resource
    private SysUserRepository sysUserRepository;
    @Resource
    private SysRolePermRepository sysRolePermRepository;

    //查询用户角色权限信息
    public SysRole searchRoleParm(SysUser sysUser){
        if( sysUser.getInstId() == -1 && sysUser.getDepId() == -1 ){
            //超管
            return sysRoleRepository.findFirstByGradeAndInstIdAndDepId(1,-1,-1);
        }else{

            //查询默认角色权限
            Integer grade = sysUser.getDepId() == -1 ? 2 : 3;
            SysRole sysRole_default = sysRoleRepository.findFirstByGradeAndPartAndInstIdAndDepId(grade,sysUser.getPart(),-1,-1);

            //查询过滤权限
            List<SysRole> list = new ArrayList<>();
//            List<SysRole> list = sysRoleRepository.findAll( sysUser.getInstId() ,new Integer[]{-1,sysUser.getDepId()},sysUser.getId() );
            SysRole sysRole = sysRoleRepository.findFirstByUserId(sysUser.getId());
            if( Validator.check(sysRole) ){
                list.add(sysRole);
            }else if (sysUser.getDepId() != -1){
               SysRole sysRole1 = sysRoleRepository.findFirstByDepId(sysUser.getDepId());
               if( Validator.check(sysRole1) ){
                   list.add(sysRole1);
               }
            }

            if( !Validator.check(list) ){
                SysRole sysRole1 = sysRoleRepository.findFirstByInstId(sysUser.getInstId());
                if( Validator.check(sysRole1) ){
                    list.add(sysRole1);
                }
            }

            List<SysPerm> sysPerms = new ArrayList<>();
            sysRole_default.getPerms().forEach( s->{
                boolean rs = list.stream().filter( c-> c.getPerms().stream().filter( d-> d.getVal().equals( s.getVal() ) ).findFirst().isPresent() ).findFirst().isPresent();
                if(rs){sysPerms.add(s);}
            });

            //重新赋权
            if( Validator.check(sysPerms) ){
                sysRole_default.setPerms(sysPerms);
            }
            return sysRole_default;
        }
    }

    // 查询角色（分页）
    public JSONObject querRoleByPage(String name , String sortField, String gradeId, Integer page, Integer size) {
        Sort sort= new Sort(Sort.Direction.DESC, Validator.check(sortField) ? sortField :"id");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<SysRole> li = null;

        if( Validator.check(gradeId) ){
            li = Validator.check(name) ? sysRoleRepository.findAllByGradeAndNameLike( Integer.parseInt(gradeId ),name ,pageable ) : sysRoleRepository.findAllByGrade( Integer.parseInt(gradeId ) , pageable );
        }else{
            li = Validator.check(name) ? sysRoleRepository.findAllByNameLike( "%"+name+"%" ,pageable  ) : sysRoleRepository.findAll(pageable);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",Validator.check(li) ? li.getTotalElements() : 0 );//信息总数
        if( li != null  ){
            JSONArray jsonArray = new JSONArray();
            li.getContent().forEach( s->{
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id",s.getId());
                jsonObject1.put("name",s.getName());
                jsonObject1.put("grade",s.getGrade());
                switch (s.getGrade()){
                    case 1: jsonObject1.put("gradeName","系统");break;
                    case 2: jsonObject1.put("gradeName","单位");break;
                    case 3: jsonObject1.put("gradeName","部门");break;
                    default: jsonObject1.put("gradeName","自定义"); break;
                }
                jsonObject1.put("part",s.getPart());
                switch (s.getPart()){
                    case 1: jsonObject1.put("partName","管理员"); break;
                    case 2: jsonObject1.put("partName","审计员"); break;
                    case 3: jsonObject1.put("partName","业务员"); break;
                    default: jsonObject1.put("partName","自定义"); break;
                }
                String userName = "所有";
                String instName = "所有";
                String depName = "所有";
                if( s.getGrade() == -1  && s.getUserId() != -1 ){
                    jsonObject1.put("type",3);
                    SysUser sysUser = sysUserRepository.findFirstById(s.getUserId());
                    userName = sysUser.getUsername();
                    instName =  sysInstitutionRepository.findFirstById(sysUser.getInstId()).getName();
                    depName =  sysDepartmentRepository.findFirstById(sysUser.getDepId()).getName();
                }else if(  s.getGrade() == -1  && s.getUserId() == -1 && s.getDepId() != -1 ){
                    jsonObject1.put("type",2);
                    SysDepartment sysDepartment = sysDepartmentRepository.findFirstById(s.getDepId());
                    instName =  sysInstitutionRepository.findFirstById(sysDepartment.getInstId()).getName();
                    depName =  sysDepartment.getName();
                }else if(  s.getGrade() == -1  && s.getUserId() == -1 && s.getDepId() == -1 && s.getInstId() != -1 ){
                    jsonObject1.put("type",1);
                    instName =  sysInstitutionRepository.findFirstById(s.getInstId()).getName();
                }
                jsonObject1.put("instId",s.getInstId());
                jsonObject1.put("instName",instName );
                jsonObject1.put("depId",s.getDepId());
                jsonObject1.put("depName",depName );
                jsonObject1.put("userId",s.getUserId());
                jsonObject1.put("userName",userName );
                jsonObject1.put("rdesc",s.getRdesc());
                jsonObject1.put("updateTime", TimeUtil.timeFotmat(s.getUpdateTime()) );
                jsonObject1.put("createTime",TimeUtil.timeFotmat(s.getCreateTime()) );
                jsonArray.add(jsonObject1);
            });
            jsonObject.put("sysRoles",jsonArray);
        }else{
            jsonObject.put("sysRoles",new String[]{});
        }

        return jsonObject;
    }
    //查询角色权限
    public JSONObject searchRolePerms(int id){
        SysRole sysRole = sysRoleRepository.findFirstById(id);
        List<SysPerm> perms = sysRole.getPerms();
        Map<Integer, List<SysPerm>> permMap = perms.stream().collect(Collectors.groupingBy(SysPerm::getType));

        List<String> menuVals = permMap.getOrDefault(PermType.MENU, new ArrayList<>()).stream()
                .filter(perm->perm.getLeaf() == 1).map(SysPerm::getVal).collect(Collectors.toList());

        List<String> btnVals = permMap.getOrDefault(PermType.BUTTON, new ArrayList<>()).stream()
                .map(SysPerm::getVal).collect(Collectors.toList());

        List<String> apiVals = permMap.getOrDefault(PermType.API, new ArrayList<>()).stream()
                .filter(perm->perm.getLeaf()== 1).map(SysPerm::getVal).collect(Collectors.toList());

        JSONObject jsonObject = new JSONObject();

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id",sysRole.getId());
        jsonObject1.put("name",sysRole.getName());
//        jsonObject1.put("rval",sysRole.getRval());
        jsonObject1.put("rdesc",sysRole.getRdesc());
        jsonObject1.put("updateTime",sysRole.getUpdateTime());
        jsonObject1.put("createTime",sysRole.getCreateTime());
//        jsonObject1.put("gradeName",sysRole.getSysGrade().getName());
//        jsonObject1.put("sysGrade",sysRole.getSysGrade());

        jsonObject.put("role",jsonObject1);
        jsonObject.put("menuVals",menuVals);
        jsonObject.put("btnVals",btnVals);
        jsonObject.put("apiVals",apiVals);

        return jsonObject;
    }
    //保存角色
    public void saveRole(JSONObject jsonObject){
        if ( !jsonObject.containsKey("indepus") ){
            return;
        }

        SysRole sysRole = new SysRole();
        JSONArray ja =jsonObject.getJSONArray("indepus");
        switch ( ja.size() ){
            case 1: sysRole.setInstId(ja.getInteger(0) ); sysRole.setDepId(-1 );sysRole.setUserId(-1 );break;
            case 2: sysRole.setInstId(ja.getInteger(0) ); sysRole.setDepId(ja.getInteger(1) );sysRole.setUserId(-1 );break;
            case 3: sysRole.setInstId(ja.getInteger(0) ); sysRole.setDepId(ja.getInteger(1) );sysRole.setUserId(ja.getInteger(2) );break;
            default: break;
        }
        sysRole.setName(jsonObject.getString("name"));
        sysRole.setGrade(-1);
        sysRole.setPart(-1);
        sysRole.setRdesc( jsonObject.containsKey("rdesc") ? jsonObject.getString("rdesc") :null );
        sysRole.setCreateTime(new Date());
        sysRole.setUpdateTime(new Date());
        sysRoleRepository.save(sysRole);
    }
    //更新角色
    public void flushRole(JSONObject jsonObject){
        if ( !jsonObject.containsKey("id") ){
            return;
        }
        sysRoleRepository.updateSysRole(jsonObject.getString("name"),
                jsonObject.containsKey("rdesc") ? jsonObject.getString("rdesc") :null ,
                jsonObject.getInteger("id"));
    }
    //删除角色
    public void deleteRole(int id){
        sysRoleRepository.deleteById(id);
    }

    //更新角色权限
    public void updateRoleperm(UpdateRolePermVo vo){
        final Integer id = vo.getId();
        final Integer type = vo.getType();
        final List<String> vals = vo.getVals();
        if( type == 1 ){
            sysRolePermRepository.deleteAllByRoleIdAndType(id,type);
        }

        if (Validator.check(vals)){
            List<SysRolePerm> list =vals.stream().map( val -> new SysRolePerm(id, val, type ,new Date() )).collect(Collectors.toList());
            sysRolePermRepository.saveAll(list);
        }
    }
    //删除角色权限
    public void delelteRoleperm(UpdateRolePermVo vo){
        final Integer id = vo.getId();
        final Integer type = vo.getType();
        final List<String> vals = vo.getVals();

        if (Validator.check(vals)){
//            List<SysRolePerm> list =vals.stream().map( val -> new SysRolePerm(id, val,new Date() )).collect(Collectors.toList());
            List<SysRolePerm> list = new ArrayList<>();
            vals.forEach(s->{
                sysRolePermRepository.deleteByRoleIdAndPermValIs(id,s);
            });
        }
    }
    //是否开启自定义权限
    public JSONObject getCustom(Integer type,Integer id){
        JSONObject jsonObject = new JSONObject();
        SysRole sysRole = null;
        SysRole visibSysRole = null;
        switch (type){
            case 1:
                sysRole= sysRoleRepository.findFirstByInstId(id);
                break;
            case 2:
                sysRole= sysRoleRepository.findFirstByDepId(id);
                break;
            case 3:
                sysRole= sysRoleRepository.findFirstByUserId(id);
                break;
            default:break;
        }
        if( Validator.check(sysRole) ){
            List<SysRole> li = new ArrayList<>();
            li.add(sysRole);
            jsonObject.put("custom",true);
            jsonObject.put("id",sysRole.getId());
            jsonObject.put("visibPerms",getCustomPerm(type,id));
            JSONArray jsonArray = new JSONArray();
            sysRole.getPerms().stream().forEach(s->{
                JSONObject jsonObject1 =new JSONObject();
                jsonObject1.put("val",s.getVal());
                jsonObject1.put("type",s.getType());
                jsonObject1.put("parent",s.getParent());
                jsonObject1.put("sys",s.getSys());
                jsonObject1.put("hidden",s.getHidden());
                jsonObject1.put("parent_node",li.stream().filter(d-> d.getPerms().stream().filter(c-> Validator.check(c.getParent()) && c.getParent().equals(s.getVal())).findFirst().isPresent() ).findFirst().isPresent() ? true:false );
                jsonArray.add(jsonObject1);
            });
            jsonObject.put("selectPerms",jsonArray);
        }else{
            jsonObject.put("custom",false);
        }
        return jsonObject;
    }
    //获取自定义权限
    public JSONArray getCustomPerm(Integer type,Integer id){
        SysRole visibSysRole = null;
        switch (type){
            case 1:
                visibSysRole = sysRoleRepository.findFirstByGradeAndPartAndInstIdAndDepId(2,1,-1,-1);
                break;
            case 2:
                visibSysRole = sysRoleRepository.findFirstByGradeAndPartAndInstIdAndDepId(3,1,-1,-1);
                //查询单位是否限制权限
                SysDepartment sysDepartment = sysDepartmentRepository.findFirstById(id);
                SysRole sysRoleInst = sysRoleRepository.findFirstByGradeAndInstIdAndDepId(-1,sysDepartment.getInstId(),-1);
                if( Validator.check(visibSysRole) && Validator.check(sysRoleInst) ){
                    Stream<SysPerm> list = visibSysRole.getPerms().stream().filter(c-> sysRoleInst.getPerms().stream().filter(d-> c.getVal().equals( d.getVal() ) ).findFirst().isPresent() );
                    visibSysRole.setPerms(list.collect(Collectors.toList()));
                }
                break;
            case 3:
                SysUser sysUser = sysUserRepository.findFirstById(id);
                Integer grade = sysUser.getDepId() == -1 ? 2 : 3;
                visibSysRole = sysRoleRepository.findFirstByGradeAndPartAndInstIdAndDepId(grade,sysUser.getPart(),-1,-1);
//                List<SysRole> list = sysRoleRepository.findAllLimtPerms( sysUser.getInstId()== -1 ? null : sysUser.getInstId(),sysUser.getDepId()==-1 ? null : sysUser.getDepId());
                List<SysRole> list = new ArrayList<>();
                if( sysUser.getDepId() != -1 ){
                    SysRole sysRole =  sysRoleRepository.findFirstByDepId(sysUser.getDepId());
                    if( Validator.check(sysRole) ){ list.add(sysRole);}
                }
                if( (sysUser.getDepId() == -1&& sysUser.getInstId() != -1) || (!Validator.check(list) && sysUser.getDepId() != -1&& sysUser.getInstId() != -1) ){
                    SysRole sysRole =  sysRoleRepository.findFirstByInstId(sysUser.getInstId());
                    if( Validator.check(sysRole) ){ list.add(sysRole);}
                }
                if(Validator.check(list)){
                    List<SysPerm> sysPerms = new ArrayList<>();
                    visibSysRole.getPerms().forEach( s->{
                        boolean rs = list.stream().filter( c-> c.getPerms().stream().filter( d-> s.getVal().equals( d.getVal() ) ).findFirst().isPresent() ).findFirst().isPresent();
                        if(rs){sysPerms.add(s);}
                    });
                    visibSysRole.setPerms(sysPerms);
                }
                break;
            default:break;
        }

        Map<String, SysPerm> visibSysPerm = new LinkedHashMap<>();
        visibSysRole.getPerms().stream().filter(s-> s.getSys()==0 ).forEach(s-> visibSysPerm.put( s.getVal(),s ));
        List<List<JSONObject>> list = new ArrayList<>();
        filtersTransform(visibSysPerm,list);
        return JSONArray.parseArray(JSON.toJSONString(list));
    }
    //是否开启自定义权限
    public JSONObject openCustom(Integer type,Integer id,Integer roleId,boolean isOpen){
        JSONObject jsonObject = new JSONObject();

        if( isOpen == false && Validator.check(roleId)){
            sysRoleRepository.deleteByIdAndGradeAndPart(roleId,-1,-1);
        }else if( isOpen == true ){
            String name = "-自定义";
            switch (type){
                case 1:
                    SysInstitution sysInstitution = sysInstitutionRepository.findFirstById(id);
                    name = sysInstitution.getName()+name;
                    break;
                case 2:
                    SysDepartment sysDepartment= sysDepartmentRepository.findFirstById(id);
                    name = sysDepartment.getName()+name;
                    break;
                case 3:
                    SysUser sysUser= sysUserRepository.findFirstById(id);
                    name = sysUser.getRealName()+name;
                    break;
                default:break;
            }
            SysRole sysRole = new SysRole();
            sysRole.setName(name);
            sysRole.setGrade(-1);
            sysRole.setPart(-1);
            sysRole.setInstId( type==1 ? id : -1 );
            sysRole.setDepId( type==2 ? id: -1 );
            sysRole.setUserId( type==3 ? id: -1 );
            sysRole.setRdesc(name+"权限");
            sysRole.setCreateTime(new Date());
            sysRole.setUpdateTime(new Date());
            SysRole rs = sysRoleRepository.saveAndFlush(sysRole);
            jsonObject.put("id",rs.getId());
            jsonObject.put("name",rs.getName());
        }
        return jsonObject;
    }
    //保存自定义权限
    @Transactional
    public void updateCustomPerm(List<SysRolePerm> sysRolePermList,int roleId){
       sysRolePermRepository.deleteAllByRoleIdIs(roleId);
       sysRolePermList.forEach(s->{
           s.setCreateTime(new Date());
           sysRolePermRepository.saveAndFlush(s);
       });
    }
    private boolean filtersTransform(Map<String, SysPerm> visibSysPerm, List<List<JSONObject>> list){
        Map<String, SysPerm> cursorMaP = new HashMap<>();
        cursorMaP.putAll(visibSysPerm);
        int cursor = 0;
        while ( Validator.check(cursorMaP) ){
            visibSysPerm.clear();
            visibSysPerm.putAll(cursorMaP);
            visibSysPerm.forEach( (k,v)->{
                //根菜单
                if( !Validator.check(v.getParent()) ){
                    boolean rs = list.stream().filter(c-> c.stream().filter(d->d.getString("val").equals(v.getVal())).findFirst().isPresent()).isParallel();
                    if( !rs ) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id",v.getVal());
                        jsonObject.put("val",v.getVal());
                        jsonObject.put("name",v.getName());
                        jsonObject.put("parent",v.getParent());
                        jsonObject.put("sys",v.getSys());
                        jsonObject.put("type",v.getType());
                        jsonObject.put("hidden",v.getHidden());
                        List<JSONObject> li = new ArrayList<>();
                        li.add(jsonObject);
                        list.add(li);
                        cursorMaP.remove(k);
                    };
                }
                //子目录
                else{
                    boolean rs = false;
                    for (List<JSONObject> lis : list) {
                        for (JSONObject c : lis) {
                            rs = iteration( c,v );
                            if(rs){  cursorMaP.remove(k); break;}
                        }
                        if(rs){ break;}
                    }
                }
            });
            if( cursorMaP.size() == cursor ){
                break;
            }else{
                cursor = cursorMaP.size();
            }
        }
        return false;
    }
    private boolean iteration( JSONObject jsonObject ,SysPerm s ){
        if( jsonObject.containsKey("children") ){
            if( jsonObject.getString("val").equals(s.getParent()) ){
                if ( !jsonObject.getJSONArray("children").stream().filter( c->JSON.parseObject(c.toString()).getString("val").equals(s.getVal())  ).findFirst().isPresent() ){
                    JSONObject jsonObject_1 = new JSONObject();
                    jsonObject.put("id",s.getVal());
                    jsonObject_1.put("val",s.getVal());
                    jsonObject_1.put("name",s.getName());
                    jsonObject_1.put("parent",s.getParent());
                    jsonObject_1.put("sys",s.getSys());
                    jsonObject_1.put("type",s.getType());
                    jsonObject_1.put("hidden",s.getHidden());
                    jsonObject.getJSONArray("children").add(jsonObject_1);
                }
                return true;
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("children");
                for(int i=0; i< jsonArray.size(); i++){
                    boolean rs = iteration( jsonArray.getJSONObject(i), s );
                    if( rs ){ return true; }
                }
                return false;
            }
        }else if( !jsonObject.containsKey("children") &&  jsonObject.getString("val").equals(s.getParent()) ){
            JSONObject jsonObject_1 = new JSONObject();
            jsonObject.put("id",s.getVal());
            jsonObject_1.put("val",s.getVal());
            jsonObject_1.put("name",s.getName());
            jsonObject_1.put("parent",s.getParent());
            jsonObject_1.put("sys",s.getSys());
            jsonObject_1.put("type",s.getType());
            jsonObject_1.put("hidden",s.getHidden());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject_1);
            jsonObject.put("children",jsonArray);
            return true;
        }
        return false;
    }
}
