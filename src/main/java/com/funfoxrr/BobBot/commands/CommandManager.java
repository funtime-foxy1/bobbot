package com.funfoxrr.BobBot.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildTimeoutEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.internal.utils.message.MessageCreateBuilderMixin;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("welcome"))
        {
            event.reply("Welcome, " + event.getUser().getAsMention() + "!").setEphemeral(true).queue();
        }

        else if (command.equals("roles"))
        {
            event.deferReply().queue();
            String response = "";
            for (Role role : event.getGuild().getRoles())
            {
                if (role.getAsMention().equals("@everyone"))
                {
                    continue;
                }
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).queue();
        }

        else if (command.equals("say"))
        {
            OptionMapping messageOption = event.getOption("message");
            OptionMapping messageOption1 = event.getOption("channel");
            String message = messageOption.getAsString();
            if (messageOption1 != null)
            {
                System.out.println("Channel and the user is "  + event.getUser().getName());
                var channel = messageOption1.getAsChannel().asTextChannel().getId();
                System.out.println(channel + ", " + messageOption1.getAsChannel().getName());
                event.getGuild().getTextChannelById(channel).sendMessage(message).queue();
            } else
            {
                System.out.println("No Channel and the user is " + event.getUser().getName());
                event.getChannel().sendMessage(message).queue();
            }
            event.reply("Your message was sent!").setEphemeral(true).queue();
        }

        else if (command.equals("ban"))
        {
            User user = event.getOption("user").getAsUser();
            String tempreason = "No reason provided.";
            if (event.getOption("reason") != null) { // CHECKS IF SECOND OPTION IS AVAILABLE
                tempreason = event.getOption("reason").getAsString();
            }

            final String reason = tempreason;

            Role modCase1 = event.getGuild().getRolesByName("Mod", false).get(0); // GETS MOD ROLE

            if (event.getMember().getRoles().contains(modCase1)) // IF MOD
            {
                event.getGuild().ban(user, 0, TimeUnit.SECONDS).reason(reason).queue(suc -> // BANS PLAYER
                {
                    var eb = Ban(user, reason);
                    event.replyEmbeds(eb).queue();
                });
            }
            else // NO PERMISSION
            {
                EmbedBuilder eb = CreateEmbed(":x: You dont have permission!", "You dont have permission to ban someone!");

                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                return;
            }


            if (user.getId() == event.getUser().getId())
            {
                var embed = CreateEmbed(":x: You cant ban yourself!", "Why would you ban your self? :joy:");
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                return;
            }
            if (user.isBot())
            {
                var embed = CreateEmbed(":x: I dont want to ban a bot!", "I dont want to ban my fellow friends!");
                event.replyEmbeds(embed.build()).setEphemeral(true).queue();
                return;
            }
        }

        else if (command.equals("createbutton")) {
            String id = event.getOption("buttonid").getAsString();
            String text = event.getOption("buttontext").getAsString();

            Button button = Button.primary(id, text);

            event.reply("Here is your test button")
                    .addActionRow(button)
                    .queue();
        }

        else if (command.equals("serverinfo")) {

            var timeCreated = event.getGuild().getTimeCreated().getDayOfWeek() + ", " + event.getGuild().getTimeCreated().getMonth() + " " + event.getGuild().getTimeCreated().getDayOfMonth() + ", " + event.getGuild().getTimeCreated().getYear();

            //event.getGuild().get

            // .setAuthor("FunFoxRR", "https://gamejolt.com/@FunFoxRR", "https://funfoxrr.neocities.org/pfp256BG.png")

            var embed = CreateEmbed(":computer: Server Info!", "")
                    .setImage(event.getGuild().getIconUrl())
                    .addField("Online Users:", String.valueOf(event.getGuild().getMembers().size()), true)
                    .addField("Created On:", timeCreated, false);

            event.replyEmbeds(embed.build()).queue();
        }

        else if (command.equals("help")) {
            //event.getGuild().get

            // .setAuthor("FunFoxRR", "https://gamejolt.com/@FunFoxRR", "https://funfoxrr.neocities.org/pfp256BG.png")

            var embed = CreateEmbed("Help", "------------------------------------------")
                    .setAuthor("FunFoxRR", "https://gamejolt.com/@FunFoxRR", "https://funfoxrr.neocities.org/pfp256BG.png")
                    .setImage("https://funfoxrr.neocities.org/bobbot.png")
                    .addField("/help [private]", "Get a list of commands!", false)
                    .addField("/serverinfo", "Shows server info. Ex: Server creation date", false)
                    .addField("/welcome", "Get a private welcome from me!", false)
                    .addField("/say <message> [channel]", "Sends a message as the bot!", false)
                    .addField("/createbutton <buttonid> <buttontext>", "Creates an example button for testing!", false)
                    .addField("/ban <user> [reason]", "Bans someone if they are a bad boi!", false);

            /*if (event.getOption("private") != null) { // CHECKS IF BOOL OPTION IS AVAILABLE
                if (event.getOption("private").getAsBoolean()) // IF TRUE
                {
                    event.replyEmbeds(embed.build()).setEphemeral(true).queue(); // private
                } else
                {
                    event.replyEmbeds(embed.build()).queue(); // not private
                }
            } else
            {
                event.replyEmbeds(embed.build()).queue(); // not private
            }*/

            var help = CreateEmbed("Command: Help", "")
                    .addField("Usage:", "/help [command]", false)
                    .addField("Desecription:", "All it does is helps you with commands!", false);
            var serverinfo = CreateEmbed("Command: serverinfo", "")
                    .addField("Usage:", "/serverinfo", false)
                    .addField("Desecription:", "Gives you some info about the current server the bot is in.", false);
            var welcome = CreateEmbed("Command: welcome", "")
                    .addField("Usage:", "/welcome", false)
                    .addField("Desecription:", "Gives you a comfy welcome to the server privately.", false);
            var say = CreateEmbed("Command: say", "")
                    .addField("Usage:", "/say <message> [channel]", false)
                    .addField("Desecription:", "I say something for you! Ex: [/say hello] -> hello", false);
            var createbutton = CreateEmbed("Command: createbutton", "")
                    .addField("Usage:", "/createbutton <buttonid> <buttontext>", false)
                    .addField("Desecription:", "Creates a button for testing!", false);
            var ban = CreateEmbed("Command: ban", "")
                    .addField("Usage:", "/ban <user> [reason]", false)
                    .addField("Desecription:", "Bans a person if they are a bad boi.", false)
                    .addField("Accessable to:", "mods", false);




            if (event.getOption("command") != null)
            {
                String commandHelp = event.getOption("command").getAsString();
                if (commandHelp.equals("help"))
                {
                    event.replyEmbeds(help.build()).setEphemeral(true).queue(); // private
                }

                else if (commandHelp.equals("serverinfo"))
                {
                    event.replyEmbeds(serverinfo.build()).setEphemeral(true).queue(); // private
                }

                else if (commandHelp.equals("welcome"))
                {
                    event.replyEmbeds(welcome.build()).setEphemeral(true).queue(); // private
                }

                else if (commandHelp.equals("say"))
                {
                    event.replyEmbeds(say.build()).setEphemeral(true).queue(); // private
                }

                else if (commandHelp.equals("createbutton"))
                {
                    event.replyEmbeds(createbutton.build()).setEphemeral(true).queue(); // private
                }

                else if (commandHelp.equals("ban"))
                {
                    event.replyEmbeds(ban.build()).setEphemeral(true).queue(); // private
                }

                else
                {
                    event.reply("**:x: The command is not vaild!**").setEphemeral(true).queue();
                }
                return;
            }

            Button aboutButton = Button.secondary("aboutbot", "About BobBot");

            event.replyEmbeds(embed.build()).addActionRow(aboutButton).setEphemeral(true).queue(); // private
        }
    }

    MessageEmbed Ban(User user, String reason)
    {
        EmbedBuilder eb;
        eb = new EmbedBuilder()
                .setTitle(":white_check_mark: Ban complete!")
                .setDescription("The user " + user.getName() + " was banned. Reason: " + reason);
        return eb.build();
    }

    EmbedBuilder CreateEmbed(String title, String description)
    {
        EmbedBuilder eb;
        eb = new EmbedBuilder()
                .setTitle(title)
                .setDescription(description);
        return eb;
    }

    // guild commands

/*    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        List<CommandData> commandData = new ArrayList<CommandData>();
        commandData.add(Commands.slash("welcome", "Replays with a welcome message! - BobBot"));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }*/

    // ON GUILD JOIN

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<CommandData>();
        commandData.add(Commands.slash("welcome", "Replays with a welcome message! - BobBot")); // 0
        commandData.add(Commands.slash("roles", "Displays all the roles. - BobBot")); // 1

        // say - 2
        OptionData option1 = new OptionData(OptionType.STRING, "message", "The message you want be to say!", true);
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel", "The message you want it to be in!").setChannelTypes(ChannelType.TEXT, ChannelType.GUILD_PRIVATE_THREAD);
        commandData.add(Commands.slash("say", "I will say something for you! - BobBot").addOptions(option1, option2));
        // ban - 3
        OptionData ban1 = new OptionData(OptionType.USER, "user", "The user you want to be banned.", true);
        OptionData ban2 = new OptionData(OptionType.STRING, "reason", "The reason for this user to be banned.");
        commandData.add(Commands.slash("ban", "Ban someone if they are a bad boi! - BobBot").addOptions(ban1, ban2));
        // button test - 4
        OptionData button1 = new OptionData(OptionType.STRING, "buttonid", "Set a button id", true);
        OptionData button2 = new OptionData(OptionType.STRING, "buttontext", "Set button text", true);
        commandData.add(Commands.slash("createbutton", "Create a button! - BobBot").addOptions(button1, button2));
        // server info - 5
        commandData.add(Commands.slash("serverinfo", "See the current server stats. - BobBot"));
        // help - 6
        OptionData helpOption = new OptionData(OptionType.STRING, "command", "Learn more about a specific command.", false);
        commandData.add(Commands.slash("help", "Get bot help! - BobBot").addOptions(helpOption));


        event.getGuild().updateCommands().addCommands(commandData).queue();
    }


    // global commands
}
