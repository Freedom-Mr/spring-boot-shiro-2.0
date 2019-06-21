package casia.isiteam.springbootshiro.model.po.sysuser.tab;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * sys_department 实体类
 * Fri Sep 28 17:40:25 CST 2018
 * @casia
 */ 
@Entity
@Table(name = "sys_department", schema = "sys_user", catalog = "")
public class SysDepartment  implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Column(name = "id")
	private int id;

	@NotNull
	@Column(name = "name")
	private String name;

	@Column(name = "leader")
	private String leader;

	@Column(name = "contacter")
	private String contacter;

	@Column(name = "address")
	private String address;

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "fax")
	private String fax;

	@Column(name = "email")
	private String email;

	@Column(name = "note")
	private String note;

	@NotNull
	@JSONField(name = "createTime", format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	@NotNull
	@Column(name = "inst_id")
	private int instId;

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

	public String getLeader(){
		return leader;
	}

	public void setLeader(String leader){
		this.leader=leader;
	}

	public String getContacter(){
		return contacter;
	}

	public void setContacter(String contacter){
		this.contacter=contacter;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getTelephone(){
		return telephone;
	}

	public void setTelephone(String telephone){
		this.telephone=telephone;
	}

	public String getFax(){
		return fax;
	}

	public void setFax(String fax){
		this.fax=fax;
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

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

	public int getInstId(){
		return instId;
	}

	public void setInstId(int instId){
		this.instId=instId;
	}

}

