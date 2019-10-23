package toolsUnit;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by andy on 2019/4/19.
 *
 */
public class RedisUtil {
    //获取redis值方法,集群
    public static String redisData(String key){
        try{
            Set<HostAndPort> nodes = new HashSet<HostAndPort>();
            nodes.add(new HostAndPort("101.91.xx.202",17000));
//            nodes.add(new HostAndPort("172.16.xx.123",7000));
//            nodes.add(new HostAndPort("172.16.xx.225",7002));
//            nodes.add(new HostAndPort("172.16.xx.225",7003));
//            nodes.add(new HostAndPort("172.16.xx.123",7005));

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

                return value;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//    @Bean
//    public JedisCluster getJedisCluster() {
//        String[] cNodes = nodes.split(",");
//        Set<HostAndPort> nodes =new HashSet<>();
//        //分割出集群节点
//        for(String node : cNodes) {
//            String[] hp = node.split(":");
//            nodes.add(new HostAndPort(hp[0],Integer.parseInt(hp[1])));
//        }
//        JedisPoolConfig jedisPoolConfig =new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(maxidle);
//        jedisPoolConfig.setMaxWaitMillis(maxwait);
//        //创建集群对象
//        return new JedisCluster(nodes,timeout,jedisPoolConfig);
//    }
//
//    @Bean
//    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)throws UnknownHostException {
//        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper objectMapper =new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//        redisTemplate.afterPropertiesSet();
//
//        return redisTemplate;
//    }




}
