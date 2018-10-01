package com.example.exception;

import com.example.common.ajax.AjaxResult;
import com.example.common.ajax.CallResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AjaxResult<String>> handleExceptions(HttpServletRequest request, Exception e) {
        AjaxResult<String> result = new AjaxResult<>();
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        result.setRst(CallResult.FAILURE.getCode());
        result.setException(e.getClass().getName());
        StringBuilder msg = new StringBuilder();

        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                msg.append(item.getMessage());
                break;
            }
            result.setMsg(msg.toString());
            log.info("字段属性数据校验不合法：{}", ex.getMessage());
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
            if (!fieldErrors.isEmpty()) {
                msg.append(fieldErrors.get(0).getDefaultMessage());
            }
            result.setMsg(msg.toString());
            log.info("Bean数据校验不合法：{}", ex.getMessage());
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
            if (!fieldErrors.isEmpty()) {
                msg.append(fieldErrors.get(0).getDefaultMessage());
            }
            result.setMsg(msg.toString());
            log.info("ModelAttribute数据校验不合法：{}", ex.getMessage());
        } else if (e instanceof TokenInvalidException) {
            result.setStatus(HttpStatus.UNAUTHORIZED.value());
            result.setMsg(e.getMessage());
        } else {
            result.setError(e.getMessage());
            log.error("服务调用出错：{}", e.getMessage());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
