package fr.pandonia.core.menus.cosmetics;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.cosmetics.arena.IArenaBlock;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.owning.IPlayerOwning;
import fr.pandonia.api.player.settings.IPlayerSettings;
import fr.pandonia.tools.ItemBuilder;
import fr.pandonia.tools.menu.PandoniaMenu;
import fr.pandonia.tools.menu.page.NewPageMenu;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Comparator;

@SuppressWarnings("deprecation")
public class CosmeticsArenaBlocksMenu extends NewPageMenu {

    private PandoniaMenu back;
    private Player p;
    private IPPlayer pplayer;
    private IPlayerSettings playerSettings;
    private IPlayerOwning playerOwning;

    public CosmeticsArenaBlocksMenu(PandoniaMenu back, Player p) {
        this.back = back;
        this.p = p;
        this.pplayer = PandoniaAPI.get().getPlayerManager().getPlayer(p.getUniqueId());
        this.playerSettings = PandoniaAPI.get().getPlayerSettingsManager().getPlayerSettings(p.getUniqueId());
        this.playerOwning = PandoniaAPI.get().getPlayerOwningManager().getPlayerOwning(p.getUniqueId());
        super.init();
    }

    @Override
    public void init() {
    }

    @Override
    public String getMenuName() {
        return "Choisir un bloc";
    }

    @Override
    public void updateItems() {
        PandoniaAPI.get().getCosmeticsManager().getArenaBlocks().stream().sorted(Comparator.comparing(IArenaBlock::getBlockID)).forEach(arenaBlock -> {
            ItemBuilder it = new ItemBuilder(Material.getMaterial(arenaBlock.getBlockID()), 1, arenaBlock.getBlockData()).setGlow(playerSettings.getActiveArenaBlockID() == arenaBlock.getID()).setName("§a" + arenaBlock.getName());
            it.setLore("");
            if(arenaBlock.getID() != 1000){
                it.addLore("   §8» §fPossédé §f: §3" + (playerOwning.getPlayerCosmeticsOwning().isOwningCosmetic(arenaBlock.getID()) ? "§a✔" : "§c✖"));
            }
            if(playerOwning.getPlayerCosmeticsOwning().isOwningCosmetic(arenaBlock.getID()) || arenaBlock.getID() == 1000){
                it.addLore("   §8» §fActif §f: §3" + (playerSettings.getActiveArenaBlockID() == arenaBlock.getID() ? "§a✔" : "§c✖"));
            }
            if (arenaBlock.getHowToGetIt() != null){
                it.addLore("");
                it.addLore("   §8» " + arenaBlock.getHowToGetIt());
                it.addLore("");
            }
            if(playerSettings.getActiveArenaBlockID() == arenaBlock.getID()){
                it.addLore("   §8» §eCe bloc est activé" );
                it.addLore("");
            }else if(playerOwning.getPlayerCosmeticsOwning().isOwningCosmetic(arenaBlock.getID()) || arenaBlock.getID() == 1000){
                it.addRightLeftClickLore("Activer ce bloc");
            }else{
                it.addLore("");
            }
            addItem(it.toItemStack(), e -> {
                if (playerOwning.getPlayerCosmeticsOwning().isOwningCosmetic(arenaBlock.getID()) || arenaBlock.getID() == 1000) {//Activer
                    playerSettings.setActiveArenaBlockID(arenaBlock.getID());
                    p.sendMessage(PandoniaMessages.getPrefix() + "§fVous venez d'activer le bloc " + arenaBlock.getName() + "§f.");
                    p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1, 1);
                    setMenuItems();
                }
            });
        });
    }

    @Override
    public void handle(InventoryClickEvent e, int index) {
        checkBack(e, back);
    }

    @Override
    public void setMenuItems() {
        setContourGlass();
        setBack();
        setItemOnPage();
        setPageItems();
    }

    @Override
    public int getLines() {
        return 4;
    }
}
