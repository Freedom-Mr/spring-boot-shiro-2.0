package casia.isiteam.springbootshiro.model.po.sysuser.tab;

import java.util.Date;

import casia.isiteam.springbootshiro.model.vo.AuthVo;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * sys_user 实体类
 * Fri Sep 28 17:40:25 CST 2018
 * @casia
 */ 
@Entity
@Table(name = "sys_user", schema = "sys_user", catalog = "")
public class SysUser  implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Column(name = "id")
	private int id;

	@NotNull
	@Column(name = "username")
	private String username;

	@NotNull
	@Column(name = "password")
	private String password;

	@NotNull
	@Column(name = "real_name")
	private String realName;

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "email")
	private String email;

	@Column(name = "note")
	private String note;

	@JSONField(name = "deadline", format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "deadline")
	private Date deadline;

	@NotNull
	@Column(name = "status")
	private int status;

	@NotNull
	@Column(name = "unique_login")
	private int uniqueLogin;

	@Column(name = "part")
	private int part;

	@Column(name = "inst_id")
	private int instId;

	@Column(name = "dep_id")
	private int depId;

	@NotNull
	@JSONField(name = "createTime", format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	@NotNull
	@JSONField(name = "updateTime", format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_time")
	private Date updateTime;

	@Transient
	private Set<AuthVo> roles  = new HashSet<>();    //用户所有角色值，在管理后台显示用户的角色
	@Transient
	private Set<AuthVo> perms = new HashSet<>();    //用户所有权限值，用于shiro做资源权限的判断

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id=id;
	}

	public String getUsername(){
		return username;
	}

	public void setUsername(String username){
		this.username=username;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password=password;
	}

	public String getRealName(){
		return realName;
	}

	public void setRealName(String realName){
		this.realName=realName;
	}

	public String getTelephone(){
		return telephone;
	}

	public void setTelephone(String telephone){
		this.telephone=telephone;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email=email;
	}

	public String getNote(){
		return note;
	}

	public void setNote(String note){
		this.note=note;
	}

	public Date getDeadline(){
		return deadline;
	}

	public void setDeadline(Date deadline){
		this.deadline=deadline;
	}

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status=status;
	}

	public int getUniqueLogin(){
		return uniqueLogin;
	}

	public void setUniqueLogin(int uniqueLogin){
		this.uniqueLogin=uniqueLogin;
	}

//	public int getRoleId(){
//		return roleId;
//	}
//
//	public void setRoleId(int roleId){
//		this.roleId=roleId;
//	}

	public int getInstId(){
		return instId;
	}

	public void setInstId(int instId){
		this.instId=instId;
	}

	public int getDepId(){
		return depId;
	}

	public void setDepId(int depId){
		this.depId=depId;
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

	public void setRoles(Set<AuthVo> roles) {
		this.roles = roles;
	}

	public Set<AuthVo> getRoles() {
		return roles;
	}

	public Set<AuthVo> getPerms() {
		return perms;
	}

	public void setPerms(Set<AuthVo> perms) {
		this.perms = perms;
	}


}

