package casia.isiteam.springbootshiro.model.vo;

public class SearchUserChatAccountListVo {
    private Integer taskId;
    private boolean action;
    private Integer pageNum = 1;
    private Integer pageMax = 10;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
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
