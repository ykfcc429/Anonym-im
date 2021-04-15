package hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.DO.Result;
import hello.DO.Room;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * @author yankaifeng
 * 创建日期 2021/4/14
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/room")
@Validated
public class RoomController {

    private final RedisTemplate<String,String> redisTemplate;

    @PostMapping("/list")
    public Result<List<String>> roomList(@Valid @NotNull int pageNo,@NotNull int pageSize) throws IOException {
        List<String> room = redisTemplate.opsForList().range("room", (long) (pageNo - 1) * pageSize, (long) pageNo * pageSize);
        return Result.success("ok",room);
    }

    @PostMapping("/newRoom")
    public Result<?> newRoom(@Valid @NotBlank String roomName) throws JsonProcessingException {
        Long roomNum = redisTemplate.opsForValue().increment("roomNum");
        Room room = new Room();
        room.setName(roomName);
        room.setNum(roomNum);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(room);
        redisTemplate.opsForList().leftPush("room",s);
        return Result.success("ok",room);
    }

    @PostMapping("/delRoom")
    public Result<?> delRoom(@Valid @NotNull Long roomNum) throws IOException {
        Result<List<String>> result = roomList(0, -1);
        List<String> data = result.getData();
        if(data!=null){
            ObjectMapper objectMapper = new ObjectMapper();
            for (int i = 0; i < data.size(); i++) {
                Room room = objectMapper.readValue(data.get(i),Room.class);
                if(room.getNum().equals(roomNum)) {
                    data.remove(data.get(i));
                    break;
                }
            }
            redisTemplate.delete("room");
            for (String datum : data) {
                redisTemplate.opsForList().leftPush("room",datum);
            }
        }
        return Result.success("ok",null);
    }
}
