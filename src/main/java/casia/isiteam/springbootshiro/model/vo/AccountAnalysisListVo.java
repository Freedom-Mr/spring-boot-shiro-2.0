package casia.isiteam.springbootshiro.model.vo;

public class AccountAnalysisListVo {
	private int eid;
	private Integer msgType;
	private Integer analysisType;
	private int rows;

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public Integer getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(Integer analysisType) {
		this.analysisType = analysisType;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}
