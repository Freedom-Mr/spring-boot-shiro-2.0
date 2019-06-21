package casia.isiteam.springbootshiro.service.systeam;

import casia.isiteam.springbootshiro.model.po.sysuser.rep.SysDepartmentRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.rep.SysInstitutionRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysDepartment;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysInstitution;
import casia.isiteam.springbootshiro.util.Validator;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by casia.wzy on 2018/10/25.
 */
@Service
public class DepartmentService {
    private final static Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Resource
    private SysDepartmentRepository sysDepartmentRepository;
    @Resource
    private SysInstitutionRepository sysInstitutionRepository;
    // 查询部门（分页）
    public JSONObject querDepartmentByPage(String name ,String sortField, int instId, Integer page, Integer size) {
        Sort sort= new Sort(Sort.Direction.DESC, Validator.check(sortField) ? sortField :"id");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        Page<SysDepartment> li = null;

        if(instId == -1 ){
            li = Validator.check(name) ? sysDepartmentRepository.findAllByNameLikeAndIdNot("%"+name+"%",-1,pageable) : sysDepartmentRepository.findAllByIdNot(-1,pageable);
        }else if( instId >0 ){
            li = Validator.check(name) ? sysDepartmentRepository.findAllByNameLikeAndIdNotAndInstId("%"+name+"%",-1,instId,pageable) : sysDepartmentRepository.findAllByInstIdAndIdNot(instId,-1,pageable);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",Validator.check(li) ? li.getTotalElements() : 0 );//信息总数
        jsonObject.put("page",Validator.check(li) ? li.getNumber() : page );//当前页
        jsonObject.put("size",Validator.check(li) ? li.getSize() : size);//页面设置的数量
        jsonObject.put("totalPages",Validator.check(li) ? li.getTotalPages() : 0 );//页数总量
//        jsonObject.put("sysDepartments",Validator.check(li) ? li.getContent() : new String[]{});//页面信息集合
        if( li != null  ){
            JSONArray jsonArray = new JSONArray();
            li.getContent().forEach( s->{
                JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(s));
                List<SysInstitution> sysInstitution = sysInstitutionRepository.findAllById(s.getInstId());
                sysInstitution.forEach(c->{
                    json.put("instName",c.getName());
                });
                jsonArray.add(json);
            });
            jsonObject.put("sysDepartments",jsonArray);
        }else{
            jsonObject.put("sysDepartments",new String[]{});
        }

        return jsonObject;
    }
    // 查询部门
    public SysDepartment queryDepartment( Integer id) {
        SysDepartment sysDepartment = sysDepartmentRepository.findFirstById(id);
        return sysDepartment;
    }
    // 删除部门
    public void deleteDepartment( Integer id) {
        sysDepartmentRepository.deleteById(id);
    }

    // 保存更新部门
    public SysDepartment saveDepartment(SysDepartment sysDepartment) {
        if( !Validator.check( sysDepartment.getCreateTime() ) ){
            sysDepartment.setCreateTime(new Date());
        }
        return sysDepartmentRepository.saveAndFlush(sysDepartment);
    }
}
