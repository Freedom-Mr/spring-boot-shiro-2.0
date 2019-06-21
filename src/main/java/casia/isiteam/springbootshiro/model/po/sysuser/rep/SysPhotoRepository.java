package casia.isiteam.springbootshiro.model.po.sysuser.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysPhoto;
import org.springframework.transaction.annotation.Transactional;

/**
 * sys_photo 实体操作类
 * Tue Jan 22 11:52:04 CST 2019
 * @casia
 */ 
public interface SysPhotoRepository extends JpaRepository<SysPhoto,Integer>{
    SysPhoto findFirstByUserId(int userId);
    @Transactional
    void deleteAllByUserId(int userId);
}

