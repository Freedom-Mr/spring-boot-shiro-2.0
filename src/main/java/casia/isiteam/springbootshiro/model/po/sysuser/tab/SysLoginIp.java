package casia.isiteam.springbootshiro.model.po.sysuser.tab;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * sys_login_ip 实体类
 * Tue Jan 29 14:52:11 CST 2019
 * @casia
 */ 
@Entity
@Table(name = "sys_login_ip", schema = "sys_user", catalog = "")
public class SysLoginIp  implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Column(name = "id")
	private int id;

	@NotNull
	@Column(name = "user_id")
	private int userId;

	@NotNull
	@Column(name = "ip")
	private String ip;

	@NotNull
	@JSONField(name="loginTime",format="yyyy-MM-dd HH:mm:ss")
	@Column(name = "login_time")
	private Date loginTime;


	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id=id;
	}

	public int getUserId(){
		return userId;
	}

	public void setUserId(int userId){
		this.userId=userId;
	}

	public String getIp(){
		return ip;
	}

	public void setIp(String ip){
		this.ip=ip;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
}

