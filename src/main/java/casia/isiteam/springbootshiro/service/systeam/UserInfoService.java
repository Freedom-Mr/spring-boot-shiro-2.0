package casia.isiteam.springbootshiro.service.systeam;

import casia.isiteam.springbootshiro.model.po.sysuser.rep.*;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.*;
import casia.isiteam.springbootshiro.properties.other.UrlConfig;
import casia.isiteam.springbootshiro.properties.result.HttpResult;
import casia.isiteam.springbootshiro.util.TimeUtil;
import casia.isiteam.springbootshiro.util.Validator;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
/**
 * Author wzy
 * Date 2018/1/15 14:46
 */
@Service
public class UserInfoService {
    private final static Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    @Resource
    private SysUserRepository sysUserRepository;
    @Resource
    private SysLoginIpRepository sysLoginIpRepository;
    @Resource
    private SysRoleRepository sysRoleRepository;
    @Resource
    private SysPermRepository sysPermRepository;
    @Resource
    private SysInstitutionRepository sysInstitutionRepository;
    @Resource
    private SysDepartmentRepository sysDepartmentRepository;
    @Resource
    private SysWhiteListRepository sysWhiteListRepository;
    @Resource
    private SysPhotoRepository sysPhotoRepository;
    @Autowired
    private UrlConfig urlConfig;
    /**
     * 根据ID查询用户
     * @param id
     * @return
     */
    public SysUser findByUserId(Integer id) {
        SysUser userInfo = new SysUser();
        try {
            userInfo = sysUserRepository.findFirstById(id);
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return userInfo;
    }
    /**
     * 根据账号查询用户
     * @param username
     * @return
     */
    public SysUser findByUsername(String username) {
        SysUser userInfo = new SysUser();
        try {
            userInfo = sysUserRepository.findByUsername(username);
        } catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return userInfo;
    }

    /**
     * 多条件查询用户列表
     //     * @param grade 级别
     //     * @param inidId 单位
     //     * @param depId 部门
     //     * @param status 锁定
     //     * @param deadline 失效时间
     //     * @param username 用户名
     * @return
     */
    public JSONObject selectUsers( JSONObject  perms) {

        //规格定义
        Specification<SysUser> specification = new Specification<SysUser>() {
            /**
             * 构造断言
             * @param root 实体对象引用
             * @param query 规则查询对象
             * @param cb 规则构建对象
             * @return 断言
             */
            @Override
            public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>(); //所有的断言
                if(Validator.check(perms.getString("userName"))){ //账号
                    Predicate per = cb.like(root.get("username").as(String.class),"%"+perms.getString("userName")+"%");
                    predicates.add(per);
                }
                if( Validator.check(perms.getString("selectRole")) && perms.getInteger("selectRole") != -1){ //级别
                    Predicate per = cb.equal(root.get("part").as(Integer.class),perms.getInteger("selectRole"));
                    predicates.add(per);
                }

                Subject subject = SecurityUtils.getSubject();
                SysUser user = (SysUser)subject.getPrincipal();
                if(Validator.check(perms.getString("selectedInstDep"))){ //单位部门
                    JSONArray instDep = perms.getJSONArray("selectedInstDep");
                    if( instDep.size() ==1 ){
                        Predicate pers = cb.equal( root.get("instId").as(Integer.class),instDep.getInteger(0) );
                        predicates.add(pers);
                        if( user.getDepId() != -1 ){
                            Predicate per = cb.equal( root.get("depId").as(Integer.class),user.getDepId() );
                            predicates.add(per);
                        }
                    }else if( instDep.size() > 1  ){
                        Predicate pers = cb.equal( root.get("instId").as(Integer.class),instDep.getInteger(0) );
                        predicates.add(pers);
                        Predicate per = cb.equal( root.get("depId").as(Integer.class),instDep.getInteger(1) );
                        predicates.add(per);
                    }else if(instDep.size() ==0){
                        if( user.getInstId() != -1 ){
                            Predicate pers = cb.equal( root.get("instId").as(Integer.class),user.getInstId() );
                            predicates.add(pers);
                        }
                        if( user.getDepId() != -1 ){
                            Predicate per = cb.equal( root.get("depId").as(Integer.class),user.getDepId() );
                            predicates.add(per);
                        }
                    }
                }else{
                    if( user.getInstId() != -1 ){
                        Predicate pers = cb.equal( root.get("instId").as(Integer.class),user.getInstId() );
                        predicates.add(pers);
                    }
                    if( user.getDepId() != -1 ){
                        Predicate per = cb.equal( root.get("depId").as(Integer.class),user.getDepId() );
                        predicates.add(per);
                    }
                }

                if(Validator.check(perms.getString("selectLock"))){ //锁定
                    Predicate per = cb.equal(root.get("status").as(Integer.class),perms.getInteger("selectLock"));
                    predicates.add(per);
                }
                if(Validator.check(perms.getString("selectDeadline"))){ //小于失效时间
                    Predicate per = cb.lessThanOrEqualTo(root.get("deadline").as(Date.class),perms.getDate("selectDeadline"));
                    predicates.add(per);
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
        Sort sort= new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(perms.getInteger("page")-1,perms.getInteger("size"),sort);

//        Page<SysUser> pages = sysUserRepository.findAllByIdGreaterThan(0,pageable);
        Page<SysUser> pages = sysUserRepository.findAll(specification,pageable);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",pages.getTotalElements() );//信息总数

        JSONArray jsonArray = new JSONArray();
        pages.getContent().forEach(s->{
            JSONObject json = new JSONObject();
            json.put("id",s.getId());
            json.put("username",s.getUsername());
            json.put("realName",s.getRealName());
            json.put("telephone",s.getTelephone());
            json.put("email",s.getEmail());
            json.put("note",s.getNote());
            json.put("deadline", TimeUtil.timeFotmat(s.getDeadline()) );
            json.put("status",s.getStatus());
            json.put("uniqueLogin",s.getUniqueLogin());
            json.put("part",s.getPart());
            String grade = "";
            if( s.getInstId() == -1 && s.getDepId() == -1 ){ grade= "超级"; }
            else if( s.getInstId() != -1 && s.getDepId() == -1 ){ grade= "单位"; }
            else if( s.getInstId() != -1 && s.getDepId() != -1 ){ grade= "部门"; }
            switch (s.getPart()){
                case 1: json.put("roleName",grade+"管理员");break;
                case 2: json.put("roleName",grade+"审计员");break;
                case 3: json.put("roleName",grade+"业务员");break;
                default: json.put("roleName","- -");break;
            }
            json.put("instId",s.getInstId());
            json.put("inst", s.getInstId()==-1? null :  sysInstitutionRepository.findFirstById(s.getInstId()));
            json.put("depId",s.getDepId());
            json.put("dep",s.getDepId()==-1?  null  : sysDepartmentRepository.findFirstById(s.getDepId()) );
            json.put("createTime",TimeUtil.timeFotmat(s.getCreateTime()) );
            jsonArray.add(json);
        });
        jsonObject.put("sysUsers",jsonArray );
        return jsonObject;
    }

    /**
     * 删除用户
     * @return
     * @throws Exception
     */
    @Transactional
    public void delUser(Integer id) throws Exception{
        sysUserRepository.deleteById(id);
    }

    /**
     * 查询所有失效用户
     * @return
     * @throws Exception
     */
    public List<SysUser> findAllInvalidUser() throws Exception{
        List<SysUser> list =  sysUserRepository.queryAllInvalidUsers( new Date() ,0);
        for( SysUser userInfo:list ){
            userInfo.setPassword(null);
        }
        return list;
    }

    /**
     * 保存登录用户ip
     * @param ip
     * @param user_id
     * @return
     */
    public void saveIp(String ip,int user_id) throws Exception{
        SysLoginIp sysLoginIp = new SysLoginIp();
        sysLoginIp.setIp(ip);
        sysLoginIp.setUserId(user_id);
        sysLoginIp.setLoginTime(new Date());
        try {
            sysLoginIpRepository.save(sysLoginIp);
        }catch (Exception e){
            sysLoginIpRepository.save(sysLoginIp);
        }
    }

    /**
     * 查询用户登录ip
     * @param user_id
     * @param page
     * @param size
     * @return
     */
    public List<SysLoginIp> searchUserLoginIp(int user_id, int page, int size) throws Exception{
        Sort sort= new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        return sysLoginIpRepository.findByUserId(user_id,pageable);
    }

    /**
     * 查询权限详情
     * @return
     */
    public void seaechPermisson() throws Exception{
        SysPerm sysPermisson =  sysPermRepository.findAllByVal("*");
        System.out.println("1");
    }
    /**
     * 保存用户
     * @return
     */
    public Integer saveUser(JSONObject json) throws Exception{
        SysUser sysUser = new SysUser();
        sysUser.setUsername(json.getString("username"));
        sysUser.setPassword(json.getString("password"));
        sysUser.setRealName(json.getString("realName"));
        sysUser.setTelephone(json.getString("telephone"));
        sysUser.setDeadline(json.getDate("deadline"));
        sysUser.setEmail(json.getString("email"));
        sysUser.setStatus(json.getInteger("status"));
        sysUser.setUniqueLogin(json.getInteger("uniqueLogin"));
        sysUser.setNote(json.getString("note"));
        sysUser.setInstId(json.getInteger("instId"));
        sysUser.setDepId(json.containsKey("depId") ? json.getInteger("depId") : -1 );
        sysUser.setPart(json.getInteger("part"));
        sysUser.setCreateTime(new Date());
        sysUser.setUpdateTime(new Date());
        SysUser user = sysUserRepository.save(sysUser);
        //保存IP白名单
        sysWhiteListRepository.deleteAllByUserId( user.getId() );
        if( json.containsKey("whiteList") ){
            JSONArray jsonArray = json.getJSONArray("whiteList");
            jsonArray.forEach(s->{
                SysWhiteList sysWhiteList = new SysWhiteList();
                sysWhiteList.setIp(s.toString());
                sysWhiteList.setUserId(user.getId());
                sysWhiteList.setCreateTime(new Date());
                sysWhiteListRepository.saveAndFlush(sysWhiteList);
            });
        }
        return user.getId();
    }
    /**
     * 修改用户自己非权限资料
     * @return
     */
    public void updateMe(Integer userId, String realName,String telephone,String email,String note) throws Exception{
        sysUserRepository.updateUserMe(realName,telephone,email,note,userId);
    }/**
     * 修改用户
     * @return
     */
    public void updateUser(JSONObject json) throws Exception{
        SysUser sysUser = new SysUser();
        sysUser.setId(json.getInteger("id"));
        sysUser.setUsername(json.getString("username"));
        sysUser.setPassword(json.getString("password"));
        sysUser.setRealName(json.getString("realName"));
        sysUser.setTelephone(json.getString("telephone"));
        sysUser.setDeadline(json.getDate("deadline"));
        sysUser.setEmail(json.getString("email"));
        sysUser.setStatus(json.getInteger("status"));
        sysUser.setUniqueLogin(json.getInteger("uniqueLogin"));
        sysUser.setNote(json.getString("note"));
        sysUser.setInstId(json.getInteger("instId"));
        sysUser.setDepId(json.containsKey("depId") && Validator.check(json.getString("depId"))? json.getInteger("depId") : -1 );
        sysUser.setPart(json.getInteger("part"));
        sysUser.setCreateTime(json.getDate("createTime"));
        sysUser.setUpdateTime(new Date());
        sysUserRepository.saveAndFlush(sysUser);

        //保存IP白名单
        sysWhiteListRepository.deleteAllByUserId( sysUser.getId() );
        if( json.containsKey("whiteList") ){
            JSONArray jsonArray = json.getJSONArray("whiteList");
            jsonArray.forEach(s->{
                SysWhiteList sysWhiteList = new SysWhiteList();
                sysWhiteList.setIp(s.toString());
                sysWhiteList.setUserId(sysUser.getId());
                sysWhiteList.setCreateTime(new Date());
                sysWhiteListRepository.saveAndFlush(sysWhiteList);
            });
        }
    }
    /**
     * 保存用户头像
     * @return
     */
    @Transactional
    public Object saveImage(MultipartFile fileField, Integer userId) {
        if(!Validator.check(fileField)) {
            return HttpResult.fail("参数解析失败！");
        }
        try {
            SysPhoto sysPhoto = new SysPhoto();
            SysPhoto Photo = sysPhotoRepository.findFirstByUserId(userId);
            if( Validator.check( Photo ) ){
                sysPhoto.setId(Photo.getId());
            }
            //解析文件类型
            String fileName = fileField.getOriginalFilename();
            String format = fileName.substring(fileName.lastIndexOf(".")+1);
            sysPhoto.setUserId(userId);
            sysPhoto.setPhoto( fileField.getBytes() );
            sysPhoto.setFormat(format);
            sysPhotoRepository.saveAndFlush( sysPhoto );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return HttpResult.success();
    }
    /**
     * 查询用户头像
     * @return
     */
    public Object searchImage(Integer userId) {
        SysPhoto photo = sysPhotoRepository.findFirstByUserId(userId);
        if( Validator.check( photo ) ){
            return photo;
        }
        return null;
    }
    /**
     * 删除用户头像
     */
    public void deleteImage(Integer userId){
        sysPhotoRepository.deleteAllByUserId(userId);
    }
    /**
     * 修改用户角色
     * @return
     */

    public void updateUserrole(JSONObject json) throws Exception{
        SysUser sysUser = sysUserRepository.findFirstById(json.getInteger("id"));

        SysUser sysUser1 = new SysUser();

        if ( Validator.check(json.getString("roleId")) ) {
            SysRole sysRole = new SysRole();
            sysRole.setId( json.getInteger("roleId") );
//            sysUser1.setSysRole(sysRole);
        }
        sysUser1.setId(sysUser.getId());
        sysUser1.setUsername(sysUser.getUsername());
        sysUser1.setPassword(sysUser.getPassword());
        sysUser1.setRealName(sysUser.getRealName());
        sysUser1.setTelephone(sysUser.getTelephone());
        sysUser1.setEmail(sysUser.getEmail());
        sysUser1.setNote(sysUser.getNote());
        sysUser1.setDeadline(sysUser.getDeadline());
        sysUser1.setStatus(sysUser.getStatus());
        sysUser1.setUniqueLogin(sysUser.getUniqueLogin());
        sysUser1.setInstId(sysUser.getInstId());
        sysUser1.setDepId(sysUser.getDepId());
        sysUser1.setCreateTime(sysUser.getCreateTime());
        sysUser1.setUpdateTime(new Date());
        sysUserRepository.save(sysUser1);
    }

    /**
     * 查询用户密码
     * @return
     */
    public JSONObject saerchUserPassword(JSONObject json) throws Exception{
        SysUser sysUser = sysUserRepository.findFirstById(json.getInteger("id"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("p",sysUser.getPassword());
        return jsonObject;
    }
    /**
     * 修改用户密码
     * @return
     */
    public void updateUserPassword(JSONObject json) throws Exception{
        sysUserRepository.updateUserPwd(json.getString("p"),json.getInteger("id"));
    }
    /**
     * 修改用户密码(登陆用户修改自己)
     * @return
     */
    public Map<Integer,String> updateUserPassword2(JSONObject json) throws Exception{
        Map<Integer,String> map = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser)subject.getPrincipal();
        if( json.containsKey("userId") && json.getInteger("userId") == user.getId() ){
            SysUser sysUser = sysUserRepository.findFirstById(json.getInteger("userId"));
            if( json.containsKey("pwd") && sysUser.getPassword().equals(json.getString("pwd")) ){
                sysUserRepository.updateUserPwd(json.getString("checkPass"),sysUser.getId());
                map.put(1 ,"修改密码成功！");
            }else{
                map.put(2 ,"原始密码错误！");
            }
        }else{
            map.put(2 ,"修改密码错误，请核对信息！");
        }
        return map;
    }
    /**
     * 查询用户是否存在
     * @return
     */
    public boolean searchUserName(String username) throws Exception{
        SysUser sysUser = sysUserRepository.findByUsername(username);
        if( Validator.check(sysUser) && Validator.check(sysUser.getUsername()) ){
            return true;
        }
        return false;
    }
    /**
     * 查询白名单
     */
    public List<SysWhiteList> searchUserWhiteList(Integer userId){
        List<SysWhiteList> list = sysWhiteListRepository.queryAllByUserId(userId);
        return list;
    }
}
