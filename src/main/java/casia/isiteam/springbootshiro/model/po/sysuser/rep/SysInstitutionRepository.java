package casia.isiteam.springbootshiro.model.po.sysuser.rep;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysInstitution;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * sys_institution 实体操作类
 * Fri Sep 28 17:40:25 CST 2018
 * @casia
 */ 
public interface SysInstitutionRepository extends JpaRepository<SysInstitution,Integer>{
    @Transactional(timeout = 10)//事务，超时10秒
    @Modifying
    SysInstitution saveAndFlush(SysInstitution institution);

    /**
     * 查询单位根据ID
     * @param id
     * @return
     */
    List<SysInstitution> findAllById(int id);
    SysInstitution findFirstById(int id);
    /**
     * 查询所有单位
     * @param id
     * @return
     */
    List<SysInstitution> findAllByIdNot(int id);
    /**
     * 查询单位（分页）
     * @param pageable
     * @return
     */
    Page<SysInstitution> findAllByIdNot(int id,Pageable pageable);
    /**
     * 根据单位名模糊查询单位（分页）
     * @param name
     * @param pageable
     * @return
     */
    Page<SysInstitution> findAllByNameLikeAndIdNot(String name ,int id,Pageable pageable);
    /**
     * 删除单位
     */
    @Transactional
    void deleteById(int id);
    /**
     * 根据单位查询部门集合
     * @param id
     * @return
     */
    @Query("SELECT i,d  FROM SysInstitution i , SysDepartment d where i.id=d.instId and i.id=?1 and i.id>0")
    List<Object[]> queryDupByInsti(int id);
    /**
     * 根据部门查询单位部门信息
     * @param id
     * @return
     */
    @Query("SELECT i,d  FROM SysInstitution i , SysDepartment d where i.id=d.instId and d.id=?1 and i.id>0")
    List<Object[]> queryDupById(int id);
    /**
     * 查询所有单位部门集合
     * @return
     */
    @Query("SELECT i,d  FROM SysInstitution i , SysDepartment d where i.id=d.instId and i.id>0")
    List<Object[]> queryAllDup();
}

