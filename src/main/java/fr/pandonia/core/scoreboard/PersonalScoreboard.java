package fr.pandonia.core.scoreboard;

import fr.pandonia.api.events.ScoreboardUpdateEvent;
import fr.pandonia.core.scoreboard.objectives.ObjectiveSign;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class PersonalScoreboard {

    private Player player;
    private final UUID uuid;
    private final ObjectiveSign objectiveSign;
    private Scoreboard scoreboard;

    public PersonalScoreboard(Player player) {
        this.player = player;
        uuid = player.getUniqueId();
        objectiveSign = new ObjectiveSign("sidebar", "HxH");

        reloadData();
        objectiveSign.addReceiver(player);

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public void reloadData() {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onScoreboardUpdate(ScoreboardUpdateEvent e){

    }

    @SuppressWarnings("unlikely-arg-type")
    public void setLines(String ip) {
        if (player != null){
            if (player.isOnline()){
                ScoreboardUpdateEvent e = new ScoreboardUpdateEvent(player, ip);
                Bukkit.getPluginManager().callEvent(e);
                try {
                    if (!e.getUUID().equals(this.uuid)) return;
                    if (e.getScores().isEmpty() && e.getLines().isEmpty()) e.setName("");
                    objectiveSign.clearScores();

                    objectiveSign.setDisplayName(e.getName());
                    int j = 0;
                    for (String line : e.getLines()) {
                        objectiveSign.setLine(j++, line);
                    }
                    int i = (e.getScores().size() + e.getLines().size());
                    for (String line : e.getScores().keySet()) {
                        objectiveSign.setLine(i - e.getScores().get(line), line);
                        j++;
                    }
                    objectiveSign.setLine(j, e.getIp());
                    objectiveSign.updateLines();
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        }
    }

    public void addObjective(String name, String type, DisplaySlot slot) {
        Objective objective;
        scoreboard.registerNewObjective(name, type);
        objective = scoreboard.getObjective(name);
        objective.setDisplaySlot(slot);
    }

    public void updateObjectif() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.getHealth() >= 19.99) {
                if (!pl.hasPotionEffect(PotionEffectType.ABSORPTION)) {
                    pl.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 3, 0, true, false));
                }
            } else {
                pl.setHealth(pl.getHealth() + 0.0001);
            }
        }
    }

    public void onLogout() {
        objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }


}
