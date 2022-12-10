package fr.pandonia.core.tablist;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.events.tablist.TeamTagViewSetToPlayer;
import fr.pandonia.api.events.tablist.TeamTagViewUpdateEvent;
import fr.pandonia.api.tablist.ITeamTag;
import fr.pandonia.api.tablist.ITeamTagView;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TeamTagView implements ITeamTagView {

    private String name;
    private Scoreboard scoreboard;
    private Map<String, ITeamTag> teamTags;

    public TeamTagView(String name) {
        this.teamTags = new HashMap<>();
        this.name = name;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    @Override
    public void set(Player player){
        if (player != null){
            TeamTagViewSetToPlayer event = new TeamTagViewSetToPlayer(player, this);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()){
                player.setScoreboard(event.getView().getScoreboard());
            }
        }
    }

    @Override
    public void setAll(Collection< ? extends Player> players) {
        for (Player player : players) {
            set(player);
        }
    }

    @Override
    public void setAll(Player[] players) {
        Player[] arrayOfPlayer;
        int j = (arrayOfPlayer = players).length;
        for (int i = 0; i < j; i++) {
            Player player = arrayOfPlayer[i];
            set(player);
        }
    }

    @Override
    public void setTeamTagHide(String player, boolean teamTagHide){
        if(teamTags.containsKey(player)){
            setNameTag(player, teamTags.get(player).getPrefix(), teamTags.get(player).getSuffix(), teamTags.get(player).getPriority(), teamTagHide);
        }else{
            setNameTag(player, "", "", 1, teamTagHide);
        }
    }

    @Override
    public void setSuffix(String player, String suffix){
        if(teamTags.containsKey(player)){
            setNameTag(player, teamTags.get(player).getPrefix(), suffix, teamTags.get(player).getPriority(), teamTags.get(player).isNameTagHide());
        }else{
            setNameTag(player, "", suffix, 1);
        }
    }

    @Override
    public void setPrefix(String player, String prefix){
        if(teamTags.containsKey(player)){
            setNameTag(player, prefix, teamTags.get(player).getSuffix(), teamTags.get(player).getPriority(), teamTags.get(player).isNameTagHide());
        }else{
            setNameTag(player, prefix, "", 1);
        }
    }

    @Override
    public void setPriority(String player, int priority){
        if(teamTags.containsKey(player)){
            setNameTag(player, teamTags.get(player).getPrefix(), teamTags.get(player).getSuffix(), priority, teamTags.get(player).isNameTagHide());
        }else{
            setNameTag(player, "", "", priority);
        }
    }

    @Override
    public void setNameTag(String player, String prefix, int priority){
        if(teamTags.containsKey(player)){
            setNameTag(player, prefix, teamTags.get(player).getSuffix(), priority);
        }else{
            setNameTag(player, prefix, "", priority);
        }
    }

    @Override
    public void setNameTag(String player, String prefix, String suffix, int priority){
        setNameTag(player, prefix, suffix, priority, false);
    }

    @Override
    public void setNameTag(String player, String prefix, String suffix, int priority, boolean hideNameTag){
        setNameTag(player, prefix, suffix, priority, hideNameTag, true);
    }

    @Override
    public void setNameTag(String player, String prefix, String suffix, int priority, boolean hideNameTag, boolean save){
        try{
            TeamTagViewUpdateEvent event = new TeamTagViewUpdateEvent(this, player, prefix, suffix, priority, hideNameTag);
            Bukkit.getPluginManager().callEvent(event);
            if(!event.isCancelled()){
                TeamTag team = new TeamTag(event.getPrefix(), event.getSuffix(), 200-event.getPriority(), this, event.isHideNameTag());
                team.set(player);
                if(event.getTeamSetEvent() != null){
                    event.getTeamSetEvent().onTeamSet(team);
                }
                if(save){
                    teamTags.put(player, team);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void restoreNameTag(String player){
        if(teamTags.get(player) != null){
            teamTags.get(player).set(player);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    public Map<String, ITeamTag> getTeamTags() {
        return teamTags;
    }

    @Override
    public ITeamTag getPlayerTeamTag(String player){
        return teamTags.get(player);
    }

    @Override
    public void forceNameTagHide(boolean value){
        this.teamTags.forEach((s, iTeamTag) -> iTeamTag.forceNameTagHide(value));
    }

    @Override
    public void updateNameTagVisibility(){
        this.teamTags.forEach((s, iTeamTag) -> iTeamTag.updateNameTagVisibility());
    }

    @SuppressWarnings("deprecation")
    @Override
    public ITeamTagView clone(String name){
        ITeamTagView view = PandoniaAPI.get().getTeamTagManager().createView(name);
        teamTags.forEach((player, teamTag) -> {
            view.setNameTag(player, teamTag.getPrefix(), teamTag.getSuffix(), 100 - teamTag.getPriority());
        });
        scoreboard.getObjectives().forEach(objective -> {
            Objective newObjective = view.getScoreboard().registerNewObjective(objective.getName(), objective.getCriteria());
            System.out.println(objective.getName() + " " + objective.getCriteria());
            for (OfflinePlayer player : scoreboard.getPlayers()) {
                newObjective.getScore(player.getName()).setScore(objective.getScore(player.getName()).getScore());
            }
            newObjective.setDisplaySlot(objective.getDisplaySlot());
        });
        return view;
    }

}
