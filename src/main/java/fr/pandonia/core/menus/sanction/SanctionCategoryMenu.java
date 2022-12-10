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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

public class SanctionCategoryMenu extends PandoniaMenu {

    private String target;
    private ISanctionCategory category;

    public SanctionCategoryMenu(String target, ISanctionCategory category) {
        this.target = target;
        this.category = category;
        super.init();
    }

    @Override
    public void init() {

    }

    @Override
    public String getMenuName() {
        return "§8┃ §fCatégorie: " + category.getName();
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (e.getCurrentItem().getItemMeta() != null){
            String name = e.getCurrentItem().getItemMeta().getDisplayName();
            ISanctionItem sanctionItem = PandoniaAPI.get().getSanctionsInfosLoader().getSanctionItem(name);
            if (sanctionItem != null){
                new SanctionItemMenu(target, sanctionItem).open(e.getWhoClicked());
            }
        }
        checkBack(e, new SanctionMenu(target));
    }

    @Override
    public void setMenuItems() {

        Document account = PandoniaAPI.get().getMongoConnection().getMongoDatabase().getCollection("accounts").find(new Document("name", target)).first();

        if (account == null){
            inventory.getViewers().forEach(humanEntity -> humanEntity.sendMessage(PandoniaMessages.getPrefix() + "§fJoueur introuvable..."));
            return;
        }

        PPlayer pTarget = PPlayer.fromDocument(account);

        setContourGlass(DyeColor.getByData(category.getGlassColor()), 9, 53);

        inventory.setItem(4, pTarget.getProfileHead());

        for (ISanctionItem item : PandoniaAPI.get().getSanctionsInfosLoader().getSanctionItemsInCategory(category.getName())){
            inventory.setItem(item.getInvSlot(), new ItemBuilder(item.getMaterial()).setName(item.getName()).addFlag(ItemFlag.HIDE_ATTRIBUTES).setLore("", "    §8» §3Clic-Droit §fou §3Clic-Gauche §f: ", "    §8» §fAccéder au sous-menu", "").toItemStack());
        }

        setBack();
    }

    @Override
    public int getLines() {
        return 6;
    }

}
