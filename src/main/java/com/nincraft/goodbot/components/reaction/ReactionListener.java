package com.nincraft.goodbot.components.reaction;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class ReactionListener extends ListenerAdapter {

    private List<ReactionResponse> reactionResponseList;

    @Autowired
    public ReactionListener(List<ReactionResponse> reactionResponseList) {
        this.reactionResponseList = reactionResponseList;
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromGuild() && !event.getAuthor().isBot()) {
            respond(event);
        }
    }

    private void respond(MessageReceivedEvent event) {
        reactionResponseList.stream()
                .filter(reactionResponse -> isExactResponse(event, reactionResponse)
                        || isContainsResponse(event, reactionResponse))
                .findFirst()
                .ifPresent(reactionResponse -> reactionResponse.react(event.getMessage(), event.getChannel()));
    }

    private boolean isExactResponse(MessageReceivedEvent event, ReactionResponse reactionResponse) {
        return reactionResponse.getType().equalsIgnoreCase("exact") && reactionResponse.getTarget()
                .equalsIgnoreCase(event.getMessage().getContentStripped());
    }

    private boolean isContainsResponse(MessageReceivedEvent event, ReactionResponse reactionResponse) {
        return reactionResponse.getType().equalsIgnoreCase("contains") && event.getMessage()
                .getContentStripped()
                .toLowerCase()
                .contains(reactionResponse.getTarget()
                        .toLowerCase());
    }
}
