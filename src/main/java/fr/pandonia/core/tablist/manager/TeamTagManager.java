package fr.pandonia.core.tablist.manager;

import fr.pandonia.api.tablist.ITeamTagView;
import fr.pandonia.api.tablist.manager.ITeamTagManager;
import fr.pandonia.core.tablist.TeamTagView;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.ArrayList;
import java.util.List;

public class TeamTagManager implements ITeamTagManager {

    private List<ITeamTagView> views;
    private boolean globalNameTagHide;

    public TeamTagManager(){
        this.views = new ArrayList<>();
        this.globalNameTagHide = false;
    }

    @Override
    public void setView(Player p, String name){
        ITeamTagView view = getView(name);
        if (view != null){
            view.set(p);
        }
    }

    @Override
    public ITeamTagView getView(String name){
        for (ITeamTagView view : views){
            if (view.getName().equalsIgnoreCase(name)){
                return view;
            }
        }
        return null;
    }

    @Override
    public ITeamTagView getPlayerView(Player p){
        for (ITeamTagView view : views) {
            if(p.getScoreboard().equals(view.getScoreboard())){
                return view;
            }
        }
        return null;
    }

    @Override
    public void setSuffix(String player, String suffix){
        for (ITeamTagView view : views){
            view.setSuffix(player, suffix);
        }
    }

    @Override
    public void setNameTag(String player, String prefix, String suffix, int priority){
        for (ITeamTagView view : views){
            view.setNameTag(player, prefix, suffix, priority);
        }
    }

    @Override
    public ITeamTagView createView(String name){
        ITeamTagView view = new TeamTagView(name);
        this.views.add(view);
        return view;
    }

    @Override
    public List<ITeamTagView> getViews() {
        return views;
    }

    @Override
    public boolean isGlobalNameTagHide() {
        return globalNameTagHide;
    }

    @Override
    public void setGlobalHideNameTag(boolean value){
        globalNameTagHide = value;
    }

    @Override
    public void updateAllNameTags(){
        for (ITeamTagView view : views) {
            if(globalNameTagHide){
                view.forceNameTagHide(true);
            }else{
                view.updateNameTagVisibility();
            }
        }
    }

    @Override
    public void shopPlayersLives(Player p){
        ITeamTagView view = getPlayerView(p);
        view.getScoreboard().registerNewObjective("Vie", "health").setDisplaySlot(DisplaySlot.PLAYER_LIST);
        view.getScoreboard().registerNewObjective("§4❤", "health").setDisplaySlot(DisplaySlot.BELOW_NAME);
        updateObjectif();
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
}
