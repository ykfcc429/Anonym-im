package hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.DO.Result;
import hello.DO.Room;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
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
    public Result<List<Room>> roomList(@Valid @NotNull Integer pageNo,@NotNull Integer pageSize) throws IOException {
        List<String> room = redisTemplate.opsForList().range("room", (long) (pageNo - 1) * pageSize, (long) pageNo * pageSize);
        List<Room> list = null;
        if(room!=null) {
            list = new ArrayList<>(room.size());
            ObjectMapper objectMapper = new ObjectMapper();
            for (String s : room) {
                Room room1 = objectMapper.readValue(s,Room.class);
                list.add(room1);
            }
        }
        return Result.success("ok",list);
    }

    @PostMapping("/newRoom")
    public Result<?> newRoom(@Valid @NotBlank String roomName) throws JsonProcessingException {
        Long roomNum = redisTemplate.opsForValue().increment("roomNum");
        Room room = new Room();
        room.setName(roomName);
        room.setNum(roomNum);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(room);
        redisTemplate.opsForList().rightPush("room",s);
        return Result.success("ok",room);
    }

    @PostMapping("/delRoom")
    public Result<?> delRoom(@Valid @NotNull Long roomNum) throws IOException {
        Result<List<Room>> result = roomList(1, Integer.MAX_VALUE);
        List<Room> data = result.getData();
        int deleted = 0;
        if(data!=null && !data.isEmpty()){
            for (int i = 0; i < data.size(); i++) {
                if(data.get(i).getNum().equals(roomNum)) {
                    data.remove(data.get(i));
                    deleted++;
                    break;
                }
            }
            if(deleted>0) {
                redisTemplate.delete("room");
                ObjectMapper objectMapper = new ObjectMapper();
                for (Room datum : data) {
                    redisTemplate.opsForList().rightPush("room", objectMapper.writeValueAsString(datum));
                }
            }
        }
        return Result.success("deleted "+ deleted +" pieces of data",null);
    }

    @GetMapping("/randomName")
    public Result<?> setToRedis(){
        String adj = redisTemplate.opsForSet().randomMember("adj");
        String noun = redisTemplate.opsForSet().randomMember("noun");
        return Result.success(adj+"的"+noun,null);
    }
}
