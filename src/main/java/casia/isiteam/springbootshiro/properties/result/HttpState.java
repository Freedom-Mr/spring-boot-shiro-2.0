package casia.isiteam.springbootshiro.properties.result;

/**
 * Author wzy
 * Date 2018/1/17 17:51
 */
public enum HttpState {
    /**
     * 登录错误次数 默认5次
     */
    PREVENTIMPACT_NUMBER( 5, "number" ,"错误登录次数"),
    /**
     * 登录开关
     */
    PREVENTIMPACT_SWITCH( 0 ,"switch","登录状态开启"),
    /**
     * 登录错误上限后禁止操作时间 /s
     */
    PREVENTIMPACT_OUTTIME( 1800 ,"outTime","登录错误上限后禁止操作时间"),

    /*********************    全局状态码     *******************/
    SUCCESS( 7200 ,"Success","请求成功！"),
    FAIL( 7100 ,"Fail","处理失败！"),
    HOME(7777,"Relogin","会话超时或其它地方已登录，请重新登录！"),
    UNKNOWN_ACCOUNT( 7300 ,"Unknown_Account","账户[%s]验证未通过,未知账户！"),
    PASSWORD_ERROR( 7301 ,"Password_Error","账户[%s]验证未通过,密码错误,%s"),
    LOCKED_ACCOUNT( 7302 ,"Locked_Account","账户[%s]验证未通过,账户已锁定（或过期）！"),
    EXCESSIVE_ATTEMPTS( 7303 , "Excessive_Attempts", "账户[%s]验证未通过,登录错误次数达到上限,账户已锁定！"),
    DISABLED_ACCOUNT( 7304 , "Disabled_Account","账户[%s]验证未通过,帐号已经禁止登录！"),
    LOGIN_ERROR( 7305 , "Login_Error","账户[%s]验证未通过,登录错误！"),
    IP_SECURITY( 7306 , "Ip_Secrity","账户[%s]验证未通过,访问IP限制！"),
    IP_BLACkLIST( 7307 , "Ip_Blacklist","非法超频请求操作，IP已加入黑名单，禁止请求，请稍后再试！"),
    NOT_EMPTY( 7400 ,"Not_Empty","参数不能为空！%s"),
    MISSING_PARAMETER( 7401 , "Missing_Parameter","缺少请求参数！required_parameter_is_not_present! %s"),
    UNAUTHORIZED( 7402 ,"Insufficient_permissions","权限不足! required_parameter_is_not_present! %s"),
    MESSAGE_NOT_READABLE( 7403 ,"Message_Not_Readable","参数解析失败! could_not_read_json! %s"),
    PARAMETER_VALIDATION_FAILED( 7404 , "Parameter_Validation_Failed","参数验证失败! Parameter_Validation_Failed! %s"),
    PARAMETER_BIND_FAILED( 7405 , "Parameter_Bind_Failed","参数绑定失败! Parameter_Validation_Failed! %s"),
    METHOD_NOT_ALLOWED( 7406 , "Method_Not_Allowed","不支持当前请求方法! request_method_not_supported! %s"),
    UNSUPPORTED_MEDIA_TYPE( 7407 , "Unsupported_Media_Type" ,"不支持当前媒体类型! content_type_not_supported! %s"),
    INTERNAL_SERVER_ERROR( 7500 ,"Internal_Server_Error" ,"业务逻辑异常! %s"),
    INTERNAL_Db_ERROR( 7501 ,"Internal_Db_Error" ,"操作数据库出现异常! %s"),
    UNKNOWN_ERROR( 7600 ,"Unknown_Error" ,"通用异常! %s");

    private final Integer Code;
    private final String reasonPhrase;
    private final String Annotation;
    private HttpState(Integer Code, String ReasonPhrase , String Annotation) {
        this.Code = Code;
        this.reasonPhrase = ReasonPhrase;
        this.Annotation = Annotation;
    }
    public Integer code() {
        return this.Code;
    }
    public String reasonPhrase() {
        return this.reasonPhrase;
    }
    public String annotation() {
        return this.Annotation.replaceAll("%s","");
    }
    public String annotation(String text) {
        return String.format(this.Annotation,text).replaceAll("%s","");
    }
    public String annotation(String text,String texts) {
        return String.format(this.Annotation,text,texts).replaceAll("%s","");
    }
}
