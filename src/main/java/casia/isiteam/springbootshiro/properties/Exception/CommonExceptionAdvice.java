package casia.isiteam.springbootshiro.properties.Exception;

import casia.isiteam.springbootshiro.properties.result.HttpState;
import casia.isiteam.springbootshiro.properties.result.HttpResult;
import casia.isiteam.springbootshiro.properties.result.Result;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthorizedException;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;


/**
 * Author wzy
 * Date 2017/7/31 20:43
 */
@ControllerAdvice
@ResponseBody
public class CommonExceptionAdvice {
    private static Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);

    /**
     * 404 - Not Found
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UnknownAccountException.class)
    public Result handleUnknownAccountException(UnknownAccountException e){
        return new HttpResult().error( HttpState.UNKNOWN_ACCOUNT.code() ,HttpState.UNKNOWN_ACCOUNT.annotation( e.getMessage() ) );
    }
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error( HttpState.MISSING_PARAMETER.annotation() , e);
        return new HttpResult().error( HttpState.MISSING_PARAMETER.code() ,HttpState.MISSING_PARAMETER.annotation( e.getMessage() ) );
    }
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnauthorizedException.class)
    public Result handleUnauthorizedException(UnauthorizedException e) {
        logger.error( HttpState.UNAUTHORIZED.annotation() , e);
        return new HttpResult().error( HttpState.UNAUTHORIZED.code() ,HttpState.UNAUTHORIZED.annotation( e.getMessage() ) );
    }
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error( HttpState.MESSAGE_NOT_READABLE.annotation() , e);
        return new HttpResult().error(HttpState.MESSAGE_NOT_READABLE.code(),HttpState.MESSAGE_NOT_READABLE.annotation( e.getMessage() ) );
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error( HttpState.PARAMETER_VALIDATION_FAILED.annotation() , e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return new HttpResult().error( HttpState.PARAMETER_VALIDATION_FAILED.code() ,HttpState.PARAMETER_VALIDATION_FAILED.annotation(message) );
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e) {
        logger.error( HttpState.PARAMETER_BIND_FAILED.annotation() , e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        return new HttpResult().error(HttpState.PARAMETER_BIND_FAILED.code(),HttpState.PARAMETER_BIND_FAILED.annotation(message) );
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleServiceException(ConstraintViolationException e) {
        logger.error(HttpState.PARAMETER_VALIDATION_FAILED.annotation() , e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String message = violation.getMessage();
        return new HttpResult().error(HttpState.PARAMETER_VALIDATION_FAILED.code() ,HttpState.PARAMETER_VALIDATION_FAILED.annotation(message) );
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Result handleValidationException(ValidationException e) {
        logger.error(HttpState.PARAMETER_VALIDATION_FAILED.annotation(), e);
        return new HttpResult().error(HttpState.PARAMETER_VALIDATION_FAILED.code(),HttpState.PARAMETER_VALIDATION_FAILED.annotation( e.getMessage() ));
    }
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.error(HttpState.PARAMETER_VALIDATION_FAILED.annotation(), e);
        return new HttpResult().error(HttpState.PARAMETER_VALIDATION_FAILED.code(),HttpState.PARAMETER_VALIDATION_FAILED.annotation( e.getMessage()));
    }
    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error(HttpState.METHOD_NOT_ALLOWED.annotation(), e);
        return new HttpResult().error(HttpState.METHOD_NOT_ALLOWED.code(),HttpState.METHOD_NOT_ALLOWED.annotation( e.getMessage() ));
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result handleHttpMediaTypeNotSupportedException(Exception e) {
        logger.error(HttpState.UNSUPPORTED_MEDIA_TYPE.annotation(), e);
        return new HttpResult().error(HttpState.UNSUPPORTED_MEDIA_TYPE.code(),HttpState.UNSUPPORTED_MEDIA_TYPE.annotation( e.getMessage() ) );
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException e) {
        logger.error( HttpState.INTERNAL_SERVER_ERROR.annotation() , e);
        return new HttpResult().error( HttpState.INTERNAL_SERVER_ERROR.code() , HttpState.INTERNAL_SERVER_ERROR.annotation( e.getMessage() ) );
    }

    /**
     * 500 - Internal Db Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleException(DataIntegrityViolationException e) {
        logger.error( HttpState.INTERNAL_Db_ERROR.annotation() , e);
        return new HttpResult().error(HttpState.INTERNAL_Db_ERROR.code(),HttpState.INTERNAL_Db_ERROR.annotation( e.getMessage() ) );
    }

    /**
     * 500 - Unknown Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        logger.error( HttpState.UNKNOWN_ERROR.annotation() , e);
        return new HttpResult().error(HttpState.UNKNOWN_ERROR.code(),HttpState.UNKNOWN_ERROR.annotation( e.getMessage() ) );
    }

}
