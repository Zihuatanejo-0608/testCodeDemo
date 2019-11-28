package toolsUnit;

import org.testng.annotations.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by andy on 2019/4/19.
 */
public class RedisTest {
    @Test
    public static void test() throws Exception{
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("172.17.xx.11",36379));
        nodes.add(new HostAndPort("172.17.xx.11",36381));
        nodes.add(new HostAndPort("172.17.xx.12",36379));
        nodes.add(new HostAndPort("172.17.xx.12",36381));
        nodes.add(new HostAndPort("172.17.xx.13",36379));
        nodes.add(new HostAndPort("172.17.xx.13",36381));

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(10);//最大连接数
        jedisPoolConfig.setMaxWaitMillis(2000);//最大等待时间
        jedisPoolConfig.setTestOnBorrow(true);//获得链接有效性检查

        JedisCluster jedisCluster = new JedisCluster(nodes,jedisPoolConfig);
        String key1 = "MBP-GAME-SERVICE+REVERSE_CARD_TYPE";
        String key2 = "MBP-GAME-SERVICE+KEY_REDIS_INCREASE_";
        String phone = "17739913654";
        String token = "token:" + "52ec8cfd-d27a-4c2e-90d9-0c3aa58d9114";
        String name = "Zihuatanejo@qq.com.cn";

        String verifyCode = "verifyCode:" + "3809c9db-ff32-494a-94fd-5930ec7b1dc6";

        String aabb = "bi-backend:metadata_map";

        if (jedisCluster.exists(aabb)){
            System.out.println(jedisCluster.type(aabb));
            //System.out.println(jedisCluster.get(aabb));
            System.out.println(jedisCluster.mget(aabb));
            System.out.println(jedisCluster.del(aabb));
        }else {
            System.out.println("不存在");
        }



//        System.out.println(jedisCluster.get(verifyCode));
//        System.out.println(jedisCluster.ttl(verifyCode));

        try{
            jedisCluster.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //获取redis值方法,集群
    public static String redisData(String key){
        try{
            Set<HostAndPort> nodes = new HashSet<HostAndPort>();
            nodes.add(new HostAndPort("172.17.xx.11",36379));
            nodes.add(new HostAndPort("172.17.xx.11",36381));
            nodes.add(new HostAndPort("172.17.xx.12",36379));
            nodes.add(new HostAndPort("172.17.xx.12",36381));
            nodes.add(new HostAndPort("172.17.xx.13",36379));
            nodes.add(new HostAndPort("172.17.xx.13",36381));

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
