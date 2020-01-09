package com.nincraft.goodbot;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;


@Log4j2
@ComponentScan({"com.nincraft.goodbot"})
public class Goodbot {

    @Autowired
    private ShardManager shardManager;

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext context) {
        return args -> {
            List<JDA> shards = shardManager.getShards();
            log.info("Starting Goodbot with {} shard(s)", shards.size());
            shardManager.getShards()
                    .forEach(jda -> {
                        try {
                            jda.awaitReady();
                            log.info("Shard ID {}: Connected to {} server(s)", jda.getShardInfo()
                                    .getShardId(), jda.getGuilds().size());
                        } catch (InterruptedException e) {
                            log.error("Failed to wait for shard to start", e);
                        }
                    });
            shardManager.setActivity(Activity.playing("Good Games"));
        };
    }
}
