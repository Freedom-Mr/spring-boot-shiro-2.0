package casia.isiteam.springbootshiro.model.po.sysuser.tab;

import com.alibaba.fastjson.annotation.JSONField;
import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Blob;

/**
 * sys_photo 实体类
 * Tue Jan 22 11:52:04 CST 2019
 * @casia
 */ 
@Entity
@Table(name = "sys_photo", schema = "sys_user", catalog = "")
public class SysPhoto  implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Column(name = "id")
	private int id;

	@NotNull
	@Column(name = "user_id")
	private int userId;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@NotNull
	@Column(name = "photo",columnDefinition="longblob")
	private byte[] photo;

	@NotNull
	@Column(name = "format")
	private String format;

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

	public byte[] getPhoto(){
		return photo;
	}

	public void setPhoto(byte[] photo){
		this.photo=photo;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}

