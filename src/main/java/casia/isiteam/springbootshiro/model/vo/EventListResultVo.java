package casia.isiteam.springbootshiro.model.vo;

public class EventListResultVo {
    private int eventListId;
    private Integer pageNum;
    private Integer pageMax;

    public int getEventListId() {
        return eventListId;
    }

    public void setEventListId(int eventListId) {
        this.eventListId = eventListId;
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
