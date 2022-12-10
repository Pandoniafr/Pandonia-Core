package fr.pandonia.core.pubsub.list;

import fr.pandonia.api.events.PlayerRankUpdateEvent;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.pubsub.MessageReceiver;
import fr.pandonia.api.rank.IRank;
import fr.pandonia.core.PandoniaCore;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

public class SendModifyPlayerMR extends MessageReceiver {

    public SendModifyPlayerMR(Document message) {
        super(message);
    }

    @Override
    public void execute() {
        UUID uuid = UUID.fromString(getMessage().getString("playerUUID"));
        String action = getMessage().getString("action");
        String type = getMessage().getString("type");
        String value = getMessage().getString("value");
        IPPlayer player = PandoniaCore.getCore().getPlayerManager().getPlayer(uuid);
        if(player != null){
            if(type.equals("hosts")){
                if(action.equals("add")){
                    player.addHosts(Integer.parseInt(value));
                }else if(action.equals("remove")){
                    player.removeHosts(Integer.parseInt(value));
                }
            }
            if(type.equals("saphirs")){
                if(action.equals("add")){
                    player.addSaphirs(Integer.parseInt(value));
                }else if(action.equals("remove")){
                    player.removeSaphirs(Integer.parseInt(value));
                }
            }
            if(type.equals("lootboxes")){
                if(action.equals("add")){
                    player.addLootbox(Integer.parseInt(value));
                }else if(action.equals("remove")){
                    player.removeLoobox(Integer.parseInt(value));
                }
            }
            if(action.equals("reset")){
                IRank playerRank = player.getRankObject(PandoniaCore.getCore().getRankManager());
                if(type.equals("itachi")){
                    player.setCustomRankName(null);
                    player.setCustomRankColor(null);
                    if(player.getBukkitPlayer() != null){
                        Bukkit.getPluginManager().callEvent(new PlayerRankUpdateEvent(player.getBukkitPlayer(), playerRank, null));
                    }
                }
            }
            if(action.equals("set")){
                IRank playerRank = player.getRankObject(PandoniaCore.getCore().getRankManager());
                if(type.equals("rank") || type.equals("rankAdd")){
                    if(playerRank != null && player.getBukkitPlayer() != null){
                        player.setRank(value);
                        IRank newRank = PandoniaCore.get().getRankManager().getRank(value);
                        if(getMessage().getLong("rankEnd") != null){
                            long time = getMessage().getLong("rankEnd");
                            if(type.equals("rankAdd") && player.getRankEnd() != null){
                                player.setRankEnd(new Date(player.getRankEnd().getTime() + (time * 1000)));
                            }else{
                                player.setRankEnd(new Date(System.currentTimeMillis() + (time * 1000)));
                            }
                        }
                        Bukkit.getPluginManager().callEvent(new PlayerRankUpdateEvent(player.getBukkitPlayer(), playerRank, newRank));
                    }
                }else{
                    if(player.getRank().equals("ITACHI")){
                        if(type.equals("itachiName")){
                            player.setCustomRankName(value);
                        }
                        if(type.equals("itachiColor")){
                            player.setCustomRankColor(value);
                        }
                        if(player.getBukkitPlayer() != null){
                            Bukkit.getPluginManager().callEvent(new PlayerRankUpdateEvent(player.getBukkitPlayer(), playerRank, null));
                        }
                    }
                }
            }
        }
    }

}
