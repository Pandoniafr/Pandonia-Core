package fr.pandonia.core.tablist;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.tablist.ITeamTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.UUID;

public class TeamTag implements ITeamTag {

    private String prefix;
    private String suffix;
    private int priority;
    private Team team;
    private boolean hideNameTag;

    public TeamTag(String prefix, String suffix, int priority, TeamTagView view, boolean hideNameTag) throws Exception {
        this.prefix = prefix;
        this.suffix = suffix;
        this.priority = priority;
        this.hideNameTag = hideNameTag;
        String tn = (priority + (prefix.length() > 6 ? prefix.substring(0, 6) : prefix) + (suffix.length() > 5 ? suffix.substring(0, 5) : suffix));
        if(hideNameTag){
            tn = tn + "ù";
        }
        this.team = view.getScoreboard().getTeam(tn);
        if(this.team == null){
            this.team = view.getScoreboard().registerNewTeam(tn);
        }
        this.team.setCanSeeFriendlyInvisibles(false);
        this.team.setAllowFriendlyFire(true);
        this.team.setNameTagVisibility(hideNameTag || PandoniaAPI.get().getTeamTagManager().isGlobalNameTagHide() ? NameTagVisibility.NEVER : NameTagVisibility.ALWAYS);

        /* VERIFICATION */
        int prefixLength = 0;
        int suffixLength = 0;
        prefixLength = prefix.length();
        suffixLength = suffix.length();
        if (prefixLength + suffixLength >= 36) {
            throw new Exception("prefix and suffix lenghts are greater than 16");
        }
        this.team.setSuffix(ChatColor.translateAlternateColorCodes('&', suffix));
        this.team.setPrefix(ChatColor.translateAlternateColorCodes('&', prefix));
    }

    @Override
    public void set(String player){
        this.team.addEntry(player);
    }

    @Override
    public void remove(String player){
        this.team.removeEntry(player);
    }

    @Override
    public void resetTagUtils(UUID uuid) {
        remove(Bukkit.getPlayer(uuid).getName());
    }

    @Override
    public void setAll(Collection<String> players) {
        for (String player : players) {
            set(player);
        }
    }

    @Override
    public void setAll(String[] players) {
        String[] arrayOfPlayer;
        int j = (arrayOfPlayer = players).length;
        for (int i = 0; i < j; i++) {
            String player = arrayOfPlayer[i];
            set(player);
        }
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        this.team.setPrefix(this.prefix);
    }

    @Override
    public void setSuffix(String suffix) {
        this.suffix = ChatColor.translateAlternateColorCodes('&', suffix);
        this.team.setSuffix(this.suffix);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public Team getTeam() {
        return this.team;
    }

    @Override
    public void removeTeam() {
        this.team.unregister();
    }

    @Override
    public int getPriority() {
        return 200 - priority;
    }

    @Override
    public boolean isNameTagHide() {
        return hideNameTag;
    }

    @Override
    public void forceNameTagHide(boolean value){
        this.team.setNameTagVisibility(value ? NameTagVisibility.NEVER : NameTagVisibility.ALWAYS);
    }

    @Override
    public void updateNameTagVisibility(){
        this.team.setNameTagVisibility(hideNameTag ? NameTagVisibility.NEVER : NameTagVisibility.ALWAYS);
    }
}
