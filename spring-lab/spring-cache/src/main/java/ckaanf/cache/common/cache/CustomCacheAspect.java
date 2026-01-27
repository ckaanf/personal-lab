package ckaanf.cache.common.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class CustomCacheAspect {
    private final List<CustomCacheHandler> cacheHandlers;
    private final CustomCacheKeyGenerator keyGenerator;

    @Around("@annotation(customCacheable)")
    public Object handleCacheable(ProceedingJoinPoint joinPoint, CustomCacheable customCacheable) {
        CacheStrategy cacheStrategy = customCacheable.cacheStrategy();
        CustomCacheHandler cacheHandler = findCacheHandler(cacheStrategy);

        String key = keyGenerator.genKey(joinPoint, cacheStrategy, customCacheable.cacheName(), customCacheable.key());
        Duration ttl = Duration.ofSeconds(customCacheable.ttlSeconds());
        Supplier<Object> dataSourceSupplier = createDatesourceSupplier(joinPoint);
        Class returnType = findReturnType(joinPoint);

        try {
            log.info("[CustomCacheAspect.handleCacheable] key = {} ", key);
            return cacheHandler.fetch(
                    key,
                    ttl,
                    dataSourceSupplier,
                    returnType
            );
        } catch (Exception e) {
            log.error("[CustomCacheAspect.handleCacheable] key = {} ", key, e);
            return dataSourceSupplier.get();
        }

    }

    private CustomCacheHandler findCacheHandler(CacheStrategy cacheStrategy) {
        return cacheHandlers.stream()
                .filter(handler -> handler.supports(cacheStrategy))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No cache handler found for strategy: " + cacheStrategy));
    }

    private Supplier<Object> createDatesourceSupplier(ProceedingJoinPoint joinPoint) {
        return () -> {
            try {
                return joinPoint.proceed();

            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Class findReturnType(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getReturnType();
    }

    @AfterReturning(pointcut = "@annotation(customCachePut)", returning = "result")
    public void handleCachePut(JoinPoint joinPoint, CustomCachePut customCachePut, Object result) {
        CacheStrategy cacheStrategy = customCachePut.cacheStrategy();
        CustomCacheHandler cacheHandler = findCacheHandler(cacheStrategy);

        String key = keyGenerator.genKey(joinPoint, cacheStrategy, customCachePut.cacheName(), customCachePut.key());
        Duration ttl = Duration.ofSeconds(customCachePut.ttlSeconds());
        cacheHandler.put(key, ttl, result);

        log.info("[CustomCacheAspect.handleCachePut] key = {} ", key);
        cacheHandler.put(key, ttl, result);
    }

    @AfterReturning(pointcut = "@annotation(customCacheEvict)")
    public void handleCacheEvict(JoinPoint joinPoint, CustomCacheEvict customCacheEvict) {
        CacheStrategy cacheStrategy = customCacheEvict.cacheStrategy();
        CustomCacheHandler cacheHandler = findCacheHandler(cacheStrategy);

        String key = keyGenerator.genKey(joinPoint, cacheStrategy, customCacheEvict.cacheName(), customCacheEvict.key());
        cacheHandler.evict(key);

        log.info("[CustomCacheAspect.handleCacheEvict] key = {} ", key);
        cacheHandler.evict(key);
    }
}
