package hello.controllAdvice;

import hello.DO.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 参数校验异常处理
 */
@Slf4j
@RestControllerAdvice(basePackages = {"hello.controller"}, annotations = {RestController.class})
@Order(1)
public class ParamExceptionControllerAdvice {

    /**
     * 参数校验异常处理
     *
     * @param e 参数校验异常
     * @return 包含异常描述的响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("参数校验发生异常", e);
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                FieldError fieldError = (FieldError) errors.get(0);
                return Result.error(4001,
                        fieldError.getField() + " " +
                                fieldError.getDefaultMessage());
            }
        }
        return Result.error(4001,"参数校验异常处理!");
    }

    /**
     * 参数校验异常处理
     *
     * @param e 参数校验异常
     * @return 包含异常描述的响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("参数校验发生异常", e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return Result.error(4002,"参数校验异常");
        }
        Optional<ConstraintViolation<?>> first = constraintViolations.stream().findFirst();
        if (first.isPresent()) {
            ConstraintViolation<?> constraintViolation = first.get();
            String message = constraintViolation.getMessage();
            String path = constraintViolation.getPropertyPath().toString();
            return Result.error(4002,
                    path.substring(path.indexOf(".", path.indexOf(".") + 1) + 1) + " " +
                            message);
        } else {
            return Result.error(4002,"参数校验异常");
        }
    }

    /**
     * 忽略参数异常处理器
     *
     * @param e 忽略参数异常
     * @return 包含异常信息的响应
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> parameterMissingExceptionHandler(MissingServletRequestParameterException e) {
        log.error("接口抛出忽略参数异常", e);
        return Result.error(4002, "请求参数 " + e.getParameterName() + " 不能为空");
    }
}
