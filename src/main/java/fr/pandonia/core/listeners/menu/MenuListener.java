package fr.pandonia.core.listeners.menu;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.tools.menu.PandoniaMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {

    private PandoniaAPI instance;

    public MenuListener(PandoniaAPI instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(e.getInventory().getHolder() instanceof PandoniaMenu && !e.getClick().equals(ClickType.DOUBLE_CLICK)){
            if(e.getCurrentItem() != null){
                e.setCancelled(true);
                PandoniaMenu menu = (PandoniaMenu) e.getInventory().getHolder();
                menu.handleMenu(e);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof PandoniaMenu){
            PandoniaMenu menu = (PandoniaMenu) e.getInventory().getHolder();
            menu.onClose(e);
        }
    }

}
