package com.library.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;

@Slf4j
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Bean("lsExecutor")
    @Override
    public Executor getAsyncExecutor() {
        // 설정값은 상황에 따라 설정
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cpuCoreCount = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(cpuCoreCount);
        executor.setMaxPoolSize(cpuCoreCount * 2);
        executor.setQueueCapacity(10);
        executor.setKeepAliveSeconds(60);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setThreadNamePrefix("LS-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }

    private static class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            log.error("Failed to execute {}", ex.getMessage());
            Arrays.asList(params).forEach(param -> log.error("Parameter value: {}", param));
        }
    }
}
