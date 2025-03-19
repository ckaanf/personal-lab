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
        // 백엔드 threadpool에서 작업이 계속 밀리면 앞단에 작업이 쌓임
        // thread가 계속 만들어지더라도 앞단의 작업을 계속 빼주기 위해서 이런식으로도 활용한 적이 있음
        // 대신 전제가 threadpool 안의 작업이 빨리 끝낸다는 점
        // 서비스 최앞단에서 많은 유저의 작업을 받을 때 이런식으로 설정
        // 응답이 보장되어있는 요청들 -> rds에 저장한다든지 하는 곳에서는 맞지 않음
        //executor.setMaxPoolSize(Integer.MAX_VALUE );
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
