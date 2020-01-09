package com.nincraft.goodbot.beans;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.List;

@Configuration
@ComponentScan(basePackages = {"com.nincraft.goodbot"})
@Log4j2
public class ApplicationBean {

    @Value("${goodbotToken}")
    private String goodbotToken;

    @Autowired
    @Bean
    public ShardManager shardManager(List<ListenerAdapter> listenerAdapters) {
        try {
            DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder(goodbotToken);
            builder.addEventListeners(listenerAdapters.toArray());
            builder.setShardsTotal(-1);
            return builder.build();
        } catch (LoginException e) {
            log.error("Failed to login", e);
        }
        return null;
    }
}
