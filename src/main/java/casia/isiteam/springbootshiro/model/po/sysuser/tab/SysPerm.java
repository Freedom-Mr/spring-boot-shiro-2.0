package casia.isiteam.springbootshiro.model.po.sysuser.tab;

import java.util.ArrayList;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * sys_perm 实体类
 * Fri Sep 28 17:40:25 CST 2018
 * @casia
 */ 
@Entity
@Table(name = "sys_perm", schema = "sys_user", catalog = "")
public class SysPerm  implements Serializable {

	@Id
	@NotNull
	@Column(name = "val")
	private String val;

	@Column(name = "parent")
	private String parent;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private int type;

	@Column(name = "leaf")
	private int leaf;

	@Column(name = "sys")
	private int sys;

	@Column(name = "hidden")
	private int hidden;

	@NotNull
	@JSONField(name = "createTime", format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	@NotNull
	@JSONField(name = "update_time", format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_time")
	private Date updateTime;

	@ManyToMany
	@JoinTable(name = "sys_role_perm",joinColumns = @JoinColumn(name = "perm_val"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<SysRole> roles;

	@Transient
	private List<SysPerm> children = new ArrayList<>();

	public List<SysPerm> getChildren() {
		return children;
	}

	public void setRoles(List<SysRole> roles) {
		this.roles = roles;
	}

	public void setChildren(List<SysPerm> children) {
		this.children = children;
	}

	public List<SysRole> getRoles() {
		return roles;
	}

	public String getVal(){
		return val;
	}

	public void setVal(String val){
		this.val=val;
	}

	public String getParent(){
		return parent;
	}

	public void setParent(String parent){
		this.parent=parent;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name=name;
	}

	public int getType(){
		return type;
	}

	public void setType(int type){
		this.type=type;
	}

	public int getLeaf(){
		return leaf;
	}

	public void setLeaf(int leaf){
		this.leaf=leaf;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}

	public int getSys() {
		return sys;
	}

	public void setSys(int sys) {
		this.sys = sys;
	}

	public int getHidden() {
		return hidden;
	}

	public void setHidden(int hidden) {
		this.hidden = hidden;
	}
}

