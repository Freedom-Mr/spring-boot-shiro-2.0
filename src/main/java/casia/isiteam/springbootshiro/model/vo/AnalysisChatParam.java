package casia.isiteam.springbootshiro.model.vo;

import java.util.Date;

public class AnalysisChatParam {
	public int eventId;
	public Integer msgType;
	public int searchType;
	public String keyWords;
	public String unKeyWords;
	public String account;
	public String vId;
	public String groupId;
	public Date beginTime;
	public Date endTime;
	public String district;
	public String province;
	public String city;
	public boolean Abroad;
	public int pageNum;
	public int pageSize;
	public int sort;
	public String rid;
	public int highLight;

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public int getSearchType() {
		return searchType;
	}
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getUnKeyWords() {
		return unKeyWords;
	}
	public void setUnKeyWords(String unKeyWords) {
		this.unKeyWords = unKeyWords;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getvId() {
		return vId;
	}
	public void setvId(String vId) {
		this.vId = vId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public boolean getAbroad() {
		return Abroad;
	}
	public void setAbroad(boolean abroad) {
		Abroad = abroad;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isAbroad() {
		return Abroad;
	}

	public int getHighLight() {
		return highLight;
	}

	public void setHighLight(int highLight) {
		this.highLight = highLight;
	}
}
