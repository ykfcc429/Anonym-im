package hello.controllAdvice;

import hello.DO.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yankaifeng
 * 创建日期 2021/3/26
 * @since ZQ001
 */
@Slf4j
@RestControllerAdvice(basePackages = {"hello.controller"},annotations = {RestController.class})
@Order(2)
public class RuntimeExceptionControllerAdvice {

    /**
     * 处理未处理的异常
     *
     * @param e 未处理的异常
     * @return 友好提示的异常返回信息
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> exceptionHandler(RuntimeException e) {
        log.error("请求抛出未捕获异常", e);
        return Result.error(5001, e.getMessage());
    }
}
