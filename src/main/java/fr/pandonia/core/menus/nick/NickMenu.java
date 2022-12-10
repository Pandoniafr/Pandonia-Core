package fr.pandonia.core.menus.nick;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.nick.INick;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.player.nick.Nick;
import fr.pandonia.tools.Heads;
import fr.pandonia.tools.ItemBuilder;
import fr.pandonia.tools.menu.AnvilGUI;
import fr.pandonia.tools.menu.PandoniaMenu;
import fr.pandonia.tools.menu.PandoniaMenuPreset;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class NickMenu extends PandoniaMenu {

    private IPPlayer player;
    private INick nick;

    @Override
    public void open(HumanEntity p) {
        player = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());
        nick = player.getNick() == null ? new Nick(player.getName()) : player.getNick();
        super.open(p);
    }

    @Override
    public String getMenuName() {
        return "Nick";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        checkMenuItems(e);
    }

    @Override
    public void setMenuItems() {
        setCornerGlass();
        set(2, 3, new ItemBuilder(Material.NAME_TAG).setName("§8» §fPseudo").addLore("", " §8┃ §3" + nick.getNick()).addRightLeftClickLore("Choisir un pseudo").toItemStack(), e -> {
            AnvilGUI gui = new AnvilGUI(PandoniaAPI.get().getPlugin(), e.getWhoClicked(), event -> {
                if(event.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                    String nickName = event.getName();
                    if(nickName.length() > 3 && nickName.length() < 16){
                        nick.setNick(nickName);
                        e.getWhoClicked().sendMessage(PandoniaMessages.getPrefix() + "Pseudo modifié !");
                    }
                    setMenuItems();
                    super.open(e.getWhoClicked());
                    event.setWillDestroy(true);
                    event.setWillClose(false);
                }else if(event.getSlot().equals(AnvilGUI.AnvilSlot.INPUT_LEFT)){
                    super.open(e.getWhoClicked());
                    event.setWillDestroy(true);
                    event.setWillClose(false);
                }else{
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                }
            });
            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.PAPER).setName(nick.getNick()).toItemStack());
            gui.open();
        });

        set(2, 7, new ItemBuilder(Material.NAME_TAG).setName("§8» §fSkin").addLore("", " §8┃ §3" + (nick.getSkinName() == null ? nick.getNick() : nick.getSkinName())).addRightLeftClickLore("Choisir un skin").toItemStack(), e -> {
            AnvilGUI gui = new AnvilGUI(PandoniaAPI.get().getPlugin(), e.getWhoClicked(), event -> {
                if(event.getSlot().equals(AnvilGUI.AnvilSlot.OUTPUT)) {
                    String nickName = event.getName();
                    if(nickName.length() > 3 && nickName.length() < 16){
                        nick.setSkinName(nickName);
                        e.getWhoClicked().sendMessage(PandoniaMessages.getPrefix() + "Skin modifié !");
                    }
                    setMenuItems();
                    super.open(e.getWhoClicked());
                    event.setWillDestroy(true);
                    event.setWillClose(false);
                }else if(event.getSlot().equals(AnvilGUI.AnvilSlot.INPUT_LEFT)){
                    super.open(e.getWhoClicked());
                    event.setWillDestroy(true);
                    event.setWillClose(false);
                }else{
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                }
            });
            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.PAPER).setName(nick.getSkinName() == null ? nick.getNick() : nick.getSkinName()).toItemStack());
            gui.open();
        });

        ItemBuilder it = new ItemBuilder(Material.SKULL_ITEM, 1, 3).setName("§8» §fGrade").getSkull(Heads.DIEU);
        it.addLore("");
        for (Ranks value : Ranks.values()) {
            it.addLore((nick.getRankNick().equals(value.rank) ? "§c»" : "  ") + " " + PandoniaAPI.get().getRankManager().getRank(value.rank).getColor() + value.rank);
        }
        it.addLore("");

        set(2, 5, it.toItemStack(), e -> {
            nick.setRankNick(Ranks.get(nick.getRankNick()).next().rank);
            setMenuItems();
        });

        set(3, 6, PandoniaMenuPreset.getValiderItem(), e -> {
            if(!nick.getNick().equalsIgnoreCase(player.getName())){
                if(nick.getSkinName() == null){
                    nick.setSkinName(nick.getNick());
                }
                player.setNick(nick);
                e.getWhoClicked().closeInventory();
                Bukkit.getScheduler().runTaskAsynchronously(PandoniaCore.get().getPlugin(), () -> PandoniaCore.getCore().getNickManager().updateNick((Player) e.getWhoClicked(), nick.getSkinName() != null));
            }else{
                player.sendMessage(PandoniaMessages.getPrefix() + "Vous ne pouvez pas mettre votre propre pseudo.");
            }
        });
    }

    private enum Ranks{
        DIEU("DIEU"),
        LEGENDE("LEGENDE"),
        ELITE("ELITE"),
        JOUEUR("JOUEUR");

        private String rank;

        Ranks(String rank) {
            this.rank = rank;
        }

        private static Ranks get(String name){
            for (Ranks value : Ranks.values()) {
                if(value.rank.equalsIgnoreCase(name)){
                    return value;
                }
            }
            return null;
        }

        private Ranks next(){
            int i = this.ordinal();
            i++;
            for (Ranks value : Ranks.values()) {
                if(value.ordinal() == i){
                    return value;
                }
            }
            return Ranks.values()[0];
        }

    }

    @Override
    public int getLines() {
        return 4;
    }
}
