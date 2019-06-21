package casia.isiteam.springbootshiro.model.po.sysuser.tab;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * sys_role_perm 实体类
 * Fri Oct 26 19:25:16 CST 2018
 * @casia
 */ 
@Entity
@Table(name = "sys_role_perm", schema = "sys_user", catalog = "")
public class SysRolePerm  implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Column(name = "id")
	private int id;

	@NotNull
	@Column(name = "role_id")
	private int roleId;

	@NotNull
	@Column(name = "perm_val")
	private String permVal;

	@NotNull
	@Column(name = "type")
	private int type;

	@NotNull
	@JSONField(name = "createTime", format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	public SysRolePerm(){}
	public SysRolePerm(Integer roleId,String permVal,Integer type ,Date createTime){
		this.roleId = roleId;
		this.permVal = permVal;
		this.type = type;
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId(){
		return roleId;
	}

	public void setRoleId(int roleId){
		this.roleId=roleId;
	}

	public String getPermVal(){
		return permVal;
	}

	public void setPermVal(String permVal){
		this.permVal=permVal;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}

