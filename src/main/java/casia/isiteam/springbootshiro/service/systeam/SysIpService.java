package casia.isiteam.springbootshiro.service.systeam;

import casia.isiteam.springbootshiro.model.po.sysuser.rep.SysLoginIpRepository;
import casia.isiteam.springbootshiro.model.po.sysuser.tab.SysLoginIp;
import casia.isiteam.springbootshiro.util.TimeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by casia.wzy on 2018/12/10
 */
@Service
public class SysIpService {
    private final static Logger logger = LoggerFactory.getLogger(SysIpService.class);

    @Resource
    private SysLoginIpRepository sysLoginIpRepository;
    /**
     * 根据用户ID查询登陆详情
     * @param id
     * @return
     */
    public JSONObject findLoginInfoByUserId(Integer id, Integer grading) {
        JSONObject jsonObject = new JSONObject();
        Sort sort= new Sort(Sort.Direction.ASC, "loginTime");
        List<SysLoginIp> list = sysLoginIpRepository.findByUserIdAndLoginTimeGreaterThanEqual(id, TimeUtil.addSubtractMonth(-1),sort);
        JSONArray jsonArray_1 = new JSONArray();
        JSONArray jsonArray_2 = new JSONArray();
        SimpleDateFormat sdf_1 = new SimpleDateFormat("MM/dd");
        Map<String,Integer> map = new LinkedHashMap<>();
        list.forEach(s->{
           String date = sdf_1.format(s.getLoginTime());
            map.put(date,map.containsKey(date) ? map.get(date)+1 : 1 );
        });
        map.forEach((k,v)->{
            jsonArray_2.add(v);
            jsonArray_1.add(k);
        });
        jsonObject.put("xAxis_data",jsonArray_1);
        jsonObject.put("series_data",jsonArray_2);
        return jsonObject;
    }
    /**
     * 根据用户ID查询IP分布
     * @param id
     * @return
     */
    public JSONObject findLoginIpByUserId(Integer id, Integer grading) {
        JSONObject jsonObject = new JSONObject();
        List<Object[]> list = sysLoginIpRepository.findByIpGroup(id, TimeUtil.addSubtractMonth(-1));
        JSONArray jsonArray_1 = new JSONArray();
        JSONArray jsonArray_2 = new JSONArray();
        list.forEach(s->{
            jsonArray_1.add(s[0]);
            JSONObject json = new JSONObject();
            json.put("value",s[1]);
            json.put("name",s[0]);
            jsonArray_2.add(json);
        });
        jsonObject.put("legend_data",jsonArray_1);
        jsonObject.put("series_data",jsonArray_2);
        return jsonObject;
    }
}
