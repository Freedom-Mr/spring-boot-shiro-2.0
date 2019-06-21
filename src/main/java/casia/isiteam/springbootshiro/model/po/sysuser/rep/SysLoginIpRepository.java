package casia.isiteam.springbootshiro.model.po.sysuser.rep;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysLoginIp;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * sys_login_ip 实体操作类
 * Tue Jan 29 14:52:11 CST 2019
 * @casia
 */ 
public interface SysLoginIpRepository extends JpaRepository<SysLoginIp,Integer>{
    List<SysLoginIp> findByUserId(int user_id, Pageable pageable);
    List<SysLoginIp> findByUserIdAndLoginTimeGreaterThanEqual(int user_id, Date loginTime, Sort sort);
    @Query("select s.ip,count(s) as c from SysLoginIp s where s.userId = ?1 and s.loginTime >= ?2 group by s.ip")
    List<Object[]> findByIpGroup(int user_id, Date loginTime);

    @Transactional
    @Modifying
    SysLoginIp save(SysLoginIp sysLoginIp);
}

