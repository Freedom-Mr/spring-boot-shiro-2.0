package casia.isiteam.springbootshiro.model.po.sysuser.tab;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * sys_white_list 实体类
 * Thu Jan 17 10:39:57 CST 2019
 * @casia
 */ 
@Entity
@Table(name = "sys_white_list", schema = "sys_user", catalog = "")
public class SysWhiteList  implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Column(name = "id")
	private int id;

	@NotNull
	@Column(name = "ip")
	private String ip;

	@NotNull
	@Column(name = "user_id")
	private int userId;

	@NotNull
	@JSONField(name="createTime",format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;


	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id=id;
	}

	public String getIp(){
		return ip;
	}

	public void setIp(String ip){
		this.ip=ip;
	}

	public int getUserId(){
		return userId;
	}

	public void setUserId(int userId){
		this.userId=userId;
	}

	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}

}

