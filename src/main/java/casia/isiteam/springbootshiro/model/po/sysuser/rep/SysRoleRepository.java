package casia.isiteam.springbootshiro.model.po.sysuser.rep;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysRole;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * sys_role 实体操作类
 * Fri Sep 28 17:40:25 CST 2018
 * @casia
 */ 
public interface SysRoleRepository extends JpaRepository<SysRole,Integer>{
    @Transactional(timeout = 10)//事务，超时10秒
    @Modifying
    SysRole saveAndFlush(SysRole sysRole);

    @Query("update  SysRole r set r.name=?1,r.rdesc=?2 where r.id=?3")
    @Transactional
    @Modifying
    void updateSysRole(String name, String rdesc , int id);

    @Transactional(timeout = 10)//事务，超时10秒
    @Modifying
    void deleteById(int id);
    @Transactional(timeout = 10)//事务，超时10秒
    @Modifying
    void deleteByIdAndGradeAndPart(int id,int grad,int part);

    SysRole findFirstById(Integer id);
    SysRole findFirstByInstId(Integer instId);
    SysRole findFirstByDepId(Integer depId);
    SysRole findFirstByUserId(Integer userId);
    SysRole findFirstByGradeAndInstIdAndDepId(Integer grade,Integer instId,Integer depId);
    SysRole findFirstByGradeAndPartAndInstIdAndDepId(Integer grade,Integer part,Integer instId,Integer depId);
    @Query("select s from SysRole s where (s.instId = ?1 and s.depId in ?2) or s.userId = ?3 ")
    List<SysRole> findAll(Integer instId ,Integer[] depIds,Integer userId);
    @Query("select s from SysRole s where s.grade=-1 and s.part=-1 and (s.instId = ?1 or s.depId in ?2 ) ")
    List<SysRole> findAllLimtPerms(Integer instId ,Integer depId);
    Page<SysRole> findAllByIdNot(int id, Pageable pageable);
    Page<SysRole> findAllByGrade(int grade,Pageable pageable);
    Page<SysRole> findAllByGradeAndNameLike(int grade,String name,Pageable pageable);
    Page<SysRole> findAllByNameLike(String name,Pageable pageable);
    Page<SysRole> findAll( Pageable pageable);

//    //更新
//    @Transactional(timeout = 10)//事务，超时10秒
//    @Query(value = "update SysRole p set p.name=?1,p.grade=?2,p.rdesc=?3 where p.id=?4 ")
//    @Modifying
//    int update(String name,Integer grade, String rdesc , int id);
}

