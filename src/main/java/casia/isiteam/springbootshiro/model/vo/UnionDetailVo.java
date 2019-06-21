package casia.isiteam.springbootshiro.model.vo;

public class UnionDetailVo {
    private Integer taskId;
    private String analysisAccount;
    private String selectedAccount;
    private Integer resultId;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getAnalysisAccount() {
        return analysisAccount;
    }

    public void setAnalysisAccount(String analysisAccount) {
        this.analysisAccount = analysisAccount;
    }

    public String getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(String selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }
}
