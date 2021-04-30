package hello.DO;

import lombok.Data;

@Data
public class HelloMessage {

    /**
     * 发送者
     */
    private String name;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 房间号
     */
    private String roomNum;

    public HelloMessage(String name, String message, String roomNum) {
        this.name = name;
        this.message = message;
        this.roomNum = roomNum;
    }
}
