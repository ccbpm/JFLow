package bp.difference.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.lang.reflect.Method;
import java.time.Duration;

@Configuration
@ConditionalOnProperty(name="spring.data.redis.repositories.enabled", havingValue="true", matchIfMissing=false)
public class RedisConfig extends CachingConfigurerSupport
{
    @Autowired
    Environment env;

    private Duration timeToLive = Duration.ofSeconds(600);

    /**
     * 在没有指定缓存Key的情况下，key生成策略
     */
    @Bean
    public KeyGenerator keyGenerator()
    {
        return new KeyGenerator()
        {
            @Override
            public Object generate(Object target, Method method, Object... params)
            {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params)
                {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    // 缓存管理器 使用Lettuce，和jedis有很大不同LettuceConnectionFactory lettuceConnectionFactory
    @Bean
    public CacheManager cacheManager()
    {
        // 关键点，spring cache的注解使用的序列化都从这来，没有这个配置的话使用的jdk自己的序列化，实际上不影响使用，只是打印出来不适合人眼识别
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))// key序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(getValueSerializer()))// value序列化方式
                .disableCachingNullValues().entryTtl(timeToLive).disableCachingNullValues();
        ;// 缓存过期时间

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(lettuceConnectionFactory())// 默认有锁 等待锁时间为0
                .cacheDefaults(redisCacheConfiguration).transactionAware();
        return builder.build();
    }

    /**
     * RedisTemplate配置 使用自定义redisTemplate的时候 重新定义序列化方式 LettuceConnectionFactory lettuceConnectionFactory
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate()
    {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());

        RedisSerializer<?> stringSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringSerializer);// key序列化
        redisTemplate.setValueSerializer(getValueSerializer());// value序列化new LZ4Serializer(getValueSerializer())
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setHashKeySerializer(genericJackson2JsonRedisSerializer);// Hash key序列化
        redisTemplate.setHashValueSerializer(getValueSerializer());// Hash value序列化
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * shiroRedisTemplate配置 使用自定义shiroRedisTemplate的时候 重新定义序列化方式
     */
    @Bean
    public RedisTemplate<String, Object> shiroRedisTemplate()
    {
        // 配置redisTemplate
        RedisTemplate<String, Object> shiroRedisTemplate = new RedisTemplate<String, Object>();
        shiroRedisTemplate.setConnectionFactory(lettuceConnectionFactory());

        RedisSerializer<?> stringSerializer = new StringRedisSerializer();

        shiroRedisTemplate.setKeySerializer(stringSerializer);// key序列化
        shiroRedisTemplate.setValueSerializer(new SerializeUtils<Object>());// value序列化
        shiroRedisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
        shiroRedisTemplate.setHashValueSerializer(new SerializeUtils<Object>());// Hash value序列化
        shiroRedisTemplate.afterPropertiesSet();

        return shiroRedisTemplate;
    }


    private RedisSerializer<String> keySerializer()
    {
        return new StringRedisSerializer();
    }

    private RedisSerializer<Object> getValueSerializer()
    {
        // 设置序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    // 单机版配置连接参数
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration()
    {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(Integer.parseInt(env.getProperty("spring.redis.database")));
        redisStandaloneConfiguration.setHostName(env.getProperty("spring.redis.host"));
        redisStandaloneConfiguration.setPort(Integer.parseInt(env.getProperty("spring.redis.port")));
        redisStandaloneConfiguration.setPassword(RedisPassword.of(env.getProperty("spring.redis.password")));

        // 集群版配置
        // RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        // String[] serverArray = clusterNodes.split(",");
        // Set<RedisNode> nodes = new HashSet<RedisNode>();
        // for (String ipPort : serverArray) {
        // String[] ipAndPort = ipPort.split(":");
        // nodes.add(new RedisNode(ipAndPort[0].trim(), Integer.valueOf(ipAndPort[1])));
        // }
        // redisClusterConfiguration.setPassword(RedisPassword.of(password));
        // redisClusterConfiguration.setClusterNodes(nodes);
        // redisClusterConfiguration.setMaxRedirects(maxRedirects);

        return redisStandaloneConfiguration;
    }

    /**
     * 配置LettuceClientConfiguration 包括线程池配置和安全项配置 genericObjectPoolConfig common-pool2线程池GenericObjectPoolConfig
     * genericObjectPoolConfig
     *
     * @return lettuceClientConfiguration
     */
    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration()
    {
        LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(Long.parseLong(env.getProperty("spring.redis.timeout")))).shutdownTimeout(Duration.ofMillis(200))
                .poolConfig(genericObjectPoolConfig()).build();
        /*if (env.getProperty("spring.redis.ssl"))
        {
            lettuceClientConfiguration.isUseSsl();
        }*/
        return lettuceClientConfiguration;
    }

    // 设置连接工厂
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory()
    {
        return new LettuceConnectionFactory(redisStandaloneConfiguration(), lettuceClientConfiguration());
    }


    /**
     * GenericObjectPoolConfig 连接池配置
     */
    @Bean
    @ConfigurationProperties(prefix = "redis.lettuce.pool")
    @Scope(value = "prototype")
    @SuppressWarnings("rawtypes")
    public GenericObjectPoolConfig genericObjectPoolConfig()
    {
        return new GenericObjectPoolConfig();
    }
}
