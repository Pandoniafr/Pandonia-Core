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
import org.bukkit.inventory.ItemStack;

public class SanctionItemMenu extends PandoniaMenu {

    private String target;
    private ISanctionItem sanctionItem;

    public SanctionItemMenu(String target, ISanctionItem sanctionItem) {
        this.target = target;
        this.sanctionItem = sanctionItem;
        super.init();
    }

    @Override
    public void init() {

    }

    @Override
    public String getMenuName() {
        return "§8┃ §fItem: " + sanctionItem.getID();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {

        ItemStack item = e.getCurrentItem();
        ISanctionCategory category = PandoniaAPI.get().getSanctionsInfosLoader().getSanctionCategory(sanctionItem.getCategory());

        if (item.getItemMeta() != null){
            String name = item.getItemMeta().getDisplayName();
            StringBuilder command = new StringBuilder();
            String level = null;
            if (name.equalsIgnoreCase("§8┃ §eNiveau 1")){
                level = sanctionItem.getLevels().get(0);
            }else if (name.equalsIgnoreCase("§8┃ §6Niveau 2")){
                level = sanctionItem.getLevels().get(1);
            }else if (name.equalsIgnoreCase("§8┃ §cNiveau 3")){
                level = sanctionItem.getLevels().get(2);
            }
            if (level != null){
                command.append(level.split(" ")[0]);
                command.append(" ").append(target).append(" ");
                command.append(level.split(" ")[1]).append(" ");
                command.append(sanctionItem.getName());
                new SanctionConfirmationMenu(target, sanctionItem, command.toString()).open(e.getWhoClicked());
            }
        }

        checkBack(e, new SanctionCategoryMenu(target, category));

    }

    @Override
    public void setMenuItems() {
        Document account = PandoniaAPI.get().getMongoConnection().getMongoDatabase().getCollection("accounts").find(new Document("name", target)).first();
        ISanctionCategory category = PandoniaAPI.get().getSanctionsInfosLoader().getSanctionCategory(sanctionItem.getCategory());

        if (account == null || category == null){
            inventory.getViewers().forEach(humanEntity -> humanEntity.sendMessage(PandoniaMessages.getPrefix() + "§fJoueur introuvable..."));
            return;
        }

        PPlayer pTarget = PPlayer.fromDocument(account);

        setContourGlass(DyeColor.getByDyeData(category.getGlassColor()), 9, 53);

        inventory.setItem(3, pTarget.getProfileHead());

        inventory.setItem(5, new ItemBuilder(sanctionItem.getMaterial()).setName("§8┃ " + sanctionItem.getName() + "§f: " + target).setLore(" ", "    §8┃ §fCatégorie: " + sanctionItem.getCategory(), " ").toItemStack());

        inventory.setItem(13, new ItemBuilder(Material.STAINED_CLAY, 1, (byte) 5).setName("§8┃ §aWarn").setLore(" ", "    §8┃ §fStatut: " + (sanctionItem.isWarn() ? "§aON" : "§cOFF"), "    §8» §cPas encore disponible", " ").toItemStack());

        inventory.setItem(20, new ItemBuilder(Material.STAINED_CLAY, 1, (byte) 4).setName("§8┃ §eNiveau 1").setLore(" ", "    §8┃ §fSanction: §3" + sanctionItem.getLevels().get(0), " ", "    §8» §3Clic-Droit §fou §3Clic-Gauche §f: ", "    §8» §fAppliquer la sanction", " ").toItemStack());
        inventory.setItem(22, new ItemBuilder(Material.STAINED_CLAY, 1, (byte) 1).setName("§8┃ §6Niveau 2").setLore(" ", "    §8┃ §fSanction: §3" + sanctionItem.getLevels().get(1), " ", "    §8» §3Clic-Droit §fou §3Clic-Gauche §f: ", "    §8» §fAppliquer la sanction", " ").toItemStack());
        inventory.setItem(24, new ItemBuilder(Material.STAINED_CLAY, 1, (byte) 14).setName("§8┃ §cNiveau 3").setLore(" ", "    §8┃ §fSanction: §3" + sanctionItem.getLevels().get(2), " ", "    §8» §3Clic-Droit §fou §3Clic-Gauche §f: ", "    §8» §fAppliquer la sanction", " ").toItemStack());

        setBack();

    }

    @Override
    public int getLines() {
        return 6;
    }
}