package casia.isiteam.springbootshiro.properties.result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Author wzy
 * Date 2017/7/31 21:28
 */
public class Result<T> {

    /** 状态码 */
    private Integer Code;
    /** 提示信息 */
    private String msg;
    /** 结果内容 */
    private T data;
    /** 时间戳 **/
    private String timestamp;

    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        Code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
