package fr.pandonia.core.menus.sanction;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.sanction.infos.ISanctionCategory;
import fr.pandonia.core.player.PPlayer;
import fr.pandonia.tools.ItemBuilder;
import fr.pandonia.tools.TimeUtils;
import fr.pandonia.tools.menu.PandoniaMenu;
import org.bson.Document;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

public class SanctionMenu extends PandoniaMenu {

    private String target;

    public SanctionMenu(String target) {
        this.target = target;
        super.init();
    }

    @Override
    public void init() {

    }

    @Override
    public String getMenuName() {
        return "§8┃ §cPanel de Sanctions";
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (e.getCurrentItem().getItemMeta() != null){
            String name = e.getCurrentItem().getItemMeta().getDisplayName();
            ISanctionCategory sanctionCategory = PandoniaAPI.get().getSanctionsInfosLoader().getSanctionCategory(name);
            if (sanctionCategory != null){
                new SanctionCategoryMenu(target, sanctionCategory).open(e.getWhoClicked());
            }else if (name.equalsIgnoreCase("§8┃ §fSanctions")){
                PandoniaAPI.get().getProxyLink().sendCommand(e.getWhoClicked().getUniqueId(), "sanctions " + target);
                e.getWhoClicked().closeInventory();
            }
        }
        checkBack(e);
    }

    @Override
    public void setMenuItems() {

        Document account = PandoniaAPI.get().getMongoConnection().getMongoDatabase().getCollection("accounts").find(new Document("name", target)).first();

        if (account == null){
            inventory.getViewers().forEach(humanEntity -> humanEntity.sendMessage(PandoniaMessages.getPrefix() + "§fJoueur introuvable..."));
            return;
        }

        PPlayer pTarget = PPlayer.fromDocument(account);

        setContourGlass(DyeColor.RED, 9, 53);

        inventory.setItem(3, new ItemBuilder(Material.WATCH).setName("§8┃ §fTemps de jeu").setLore("", " §8• §fTemps de jeu (lobby, jeu) §f: §3" + TimeUtils.timeToStringAll(pTarget.getPlayingTime()), "").toItemStack());
        inventory.setItem(4, pTarget.getProfileHead());
        inventory.setItem(5, new ItemBuilder(Material.PAPER).setName("§8┃ §fSanctions").setLore("", "    §8» §3Clic-Droit §fou §3Clic-Gauche §f: ", "    §8» §fVoir les sanctions du joueur", "").toItemStack());

        setBack();

        for (ISanctionCategory category : PandoniaAPI.get().getSanctionsInfosLoader().getSanctionCategories()){
            inventory.setItem(category.getInvSlot(), new ItemBuilder(category.getMaterial()).setName(category.getName()).addFlag(ItemFlag.HIDE_ATTRIBUTES).setLore("", "    §8» §3Clic-Droit §fou §3Clic-Gauche §f: ", "    §8» §fAccéder au sous-menu", "").toItemStack());
        }

    }

    @Override
    public int getLines() {
        return 6;
    }

}
