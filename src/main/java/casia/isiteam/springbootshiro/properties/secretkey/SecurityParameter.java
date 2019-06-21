package casia.isiteam.springbootshiro.properties.secretkey;

import java.lang.annotation.*;
import org.springframework.web.bind.annotation.Mapping;
/**
 * Created by casia.wzy on 2019/2/21
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface SecurityParameter {
    /**
     * 入参是否解密，默认解密
     */
    boolean inDecode() default false;

    /**
     * 出参是否加密，默认加密
     */
    boolean outEncode() default false;
}
