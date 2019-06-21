package casia.isiteam.springbootshiro.model.vo;

public class UnionListVo {
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    private Integer taskId;
    private String accountId;
    private Integer resultId;
    private Integer pageNum = 1;
    private Integer pageMax = 10;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getAccountId() {
        return accountId;
    }

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
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
