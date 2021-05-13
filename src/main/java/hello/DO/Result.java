package hello.DO;

import lombok.Data;

/**
 * @author yankaifeng
 * 创建日期 2021/4/14
 */
@Data
public class Result<F> {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private F data;

    public Result(int code,String message,F f){
        this.code = code;
        this.message = message;
        this.data = f;
    }

    public static <F> Result<F> success(String message,F f){
        return new Result<>(233,message,f);
    }

    public static <F> Result<F> success(F f){
        return success("success",f);
    }

    public static <F> Result<F> error(int code,String message){
        return new Result<>(code,message,null);
    }
}
