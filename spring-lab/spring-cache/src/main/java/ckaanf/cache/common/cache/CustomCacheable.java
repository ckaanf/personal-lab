package ckaanf.cache.common.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomCacheable {
    CacheStrategy cacheStrategy() default CacheStrategy.NONE;
    String cacheName();
    String key();
    long ttlSeconds() default 0;
}
