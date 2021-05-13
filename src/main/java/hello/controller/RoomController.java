package hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.DO.Result;
import hello.DO.Room;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Api(value = "房间管理操作接口", tags = "房间管理操作接口")
@ApiResponses({@ApiResponse(code = 233, message = "成功")
        , @ApiResponse(code = 4002, message = "参数校验错误")
        , @ApiResponse(code = 5001, message = "系统错误")})
public class RoomController {

    private final RedisTemplate<String,String> redisTemplate;

    @PostMapping("/list")
    @ApiOperation(value = "获取房间列表", notes = "通过分页参数获取房间列表")
    public Result<List<Room>> roomList(@NotNull @RequestParam("pageNo")@ApiParam(name = "pageNo", value = "页码") int pageNo,
                                       @NotNull @RequestParam("pageSize")@ApiParam(name = "pageSize", value = "每页数据条数") int pageSize) throws IOException {
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
    @ApiOperation(value = "增加一个新房间", notes = "传入房间名字来增加一个新房间,返回值中包含生成的房间号")
    public Result<?> newRoom(@Valid @NotBlank @RequestParam @ApiParam(name = "roomName", value = "房间名")  String roomName) throws JsonProcessingException {
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
    @ApiOperation(value = "删除一个房间", notes = "通过房间号删除一个房间,房间删除后无法通过房间列表获取,如果有用户在已删除的房间内仍然可以继续聊天")
    public Result<?> delRoom(@Valid @NotNull @RequestParam @ApiParam(name = "roomNum", value = "房间号") Long roomNum) throws IOException {
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
    @ApiOperation(value = "获取一个随机的昵称", notes = "获取一个随机的昵称")
    public Result<?> randomName(){
        String adj = redisTemplate.opsForSet().randomMember("adj");
        String noun = redisTemplate.opsForSet().randomMember("noun");
        return Result.success(adj+"的"+noun,null);
    }
}
