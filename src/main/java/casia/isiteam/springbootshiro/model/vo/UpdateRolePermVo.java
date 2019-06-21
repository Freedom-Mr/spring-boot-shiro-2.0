package casia.isiteam.springbootshiro.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateRolePermVo implements Serializable{

    private Integer id;
    private Integer type;
    private List<String> vals = new ArrayList<>();
    private List<Integer> syss = new ArrayList<>();
    private List<Integer> hiddens = new ArrayList<>();
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<String> getVals() {
        return vals;
    }

    public void setVals(List<String> vals) {
        this.vals = vals;
    }

    public List<Integer> getSyss() {
        return syss;
    }

    public void setSyss(List<Integer> syss) {
        this.syss = syss;
    }

    public List<Integer> getHiddens() {
        return hiddens;
    }

    public void setHiddens(List<Integer> hiddens) {
        this.hiddens = hiddens;
    }
}
