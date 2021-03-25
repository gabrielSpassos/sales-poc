package com.gabrielspassos.poc.config;

import com.mongodb.reactivestreams.client.MongoClient;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.reactivestreams.ReactiveStreamsMongoLockProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShedlockMongoConfig {

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Bean
    public LockProvider lockProvider(MongoClient mongo) {
        return new ReactiveStreamsMongoLockProvider(mongo.getDatabase(database));
    }

}
