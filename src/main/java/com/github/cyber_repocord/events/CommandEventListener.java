package com.github.cyber_repocord.events;

import com.github.cyber_repocord.helperclasses.Command;
import com.github.cyber_repocord.helperclasses.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public class CommandEventListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        List<Command> commands = Utils.getCommands();
        String[] args = event.getMessage().getContentRaw().split(" ");
        String commandAsText = args[0];
        if (event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) return;
        if (Utils.isBeta() && !event.getChannel().getId().equals(Utils.getBetaChannel())) {
            return;
        }
        if (commandAsText.length() >= Utils.getPrefix().length()) {
            commandAsText = commandAsText.substring(0, Utils.getPrefix().length());
            if (!commandAsText.equalsIgnoreCase(Utils.getPrefix())) {
                return;
            } else {
                commandAsText = args[0].substring(Utils.getPrefix().length());
            }
        } else {
            return;
        }
        for (Command command : commands) {
            if (command.isEnabled() && !command.isPrivate(true) && (command.getCommand().equalsIgnoreCase(commandAsText) || command.getAliases().contains(commandAsText.toLowerCase()))) {
                try {
                    command.execute(event, args);
                } catch (Exception e) {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setAuthor(event.getAuthor().getAsTag(), null, event.getAuthor().getAvatarUrl());
                    builder.setTitle("Error");
                    builder.setDescription("Couldn't execute command: An error occurred. This is a bug, please report it on our support server (" + Utils.getPrefix() + "support).\nError message: `" + e.getMessage() + "`");
                    builder.setColor(new Color(0xC80000));

                    event.getChannel().sendMessage(builder.build()).queue();
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        List<Command> commands = Utils.getCommands();
        String[] args = event.getMessage().getContentRaw().split(" ");
        String commandAsText = args[0];
        if (event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId())) return;
        if (commandAsText.length() >= Utils.getPrefix().length()) {
            commandAsText = commandAsText.substring(0, Utils.getPrefix().length());
            if (!commandAsText.equalsIgnoreCase(Utils.getPrefix())) {
                return;
            } else {
                commandAsText = args[0].substring(Utils.getPrefix().length());
            }
        } else {
            return;
        }
        for (Command command : commands) {
            if (command.isEnabled() && command.isPrivate(false) && (command.getCommand().equalsIgnoreCase(commandAsText) || command.getAliases().contains(commandAsText.toLowerCase()))) {
                try {
                    command.execute(event, args);
                } catch (Exception e) {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setAuthor(event.getAuthor().getAsTag(), null, event.getAuthor().getAvatarUrl());
                    builder.setTitle("Error");
                    builder.setDescription("Couldn't execute command: An error occurred. This is a bug. Please report it on our support server (" + Utils.getPrefix() + "support).\nError message: `" + e.getMessage() + "`");
                    builder.setColor(new Color(0xC80000));

                    event.getChannel().sendMessage(builder.build()).queue();
                    e.printStackTrace();
                }
            }
        }
    }
}
