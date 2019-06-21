package casia.isiteam.springbootshiro.model.po.sysuser.rep;

import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysInstitution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysDepartment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * sys_department 实体操作类
 * Fri Sep 28 17:40:25 CST 2018
 * @casia
 */ 
public interface SysDepartmentRepository extends JpaRepository<SysDepartment,Integer>{
    @Transactional(timeout = 10)//事务，超时10秒
    @Modifying
    SysDepartment saveAndFlush(SysDepartment sysDepartment);
    SysDepartment findFirstById(Integer id);
    /**
     * 查询部门（分页）
     * @param pageable
     * @return
     */
    Page<SysDepartment> findAllByIdNot(int id,Pageable pageable);
    /**
     * 根据部门名模糊查询部门（分页）
     * @param name
     * @param pageable
     * @return
     */
    Page<SysDepartment> findAllByNameLikeAndIdNot(String name , int id, Pageable pageable);
    /**
     * 根据单位查询部门（分页）
     * @param instId
     * @param pageable
     * @return
     */
    Page<SysDepartment> findAllByInstIdAndIdNot(int instId , int id, Pageable pageable);
    /**
     * 根据单位查询部门
     * @param instId
     * @return
     */
    List<SysDepartment> findAllByInstIdIs(int instId);
    /**
     * 根据单位和id查询部门
     * @param instId
     * @return
     */
    List<SysDepartment> findAllByInstIdAndId(int instId,int id);
    /**
     * 根据模糊部门名 与单位ID 查询部门（分页）
     * @param name
     * @param pageable
     * @return
     */
    Page<SysDepartment> findAllByNameLikeAndIdNotAndInstId(String name , int id, int instId, Pageable pageable);
}

