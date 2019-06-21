package casia.isiteam.springbootshiro.model.po.sysuser.tab;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * sys_role 实体类
 * Fri Sep 28 17:40:25 CST 2018
 * @casia
 */ 
@Entity
@Table(name = "sys_role", schema = "sys_user", catalog = "")
public class SysRole  implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Column(name = "id")
	private int id;

	@NotNull
	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "grade")
	private Integer grade;

	@NotNull
	@Column(name = "part")
	private Integer part;

	@NotNull
	@Column(name = "inst_id")
	private Integer instId;

	@NotNull
	@Column(name = "dep_id")
	private Integer depId;

	@NotNull
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "rdesc")
	private String rdesc;

	@NotNull
	@JSONField(name = "createTime", format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	@NotNull
	@JSONField(name = "updateTime", format="yyyy-MM-dd HH:mm:ss")
//	@JsonFormat(pattern = "YYYY-MM-dd HH:mm")
	@Column(name = "update_time")
	private Date updateTime;

	@ManyToMany
	@JoinTable(name = "sys_role_perm",joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "perm_val"))
	private List<SysPerm> perms;

//	@OneToOne
//	@JoinColumn(name = "grade", referencedColumnName="id", unique = false)
//	private SysGrade sysGrade;


	public void setPerms(List<SysPerm> perms) {
		this.perms = perms;
	}

	public List<SysPerm> getPerms() {
		return perms;
	}

	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id=id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name=name;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getPart() {
		return part;
	}

	public void setPart(Integer part) {
		this.part = part;
	}

	public Integer getInstId() {
		return instId;
	}

	public void setInstId(Integer instId) {
		this.instId = instId;
	}

	public Integer getDepId() {
		return depId;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRdesc(){
		return rdesc;
	}

	public void setRdesc(String rdesc){
		this.rdesc=rdesc;
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

}

