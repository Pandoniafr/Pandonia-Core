package fr.pandonia.core.menus.guilds;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.guild.IGuild;
import fr.pandonia.tools.ItemBuilder;
import fr.pandonia.tools.menu.PandoniaMenu;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuildMainMenu extends PandoniaMenu {

    private IGuild guild;

    @Override
    public void open(HumanEntity p) {
        guild = PandoniaAPI.get().getGuildManager().getGuild(p.getName());
        if(guild == null){
            System.out.println("guild null");
        }
        super.open(p);
    }

    @Override
    public String getMenuName() {
        return "Ma Guilde";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        //     checkBack(e, new ProfilMenu());
        if(e.getSlot() == getSlot(2, 3)){
            new GuildMembersMenu(guild).open(e.getWhoClicked());
        }else if(e.getCurrentItem().getType().equals(Material.HOPPER)){
            new GuildInvitationsMenu(guild).open(e.getWhoClicked());
        }
    }

    @Override
    public void setMenuItems() {
        setCornerGlass();
        setBack();
        set(2, 2, new ItemBuilder(Material.NAME_TAG).setName("§fInformation de la Guilde").setLore(
                "",
                "§7Niveau §8» §3" + 5,
                "§7Saphirs §8» §3" + guild.getSaphirs(),
                ""
        ).toItemStack());

        set(2, 3, new ItemBuilder(Material.DIAMOND_SWORD).setName("§fMembres").setLore(
                "",
                "§fNombre de membre §8» §3" + guild.getMembers().size()
        ).addRightLeftClickLore("Voir les membres").toItemStack());

        set(2, 4, new ItemBuilder(Material.HOPPER).setName("§fInvitations").setRightLeftClickLore("Voir les invitations").toItemStack());
    }

    @Override
    public int getLines() {
        return 3;
    }
}
