package com.welhire.cv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class   AsyncConfig {

    private final AsyncProperties props;

    public AsyncConfig(AsyncProperties props) {
        this.props = props;
    }

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(props.getCorePoolSize());
        ex.setMaxPoolSize(props.getMaxPoolSize());
        ex.setQueueCapacity(props.getQueueCapacity());
        ex.setThreadNamePrefix(props.getThreadNamePrefix());
        ex.initialize();
        return ex;
    }
}
