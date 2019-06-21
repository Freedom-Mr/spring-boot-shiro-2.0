package casia.isiteam.springbootshiro.model.vo;

import java.util.List;

public class SaveEventListVo {
	private List<String> ids;// 选择的账号列表
	private Integer bid;// 批次id
	private boolean selectAll;// 是否全量 全选传ids
	private Integer eid;
	private Integer eventListId;// analysis_event_list表的id
	private Integer resultId;// 结果id,
	private String name;// 分析列表名称

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public boolean isSelectAll() {
		return selectAll;
	}

	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}

	public Integer getEid() {
		return eid;
	}

	public void setEid(Integer eid) {
		this.eid = eid;
	}

	public Integer getEventListId() {
		return eventListId;
	}

	public void setEventListId(Integer eventListId) {
		this.eventListId = eventListId;
	}

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
