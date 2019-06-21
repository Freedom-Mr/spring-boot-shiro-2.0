package casia.isiteam.springbootshiro.model.po.sysuser.rep;

import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysRolePerm;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * sys_role_perm 实体操作类
 * Fri Oct 26 19:25:16 CST 2018
 * @casia
 */ 
public interface SysRolePermRepository extends JpaRepository<SysRolePerm,Integer>{
    @Transactional
    void deleteByPermVal(String permVal);
    @Transactional
    void deleteByRoleIdAndPermValIs(Integer roleId,String permVal);
    @Transactional
    void deleteAllByRoleIdIs(Integer roleId);
    @Transactional
    void deleteAllByRoleIdAndType(Integer roleId,Integer type);
}

