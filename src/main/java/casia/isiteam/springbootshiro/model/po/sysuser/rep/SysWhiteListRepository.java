package casia.isiteam.springbootshiro.model.po.sysuser.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysWhiteList;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * sys_white_list 实体操作类
 * Thu Jan 17 10:39:57 CST 2019
 * @casia
 */ 
public interface SysWhiteListRepository extends JpaRepository<SysWhiteList,Integer>{
    List<SysWhiteList> queryAllByUserId(Integer userId);

    @Transactional
    @Modifying
    void deleteAllByUserId(Integer userId);

    @Transactional
    @Modifying
    SysWhiteList saveAndFlush(SysWhiteList sysWhiteList);

    @Transactional
    @Modifying
    void deleteAllByIdIn(List<Integer> ids);
}

