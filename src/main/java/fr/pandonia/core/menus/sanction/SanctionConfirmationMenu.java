package fr.pandonia.core.menus.sanction;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.sanction.infos.ISanctionCategory;
import fr.pandonia.api.sanction.infos.ISanctionItem;
import fr.pandonia.core.player.PPlayer;
import fr.pandonia.tools.ItemBuilder;
import fr.pandonia.tools.menu.PandoniaMenu;
import org.bson.Document;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SanctionConfirmationMenu extends PandoniaMenu {

    private String target;
    private ISanctionItem sanctionItem;
    private String command;

    public SanctionConfirmationMenu(String target, ISanctionItem sanctionItem, String command) {
        this.target = target;
        this.sanctionItem = sanctionItem;
        this.command = command;
        super.init();
    }

    @Override
    public void init() {

    }

    @Override
    public String getMenuName() {
        return "§8┃ §fConfirmation: " + sanctionItem.getID();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        String command = inventory.getItem(13).getItemMeta().getLore().get(1);
        if (e.getCurrentItem().getItemMeta() != null && sanctionItem != null){
            String name = e.getCurrentItem().getItemMeta().getDisplayName();
            if (name.equalsIgnoreCase("§aConfirmer ?")){
                PandoniaAPI.get().getProxyLink().sendCommand(e.getWhoClicked().getUniqueId(), command.replaceAll("§f/", ""));
                e.getWhoClicked().closeInventory();
            }else if (name.equalsIgnoreCase("§cAnnuler")){
                new SanctionItemMenu(target, sanctionItem).open(e.getWhoClicked());
            }
        }
    }

    @Override
    public void setMenuItems() {
        Document account = PandoniaAPI.get().getMongoConnection().getMongoDatabase().getCollection("accounts").find(new Document("name", target)).first();

        if (account == null){
            inventory.getViewers().forEach(humanEntity -> humanEntity.sendMessage(PandoniaMessages.getPrefix() + "§fJoueur introuvable..."));
            return;
        }

        PPlayer pTarget = PPlayer.fromDocument(account);

        ISanctionCategory category = PandoniaAPI.get().getSanctionsInfosLoader().getSanctionCategory(sanctionItem.getCategory());
        DyeColor color = DyeColor.BLUE;
        if (category != null){
            color = DyeColor.getByDyeData(category.getGlassColor());
        }

        setContourGlass(color, 0, getSlots()-1);

        inventory.setItem(4, pTarget.getProfileHead());

        inventory.setItem(11, new ItemBuilder(Material.STAINED_CLAY, 1, (byte) 5).setName("§aConfirmer ?").toItemStack());
        inventory.setItem(13, new ItemBuilder(Material.SIGN).setName("§fVérification").setLore(" ", "§f/" + command, " ").toItemStack());
        inventory.setItem(15, new ItemBuilder(Material.STAINED_CLAY, 1, (byte) 14).setName("§cAnnuler").toItemStack());
    }

    @Override
    public int getLines() {
        return 3;
    }

}
