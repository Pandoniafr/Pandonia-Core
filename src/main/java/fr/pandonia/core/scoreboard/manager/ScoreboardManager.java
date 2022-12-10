package fr.pandonia.core.scoreboard.manager;

import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.scoreboard.PersonalScoreboard;
import fr.pandonia.tools.DarkColorCalculator;
import fr.pandonia.tools.menu.PandoniaMenuPreset;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScoreboardManager {

    private PandoniaCore instance;

    private final ScheduledExecutorService executorMonoThread;

    private final Map<UUID, PersonalScoreboard> scoreboards;
    private final ScheduledFuture glowingTask;
    private final ScheduledFuture reloadingTask;
    private int ipCharIndex;
    private int cooldown;

    public ScoreboardManager(PandoniaCore instance) {
        this.instance = instance;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(16);
        executorMonoThread = Executors.newScheduledThreadPool(1);
        scoreboards = new HashMap<>();
        ipCharIndex = 0;
        cooldown = 0;

        glowingTask = scheduledExecutorService.scheduleAtFixedRate(() -> {
            String ip = colorIpAt();
            for (PersonalScoreboard scoreboard : scoreboards.values())
                executorMonoThread.execute(() -> scoreboard.setLines(ip));
        }, 80, 80, TimeUnit.MILLISECONDS);

        reloadingTask = scheduledExecutorService.scheduleAtFixedRate(() -> {
            for (PersonalScoreboard scoreboard : scoreboards.values())
                executorMonoThread.execute(scoreboard::reloadData);
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void onDisable() {
        scoreboards.values().forEach(PersonalScoreboard::onLogout);
    }

    public void onLogin(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            return;
        }
        scoreboards.put(player.getUniqueId(), new PersonalScoreboard(player));
    }

    public void onLogout(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).onLogout();
            scoreboards.remove(player.getUniqueId());
        }
    }

    public void update(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).reloadData();
        }
    }

    private String colorIpAt() {
        String ip = "    mc.pandonia.fr";
        ChatColor color = PandoniaMenuPreset.scoreboardColor == null ? PandoniaMenuPreset.textColor : PandoniaMenuPreset.scoreboardColor;
        if(color == null){
            color = ChatColor.AQUA;
        }
        if (cooldown > 0) {
            cooldown--;
            return ChatColor.WHITE + ip;
        }

        StringBuilder formattedIp = new StringBuilder();

        if (ipCharIndex > 0) {
            formattedIp.append(ip.substring(0, ipCharIndex - 1));
            formattedIp.append(color).append(ip.substring(ipCharIndex - 1, ipCharIndex));
        } else {
            formattedIp.append(ip.substring(0, ipCharIndex));
        }

        formattedIp.append(color).append(ip.charAt(ipCharIndex));

        if (ipCharIndex + 1 < ip.length()) {
            formattedIp.append(DarkColorCalculator.getDarkColor(color)).append(ip.charAt(ipCharIndex + 1));

            if (ipCharIndex + 2 < ip.length())
                formattedIp.append(ChatColor.WHITE).append(ip.substring(ipCharIndex + 2));

            ipCharIndex++;
        } else {
            ipCharIndex = 0;
            cooldown = 30;
        }

        return ChatColor.WHITE + formattedIp.toString();
    }


    public PersonalScoreboard getScoreboard(Player player) {
        return scoreboards.get(player.getUniqueId());
    }

}
