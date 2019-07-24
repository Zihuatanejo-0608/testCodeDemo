package tools;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by andy on 2019/4/19.
 */
public class RedisTest {
    //获取redis值方法,集群
    public static String redisData(String key){
        try{
            Set<HostAndPort> nodes = new HashSet<HostAndPort>();
            nodes.add(new HostAndPort("172.17.qq.11",36379));
            nodes.add(new HostAndPort("172.17.qq.11",36381));
            nodes.add(new HostAndPort("172.17.qq.12",36379));
            nodes.add(new HostAndPort("172.17.qq.12",36381));
            nodes.add(new HostAndPort("172.17.qq.13",36379));
            nodes.add(new HostAndPort("172.17.qq.13",36381));

            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(10);//最大连接数
            jedisPoolConfig.setMaxWaitMillis(2000);//最大等待时间
            jedisPoolConfig.setTestOnBorrow(true);//获得链接有效性检查

            JedisCluster jedisCluster = new JedisCluster(nodes,jedisPoolConfig);
            if (jedisCluster.exists(key)){
                String value = jedisCluster.get(key);

                System.out.println("*****************");
                System.out.println("redis值:" + jedisCluster.get(key) + "\n剩余时间:" + jedisCluster.ttl(key));
                System.out.println("*****************");

                //jedisCluster.del(key);
                return value;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
