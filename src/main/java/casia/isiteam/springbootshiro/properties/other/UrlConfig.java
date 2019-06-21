package casia.isiteam.springbootshiro.properties.other;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by casia.wzy on 2018/12/12
 */
@Component
@ConfigurationProperties(prefix = "address")
public class UrlConfig {
    private  String  excelUploadUrl;
    private  String  fileDownIp;
    private  String  eventUploadUrl;

    public String getExcelUploadUrl() {
        return excelUploadUrl;
    }

    public void setExcelUploadUrl(String excelUploadUrl) {
        this.excelUploadUrl = excelUploadUrl;
    }

    public String getFileDownIp() {
        return fileDownIp;
    }

    public void setFileDownIp(String fileDownIp) {
        this.fileDownIp = fileDownIp;
    }

	public String getEventUploadUrl() {
		return eventUploadUrl;
	}

	public void setEventUploadUrl(String eventUploadUrl) {
		this.eventUploadUrl = eventUploadUrl;
	}
    
}

