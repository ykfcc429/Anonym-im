package hello.controller;

import hello.DO.Result;
import hello.util.BloomFilter;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author yankaifeng
 * 创建日期 2021/5/13
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/name")
@Validated
@Api(value = "昵称操作接口", tags = "昵称操作接口")
@ApiResponses({@ApiResponse(code = 233, message = "成功")
        , @ApiResponse(code = 4002, message = "参数校验错误")
        , @ApiResponse(code = 5001, message = "系统错误")})
public class NickNameController {

    private final RedisTemplate<String,String> redisTemplate;

    private final BloomFilter bloomFilter;

    @GetMapping("/randomName")
    @ApiOperation(value = "获取一个随机的昵称", notes = "获取一个随机的昵称")
    public Result<?> randomName(){
        String adj = redisTemplate.opsForSet().randomMember("adj");
        String noun = redisTemplate.opsForSet().randomMember("noun");
        return Result.success(adj+"的"+noun,null);
    }

    @PostMapping("/getName")
    @ApiOperation(value = "查看昵称是否存在", notes = "查看昵称是否存在")
    public Result<?> getName(@Valid @NotNull @RequestParam @ApiParam(name = "name", value = "名字") String name){
        return Result.success(!bloomFilter.exits("name", name) ?"不存在":"存在");
    }
}
