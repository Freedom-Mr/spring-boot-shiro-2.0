package casia.isiteam.springbootshiro.model.po.sysuser.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysPerm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * sys_perm 实体操作类
 * Fri Sep 28 17:40:25 CST 2018
 * @casia
 */ 
public interface SysPermRepository extends JpaRepository<SysPerm,Integer>{
    SysPerm findAllByVal(String Val);
    List<SysPerm> findByTypeIsIn(Integer[] types);

    SysPerm saveAndFlush(SysPerm sysPerm);
    @Transactional
    List<SysPerm> saveAndFlush(List<SysPerm> sysPerms);
    @Transactional
    long deleteAllByValIn(List<String> val);
    @Transactional
    long deleteAllByParentIn(List<String> parent);
    @Transactional
    long deleteByVal(String val);
}

