package com.funfoxrr.BobBot.listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.Console;

public class EventListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        String emoji = event.getReaction().getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();

        String message = user.getAsTag() + " reacted to a message with " + emoji + " in the " + channelMention + " channel.";
        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(message).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        event.getGuild().getSystemChannel().sendMessage("I'm BobBot from Survival! Thanks for adding me to your server! My prefix is `/`. A list of commands can be found by (temporarily) typing my prefix.").queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (event.getAuthor().isBot())
        {
            return;
        }

        String message = event.getMessage().getContentRaw();
        if (message.contains("ping"))
        {
            event.getMessage().reply("Pong! - You found a test phrase!").queue();
        }

        if (message.toLowerCase().contains("who cares") || message.toLowerCase().contains("ok and") || message.toLowerCase().contains("stupid"))
        {
            event.getMessage().reply(":cry:").queue();
        }

        /*if (message.contains(":cry:"))
        {
            event.getMessage().reply(":cry: = chain").queue();
        }*/
    }

    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        User user = event.getUser();
        String message = user.getAsTag() + " is faking being offline! :scream:";
        if (event.getNewOnlineStatus().getKey() == "offline")
            event.getGuild().getDefaultChannel().asTextChannel().sendMessage(message).queue();
    }
}
