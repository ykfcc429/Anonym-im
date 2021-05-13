package hello.util;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 布隆过滤器
 * @author yankaifeng
 * 创建日期 2021/5/13
 */
@AllArgsConstructor
@Component
public class BloomFilter {

    final int MAXIMUM_CAPACITY = 1 << 30;

    private Map<String ,Integer> map = new HashMap<>();

    private final RedisTemplate<String,String> redisTemplate;

    public Boolean exits(String topic, String key)throws NullPointerException{
        if(key==null)
            throw new NullPointerException();
        return redisTemplate.opsForValue().getBit(topic,getOffset(topic,key));
    }

    public void create(String topic, int initSize){
        int n = -1 >>> Integer.numberOfLeadingZeros(initSize - 1);
        int size =  (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
        map.put(topic,size);
    }

    public void create(String topic){
        this.create(topic,1 << 16);
    }

    public void put(String topic, String key)throws NoSuchElementException{
        contains(topic);
        redisTemplate.opsForValue().setBit(topic,getOffset(topic, key),true);
    }

    private int getOffset(String topic, String key)throws NoSuchElementException{
        contains(topic);
        Integer integer = map.get(topic);
        int hash;
        hash = (hash = key.hashCode())^ (hash >>> 16);
        return hash & (integer-1);
    }

    private void contains(String topic)throws NoSuchElementException{
        if(!map.containsKey(topic))
            throw new NoSuchElementException();
    }
}
