package casia.isiteam.springbootshiro.properties.secretkey;

import casia.isiteam.springbootshiro.util.EncodeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Created by casia.wzy on 2019/2/21
 */
@ControllerAdvice(basePackages = "casia.isiteam.springbootshiro")
public class EncodeResponse implements ResponseBodyAdvice {
    private final static Logger logger = LoggerFactory.getLogger(EncodeResponse.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        boolean encode = false;
        if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
            SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
            encode = serializedField.outEncode();
        }
        if (encode) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                return EncodeUtil.encrypt(result);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行加密出现异常："+e.getMessage());
            }
        }
        return body;
    }
}
