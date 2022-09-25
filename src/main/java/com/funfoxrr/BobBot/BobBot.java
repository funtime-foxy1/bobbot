package com.funfoxrr.BobBot;

import com.funfoxrr.BobBot.buttons.ButtonListener;
import com.funfoxrr.BobBot.commands.CommandManager;
import com.funfoxrr.BobBot.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class BobBot {

    private final Dotenv config;

    private final ShardManager shardManager;

    public BobBot()
    {
        config = Dotenv.configure().load();

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.get("TOKEN"));
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_BANS);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setMemberCachePolicy(MemberCachePolicy.ONLINE);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);
        builder.setActivity(Activity.playing("Survival"));
        shardManager = builder.build();

        // listeners

        shardManager.addEventListener(new EventListener(), new CommandManager(), new ButtonListener());
    }

    public ShardManager getShardManager()
    {
        return shardManager;
    }

    public Dotenv getConfig() {
        return config;
    }

    public static void main(String[] args) {
        System.out.println("Hello world!");

        BobBot bot = new BobBot();
    }
}