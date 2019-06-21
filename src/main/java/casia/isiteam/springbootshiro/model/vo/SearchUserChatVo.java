package casia.isiteam.springbootshiro.model.vo;

public class SearchUserChatVo {
    private Integer taskId;
    private Integer pageNum;
    private Integer pageMax;
    private boolean action;
    private String keywords;
    private String notKeywords;
    private String account;
    private String friend;
    private String bTime;
    private String eTime;
    private String province;
    private int sort;
    private int highLight;
    private String prefecture;
    private String city;

    public String getPrefecture() {
        return prefecture;
    }

    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setNotKeywords(String notKeywords) {
        this.notKeywords = notKeywords;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getNotKeywords() {
        return notKeywords;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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

    public boolean isAction() {
        return action;
    }

    public void setAction(boolean action) {
        this.action = action;
    }

    public String getbTime() {
        return bTime;
    }

    public void setbTime(String bTime) {
        this.bTime = bTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getHighLight() {
        return highLight;
    }

    public void setHighLight(int highLight) {
        this.highLight = highLight;
    }
}
