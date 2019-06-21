package casia.isiteam.springbootshiro.model.vo;

public class ListNodesVo {
	private Integer eid;
	private Integer bid;
	private Integer sourceType;
	private Integer nodeType;
	private Integer pageNum;
	private Integer pageMax;

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public Integer getEid() {
		return eid;
	}

	public void setEid(Integer eid) {
		this.eid = eid;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Integer getNodeType() {
		return nodeType;
	}

	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageMax() {
		return pageMax;
	}

	public void setPageMax(Integer pageMax) {
		this.pageMax = pageMax;
	}

}
