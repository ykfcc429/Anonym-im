package hello;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class HelloMessage {

    /**
     * 消息内容
     */
    @NotNull
    private String name;

    /**
     * 房间号
     */
    @NotBlank
    private String roomNum;


    public HelloMessage(String name, String roomNum) {
        this.name = name;
        this.roomNum = roomNum;
    }
}
