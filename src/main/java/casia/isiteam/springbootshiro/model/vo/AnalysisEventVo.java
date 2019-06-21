package casia.isiteam.springbootshiro.model.vo;

import java.util.Date;

public class AnalysisEventVo {
	private Integer id; //主键id
	private String name;
	private String userName;//用户名
	private String note;
	private int batchTime;//上传批次
	private int uploadFileNum;//上传文件总数
	private int failedFileNum;//上传文件失败总数
	private int status;//1-为分析中
	private Date createTime;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getBatchTime() {
		return batchTime;
	}
	public void setBatchTime(int batchTime) {
		this.batchTime = batchTime;
	}
	public int getUploadFileNum() {
		return uploadFileNum;
	}
	public void setUploadFileNum(int uploadFileNum) {
		this.uploadFileNum = uploadFileNum;
	}
	public int getFailedFileNum() {
		return failedFileNum;
	}
	public void setFailedFileNum(int failedFileNum) {
		this.failedFileNum = failedFileNum;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
