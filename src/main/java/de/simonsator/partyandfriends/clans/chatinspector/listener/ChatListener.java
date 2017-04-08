package de.simonsator.partyandfriends.clans.chatinspector.listener;

import de.simonsator.partyandfriends.api.events.message.SimpleMessageEvent;
import de.simonsator.partyandfriends.api.pafplayers.OnlinePAFPlayer;
import de.simonsator.partyandfriends.clan.api.events.ClanMessageEvent;
import de.simonsator.partyandfriends.clan.commands.ClanCommands;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class ChatListener implements Listener {
	private final List<String> FORBIDDEN;
	private final String DO_NOT_WRITE_THAT;
	private final String PERMISSION;

	public ChatListener(List<String> pList, Configuration pConfig) {
		for (int i = 0; i < pList.size(); i++)
			pList.set(i, pList.get(i).toLowerCase());
		FORBIDDEN = pList;
		PERMISSION = pConfig.getString("Permission.Ignore");
		DO_NOT_WRITE_THAT = pConfig.getString("Messages.DoNotWriteThat");
	}

	@EventHandler
	public void onChat(ClanMessageEvent pEvent) {
		if (cancelled(pEvent))
			pEvent.getSender().sendMessage(ClanCommands.getInstance().getPrefix() + DO_NOT_WRITE_THAT);
	}

	private boolean cancelled(SimpleMessageEvent pEvent) {
		final OnlinePAFPlayer p = pEvent.getSender();
		if (!p.hasPermission(PERMISSION)) {
			String message = pEvent.getMessage().toLowerCase();
			for (String forbiddenWord : FORBIDDEN)
				if (message.contains(forbiddenWord)) {
					pEvent.setCancelled(true);
					return true;
				}
		}
		return false;
	}
}