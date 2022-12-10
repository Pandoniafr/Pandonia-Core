package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.pubsub.MessageReceiver;
import fr.pandonia.core.guild.Guild;
import fr.pandonia.core.player.PPlayer;
import fr.pandonia.core.player.battlepass.PlayerBattlePass;
import fr.pandonia.core.player.friends.PlayerFriends;
import fr.pandonia.core.player.owning.PlayerOwning;
import fr.pandonia.core.player.settings.PlayerSettings;
import fr.pandonia.core.player.stats.PlayerStats;
import org.bson.Document;

import java.util.UUID;

public class GetAccountMR extends MessageReceiver {

    public GetAccountMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {

        if(!getMessage().getString("server").equals(PandoniaAPI.get().getInstanceInfo().getName())){
            return;
        }
        Document account = getMessage().get("account", Document.class);
        Document settings = getMessage().get("settings", Document.class);
        Document owning = getMessage().get("owning", Document.class);
        Document stats = getMessage().get("stats", Document.class);
        Document friends = getMessage().get("friends", Document.class);
        Document battlePass = getMessage().get("battlePass", Document.class);
        Document guildDoc = getMessage().get("guild", Document.class);


        UUID uuid = UUID.fromString(account.getString("uuid"));

        // PandoraAPI.get().getPlayerManager().getTempPlayer(uuid).setLoaded(true);
        PPlayer pp = PPlayer.fromDocument(account);
        PandoniaAPI.get().getPlayerManager().addPlayer(pp);


        PandoniaMessages.debug("Event: GetAccountMR " + pp.getName() + " " + PandoniaMessages.getActualDate());

        if (settings != null){
            PlayerSettings playerSettings = PlayerSettings.fromDocument(settings);
            PandoniaAPI.get().getPlayerSettingsManager().addPlayerSettings(playerSettings);
        }

        if (owning != null){
            PlayerOwning playerOwning = PlayerOwning.fromDocument(owning);
            PandoniaAPI.get().getPlayerOwningManager().addPlayerOwning(playerOwning);
        }

        if (stats != null){
            PlayerStats playerStats = PlayerStats.fromDocument(stats);
            PandoniaAPI.get().getPlayerStatsManager().addPlayerStats(playerStats);
        }

        if(friends != null){
            PlayerFriends playerFriends = PlayerFriends.fromDocument(friends);
            PandoniaAPI.get().getFriendsManager().addPlayerFriends(playerFriends);
        }

        if (battlePass != null){
            PlayerBattlePass playerBattlePass = PlayerBattlePass.fromDocument(battlePass);
            PandoniaAPI.get().getPlayerBattlePassManager().addPlayerBattlePass(playerBattlePass);
        }

        if (guildDoc != null){
            Guild guild = Guild.fromDocument(guildDoc);
            PandoniaAPI.get().getGuildManager().addGuild(guild);
            //guild.getMembers().stream().filter(gm -> gm.getHead() == null).forEach(IGuildMember::loadHead);
        }

    }

}
