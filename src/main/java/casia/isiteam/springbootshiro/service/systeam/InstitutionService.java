package casia.isiteam.springbootshiro.service.systeam;

import casia.isiteam.springbootshiro.constant.PermType;
import casia.isiteam.springbootshiro.model.po.sysuser.rep.SysDepartmentRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.rep.SysInstitutionRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.rep.SysUserRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysDepartment;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysInstitution;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysPerm;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysUser;
import casia.isiteam.springbootshiro.util.Validator;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by casia.wzy on 2018/10/24.
 */
@Service
public class InstitutionService {
    private final static Logger logger = LoggerFactory.getLogger(InstitutionService.class);
    @Resource
    private SysInstitutionRepository sysInstitutionRepository;
    @Resource
    private SysDepartmentRepository sysDepartmentRepository;
    @Resource
    private SysUserRepository sysUserRepository;

    // 查询单位
    public JSONObject querInstitutionById(Integer id ) {
        List<SysInstitution> li = id == -1 ? sysInstitutionRepository.findAllByIdNot(-1) : sysInstitutionRepository.findAllById(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sysInstitutions",li);//页面信息集合
        return jsonObject;
    }
    // 查询单位
    public SysInstitution querInstitutionById2(Integer id ) {
        SysInstitution sysInstitution =  sysInstitutionRepository.findFirstById(id);
        return sysInstitution;
    }
    // 查询单位（分页）
    public JSONObject querInstitutionByPage(String name , Integer page, Integer size, String sortField) {
        Sort sort= new Sort(Sort.Direction.DESC, Validator.check(sortField) ? sortField :"id");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<SysInstitution> li = Validator.check(name) ? sysInstitutionRepository.findAllByNameLikeAndIdNot("%"+name+"%",-1,pageable) : sysInstitutionRepository.findAllByIdNot(-1,pageable);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",li.getTotalElements());//信息总数
        jsonObject.put("page",li.getNumber());//当前页
        jsonObject.put("size",li.getSize());//页面设置的数量
        jsonObject.put("totalPages",li.getTotalPages());//页数总量
        jsonObject.put("sysInstitutions",li.getContent());//页面信息集合
        return jsonObject;
    }

    // 删除单位
    public void deleteInstitution( Integer id) {
        sysInstitutionRepository.deleteById(id);
    }

    // 保存更新单位
    public SysInstitution saveInstitution(SysInstitution sysInstitution) {
        if( !Validator.check( sysInstitution.getCreateTime() ) ){
            sysInstitution.setCreateTime(new Date());
        }
        return sysInstitutionRepository.saveAndFlush(sysInstitution);
    }

    // 根据用户权限查询看见单位及其部门
    public JSONArray queryUserIntis() {
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser)subject.getPrincipal();

        List<SysInstitution> list = new ArrayList();
        if( Validator.check( user ) ){
            if( user.getInstId() == -1 ){
                list = sysInstitutionRepository.findAllByIdNot(-1);
            }else{
                list = sysInstitutionRepository.findAllById(user.getInstId());
            }
        }

        JSONArray jsonArray = new JSONArray();
        list.forEach(s->{
            List<SysDepartment> deps = user.getDepId() == -1 ? sysDepartmentRepository.findAllByInstIdIs(s.getId()) : sysDepartmentRepository.findAllByInstIdAndId(s.getId(),user.getDepId());
            JSONObject js_1 = new JSONObject();
            js_1.put("value",s.getId() );
            js_1.put("label",s.getName() );
            JSONArray ja_1 = new JSONArray();
            deps.forEach(c->{
                JSONObject js_2 = new JSONObject();
                js_2.put("value",c.getId() );
                js_2.put("label",c.getName() );
                ja_1.add(js_2);
            });
            js_1.put("children",ja_1);
            jsonArray.add(js_1);
        });
        return jsonArray;
    }
    // 根据用户权限查询看见单位及其部门、用户
    public JSONArray queryUserIntisDeps() {
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser)subject.getPrincipal();

        List<SysInstitution> list = new ArrayList();
        if( Validator.check( user ) ){
            if( user.getInstId() == -1 ){
                list = sysInstitutionRepository.findAllByIdNot(-1);
            }else{
                list = sysInstitutionRepository.findAllById(user.getInstId());
            }
        }

        JSONArray jsonArray = new JSONArray();
        list.forEach(s->{
            List<SysDepartment> deps = user.getDepId() == -1 ? sysDepartmentRepository.findAllByInstIdIs(s.getId()) : sysDepartmentRepository.findAllByInstIdAndId(s.getId(),user.getDepId());
            JSONObject js_1 = new JSONObject();
            js_1.put("value",s.getId() );
            js_1.put("label",s.getName() );
            JSONArray ja_1 = new JSONArray();
            deps.forEach(c->{
                JSONObject js_2 = new JSONObject();
                js_2.put("value",c.getId() );
                js_2.put("label",c.getName() );

                JSONArray ja_2 = new JSONArray();
                List<SysUser> users = sysUserRepository.findAllByDepId( c.getId() );
                users.forEach(u->{
                    JSONObject js_3 = new JSONObject();
                    js_3.put("value",u.getId() );
                    js_3.put("label",u.getRealName());
                    ja_2.add(js_3);
                });
                if( ja_2.size() != 0 ){js_2.put("children",ja_2);}
                ja_1.add(js_2);
            });
            if( ja_1.size() != 0 ){js_1.put("children",ja_1);}
            jsonArray.add(js_1);
        });
        return jsonArray;
    }
}
