package com.study.cache.config;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.study.cache.dto.PostDto;
import com.study.cache.dto.ProductReviewDto;
import com.study.cache.entity.PostEntity;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class KryoRedisSerializer<T> implements RedisSerializer<T> {

    private final Class<T> clazz;

    private final ThreadLocal<Kryo> kryoThreadLocal;

    @SuppressWarnings("unchecked")
    public KryoRedisSerializer(Class<T> clazz) {
        this.clazz = clazz;

        this.kryoThreadLocal = ThreadLocal.withInitial(() -> {
            Kryo kryo = new Kryo();

            // 필요한 클래스 등록 (명시적인 ID 부여)
            kryo.register(ArrayList.class, 100);
            kryo.register(ProductReviewDto.class, 101);
            kryo.register(LocalDateTime.class, 102);
            kryo.register(HashSet.class, 103);
            kryo.register(Arrays.asList("").getClass(), 104); // Arrays$ArrayList.class 등록
            kryo.register(Collections.unmodifiableList(new ArrayList<>()).getClass(), 105); // UnmodifiableList 등록
            kryo.register(Integer.class, 106);
            kryo.register(Long.class, 107);
            kryo.register(String.class, 108);
            kryo.register(PostDto.class, 115);
            kryo.register(PostEntity.class, 116);
            // 필요한 다른 클래스들도 고유한 ID로 등록
            // kryo.register(YourCustomClass.class, 116);

            return kryo;
        });
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) return new byte[0];
        Kryo kryo = kryoThreadLocal.get();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Output output = new Output(baos)) {
            kryo.writeObject(output, t);
            output.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new SerializationException("Kryo 직렬화 오류", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) return null;
        Kryo kryo = kryoThreadLocal.get();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             Input input = new Input(bais)) {
            return (T) kryo.readObject(input, clazz);
        } catch (Exception e) {
            throw new SerializationException("Kryo 역직렬화 오류", e);
        }
    }

}
