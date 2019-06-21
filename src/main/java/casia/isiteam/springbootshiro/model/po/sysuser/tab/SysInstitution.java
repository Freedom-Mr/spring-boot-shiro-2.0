package casia.isiteam.springbootshiro.model.po.sysuser.tab;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * sys_institution 实体类
 * Fri Sep 28 17:40:25 CST 2018
 * @casia
 */ 
@Entity
@Table(name = "sys_institution", schema = "sys_user", catalog = "")
public class SysInstitution  implements Serializable {

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

	@Column(name = "telephone")
	private String telephone;

	@Column(name = "fax")
	private String fax;

	@Column(name = "address")
	private String address;

	@Column(name = "zip_code")
	private String zipCode;

	@Column(name = "email")
	private String email;

	@Column(name = "web")
	private String web;

	@Column(name = "note")
	private String note;

	@NotNull
	@JSONField(name = "createTime", format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;


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

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address=address;
	}

	public String getZipCode(){
		return zipCode;
	}

	public void setZipCode(String zipCode){
		this.zipCode=zipCode;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email=email;
	}

	public String getWeb(){
		return web;
	}

	public void setWeb(String web){
		this.web=web;
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

}

