package com.funfoxrr.BobBot.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ButtonListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        //event.reply(event.getUser().getName() + " clicked on it and the id of the button is " + event.getComponentId()).queue();

        if (event.getComponentId().equals("ping"))
        {
            event.reply("Pong! hehe").queue();
            return;
        }


        event.reply(event.getComponentId() + " got clicked and has no special function.").queue();
    }
}
