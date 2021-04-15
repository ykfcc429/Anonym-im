package hello.DO;

import lombok.Data;

@Data
public class HelloMessage {

    /**
     * 消息内容
     */
    private String name;

    /**
     * 房间号
     */
    private Long roomNum;

    public HelloMessage(String name, Long roomNum) {
        this.name = name;
        this.roomNum = roomNum;
    }
}
