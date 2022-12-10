package fr.pandonia.core.menus.guilds;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.guild.GuildRank;
import fr.pandonia.api.guild.IGuild;
import fr.pandonia.api.guild.IGuildInvitation;
import fr.pandonia.api.guild.IGuildMember;
import fr.pandonia.tools.ItemBuilder;
import fr.pandonia.tools.TimeUtils;
import fr.pandonia.tools.menu.page.PageMenu;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GuildInvitationsMenu extends PageMenu {

    private IGuild guild;
    private IGuildMember p;

    public GuildInvitationsMenu(IGuild guild) {
        this.guild = guild;
        super.init();
    }

    @Override
    public void init() {
    }

    @Override
    public void open(HumanEntity p) {
        this.p = guild.getMember(p.getUniqueId());
        super.open(p);
    }

    @Override
    public List<ItemStack> getItemList() {
        List<ItemStack> list = new ArrayList<>();
        guild.getInvitations().stream().sorted(Comparator.comparing(IGuildInvitation::getWhen)).forEach(invit -> {
            ItemBuilder it = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner(invit.getReceiver().getName()).setName("§3" + invit.getReceiver());
            IGuildMember sender = guild.getMember(invit.getSender().getUUID());
            it.setLore("");
            if(sender != null){
                it.addLore("§7Invité par §8» " + sender.getGuildRank().getColor() + sender.getName());
            }
            it.addLore("§7Temps restant §8» §3" + TimeUtils.timeToString(5000 - ((System.currentTimeMillis() - invit.getWhen().getTime()) / 1000)));
            if(p != null && p.getGuildRank().ordinal() <= GuildRank.GESTIONNAIRE.ordinal()){
                it.addRightLeftClickLore("Retirer l'invitation");
            }else{
                it.addLore("");
            }
            list.add(it.toItemStack());
        });
        return list;
    }

    @Override
    public void handle(InventoryClickEvent e, int index) {
        checkBack(e, new GuildMainMenu());
        if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM) && p.getGuildRank().ordinal() <= GuildRank.GESTIONNAIRE.ordinal()){
            SkullMeta skullMeta = (SkullMeta) e.getCurrentItem().getItemMeta();
            String name = skullMeta.getOwner();
            PandoniaAPI.get().getProxyLink().sendCommand(e.getWhoClicked().getUniqueId(), "guild deinvite " + name);
        }
    }

    @Override
    public String getMenuName() {
        return "Invitations";
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
