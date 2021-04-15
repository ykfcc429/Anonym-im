package hello.DO;

import lombok.Data;

/**
 * @author yankaifeng
 * 创建日期 2021/4/15
 * @since ZQ001
 */
@Data
public class Room {

    /**
     * 房间名
     */
    private String name;

    /**
     * 房间号
     */
    private Long num;
}
