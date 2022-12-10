package fr.pandonia.core.menus.guilds;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.guild.IGuild;
import fr.pandonia.api.guild.IGuildMember;
import fr.pandonia.api.server.IServer;
import fr.pandonia.tools.ItemBuilder;
import fr.pandonia.tools.menu.page.PageMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class GuildMembersMenu extends PageMenu {

    private IGuild guild;

    public GuildMembersMenu(IGuild guild) {
        this.guild = guild;
        super.init();
    }

    @Override
    public void init() {
    }

    @Override
    public List<ItemStack> getItemList() {
        List<ItemStack> list = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.FRANCE);
        guild.getMembers().stream().sorted(Comparator.comparing(iGuildMember -> ((IGuildMember)iGuildMember).getGuildRank().ordinal()).thenComparing(iGuildMember -> ((IGuildMember)iGuildMember).getXPGathered())).forEach(gm -> {
            IServer server = PandoniaAPI.get().getServerManager().getPlayerServer(gm.getUUID());
            ItemBuilder it = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(gm.getName()).setName(gm.getGuildRank().getColor() + gm.getName());
            it.setLore("");
            it.addLore("§7Rang §8» " + gm.getGuildRank().getColor() + gm.getGuildRank().getName());
            it.addLore("§7Connecté §8» " + (server == null ? "§c§l✖" : "§3" + server.getUserFriendlyName()));
            it.addLore("§7Rejoint le §8» §3" + sdf.format(gm.getJoin()));
            it.addLore("");
            it.addLore("§7XP apportée §8» §3" + gm.getXPGathered());
            it.addLore("§7Saphirs donnés §8» §3" + gm.getSaphirsGiven());
            it.addLore("");
            list.add(it.toItemStack());
        });
        return list;
    }

    @Override
    public void handle(InventoryClickEvent e, int index) {
        checkBack(e, new GuildMainMenu());
    }

    @Override
    public String getMenuName() {
        return "Membres de la guilde";
    }

    @Override
    public void setMenuItems() {
        setCornerGlass();
        setBack();
        setItemOnPage();
        setPageItems();
    }

    @Override
    public int getLines() {
        int n = (guild.getMembers().size()/7) + 3;
        if(guild.getMembers().size()%7 == 0){
            n--;
        }
        return n;
    }
}
