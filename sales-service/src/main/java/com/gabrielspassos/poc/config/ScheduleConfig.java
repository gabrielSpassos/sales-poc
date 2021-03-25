package com.gabrielspassos.poc.config;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "30s")
@Configuration
public class ScheduleConfig {
}
