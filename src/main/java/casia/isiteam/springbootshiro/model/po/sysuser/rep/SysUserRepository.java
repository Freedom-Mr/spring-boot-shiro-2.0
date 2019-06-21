package casia.isiteam.springbootshiro.model.po.sysuser.rep;

import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * sys_user 实体操作类
 * Fri Sep 28 17:40:25 CST 2018
 * @casia
 */ 
public interface SysUserRepository extends JpaRepository<SysUser,Integer>{
    @Transactional
    @Modifying
    SysUser saveAndFlush(SysUser sysUser);

    SysUser findByUsername(String username);
    SysUser findFirstById(Integer id);
    Page<SysUser> findAllByIdGreaterThan(Integer id,Pageable pageable);
    List<SysUser> findAllByDepId(int depId, Pageable pageable);
    List<SysUser> findAllByDepId(int depId);
    //自定义条件
    Page<SysUser> findAll(Specification<SysUser> spec, Pageable pageable);
    //查询无效用户
    @Query("select u from SysUser u where u.deadline< ?1 and u.status = ?2")
    List<SysUser> queryAllInvalidUsers(Date nowDate, int status);

    //修改用户密码
    @Query("update  SysUser u set u.password=?1 where u.id=?2")
    @Transactional
    @Modifying
    void updateUserPwd(String password, int id);

    //修改用户密码
    @Query("update  SysUser u set u.realName=?1, u.telephone=?2, u.email=?3, u.note=?4 where u.id=?5")
    @Transactional
    @Modifying
    void updateUserMe(String realName, String telephone,String email,String note,Integer id);

}

