package casia.isiteam.springbootshiro.service.systeam;

import casia.isiteam.springbootshiro.constant.PermType;
import casia.isiteam.springbootshiro.model.po.sysuser.rep.SysPermRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.rep.SysRolePermRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysPerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by casia.wzy on 2018/10/16.
 */
@Service
public class PermService {
    private final static Logger logger = LoggerFactory.getLogger(PermService.class);
    @Resource
    private SysPermRepository sysPermRepository;
    @Resource
    private SysRolePermRepository sysRolePermRepository;

    // 查询所有权限
    public List<SysPerm> querAllPerm() {
        return sysPermRepository.findByTypeIsIn(new Integer[]{PermType.MENU,PermType.BUTTON,PermType.API});
    }

    // 根据权限值查询权限
    public SysPerm findByVal(String val) {
        return sysPermRepository.findAllByVal(val);
    }
    // 根据权限类型查询权限
    public List<SysPerm> findByTypes(Integer[] type) {
        return sysPermRepository.findByTypeIsIn(type);
    }
    // 保存权限
    public SysPerm savePerm(SysPerm sysPerm) {
        return sysPermRepository.saveAndFlush(sysPerm);
    }
    // 保存权限
    @Transactional
    public void savePerm(List<SysPerm> sysPerms) {
        sysPerms.forEach(s->{
            s.setCreateTime(new Date());
            s.setUpdateTime(new Date());
        });
        sysPermRepository.saveAll(sysPerms);
    }
    // 删除权限（根据val）
    public long deletePermByVal(String Val){
        return sysPermRepository.deleteByVal(Val);
    }
    // 同步菜单权限
    @Transactional("transactionManagerSysUser")
    public void updatePerm(List<SysPerm> sysPerms) {
        Integer[] integers= new Integer[]{PermType.MENU,PermType.BUTTON,PermType.API};
        List<SysPerm> list = querAllPerm();

        Set<String> setSysPerms = new HashSet<>();
        //获取保存及其修改权限集合
        sysPerms.stream().filter(s-> Arrays.asList(integers).contains(s.getType()) ).forEach( s->{
            setSysPerms.add(s.getVal());
            boolean isadd = true;
            for(SysPerm sysPerm : list ){
                if( sysPerm.getVal().equals( s.getVal() ) ){
                    if( sysPerm.getType() != s.getType() || !sysPerm.getName().equals(s.getName()) ||  sysPerm.getSys()!=s.getSys() || sysPerm.getHidden()!=s.getHidden() ||
                            !(sysPerm.getParent() == null ? "" :sysPerm.getParent() ).equals(sysPerm.getParent() == null ? "" :sysPerm.getParent() )
                            || sysPerm.getLeaf() != s.getLeaf() ){
                        //修改
                        s.setCreateTime(sysPerm.getCreateTime());
                        s.setUpdateTime(new Date());
//                        save_update_datas.add(s);
                        sysPermRepository.saveAndFlush(s);
                    }
                    isadd = false;
                    break;
                }
            }
            if( isadd ){
                s.setCreateTime(new Date());
                s.setUpdateTime(new Date());
//                save_update_datas.add(s);
                //保存
                sysPermRepository.saveAndFlush(s);
            }
        });

        //删除权限集合
        List<String> delVal = new ArrayList<>();
        list.stream().filter( s-> s.getType() == PermType.MENU && !setSysPerms.contains(s.getVal()) && !delVal.contains(s.getVal()) ).forEach( s->{
            delVal.add(s.getVal());
        });
        //执行更新保存权限
//        sysPermRepository.saveAndFlush(save_update_datas);
        //执行删除权限
        sysPermRepository.deleteAllByValIn(delVal);
        sysPermRepository.deleteAllByParentIn(delVal);
    }
}
