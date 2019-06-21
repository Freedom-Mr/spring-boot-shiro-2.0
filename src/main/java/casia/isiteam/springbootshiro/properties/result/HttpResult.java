package casia.isiteam.springbootshiro.properties.result;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author wzy
 * Date 2017/7/31 21:37
 */
public class HttpResult {
    public synchronized static Result success( Object data ){
        Result result = new Result();
        result.setCode( HttpState.SUCCESS.code());
        result.setMsg( HttpState.SUCCESS.annotation() );
        result.setData(data);
        return result;
    }
    public synchronized static Result success( String msg , Object data ){
        Result result = new Result();
        result.setCode( HttpState.SUCCESS.code());
        result.setMsg( msg );
        result.setData(data);
        return result;
    }
    public synchronized static Result success(){
        return success(null);
    }

    public synchronized static Result fail( String msg ){
        Result result = new Result();
        result.setCode( HttpState.FAIL.code());
        result.setMsg( msg );
        result.setData(null);
        return result;
    }
    public synchronized static Result fail( String msg , Object data ){
        Result result = new Result();
        result.setCode( HttpState.FAIL.code());
        result.setMsg( msg );
        result.setData(data);
        return result;
    }

    public synchronized static Result error (Integer code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
    public synchronized static Result error (Integer code,String msg,String data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}
