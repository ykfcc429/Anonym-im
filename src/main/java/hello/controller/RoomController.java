package hello.controller;

import hello.DO.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yankaifeng
 * 创建日期 2021/4/14
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/room")
public class RoomController {

    @RequestMapping("/list")
    public Result<?> roomList(){

        return Result.success("ok",null);
    }
}
