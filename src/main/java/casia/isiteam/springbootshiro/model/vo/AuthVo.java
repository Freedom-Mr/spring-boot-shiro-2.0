package casia.isiteam.springbootshiro.model.vo;

import com.alibaba.fastjson.JSON;

import java.util.Objects;

/**
 * Created by casia.wzy on 2018/9/28.
 */
public class AuthVo {
    private String name;//显示名
    private String val;//值

    public AuthVo(){};
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public AuthVo(String name, String val) {
        this.name = name;
        this.val = val;
    }
    @Override
    public int hashCode() {
        return Objects.hash(val);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
