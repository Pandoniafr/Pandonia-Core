package fr.pandonia.core.npc.manager;

import fr.pandonia.api.npc.manager.INPCManager;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.tools.npc.NPCLib;
import fr.pandonia.tools.npc.PandoniaNPC;
import fr.pandonia.tools.npc.api.events.NPCInteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NPCManager implements INPCManager, Listener {

    private PandoniaCore instance;

    private boolean enabled;
    private List<PandoniaNPC> npcs;

    public NPCManager(PandoniaCore instance) {
        this.instance = instance;

        this.enabled = false;
        this.npcs = new ArrayList<>();
    }

    @Override
    public void enableNPCs() {
        instance.setNPCLib(new NPCLib(instance.getPlugin()));
        startUpdateTask();
        this.enabled = true;
    }

    public void startUpdateTask(){
        instance.getPlugin().getServer().getScheduler().runTaskTimer(instance.getPlugin(), () -> {
            for (PandoniaNPC npc : npcs){
                if(npc.updateLines()){
                    npc.updateView();
                }
            }
        }, 0, 20);
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent e){
        System.out.println("Inteaction NPC " + e.getNPC().getEntityId());
        List<PandoniaNPC> npc = npcs.stream().filter(pandoraNPC -> pandoraNPC.getNpc().getEntityId() == e.getNPC().getEntityId()).collect(Collectors.toList());
        npc.forEach(pandoraNPC -> {
            pandoraNPC.onInteract(e.getWhoClicked());
        });
    }

    @Override
    public void onJoin(Player p) {
        for (PandoniaNPC npc : npcs){
            npc.show(p);
        }
    }

    @Override
    public void addNPC(PandoniaNPC npc) {
        this.npcs.add(npc);
        Bukkit.getOnlinePlayers().forEach(npc::show);
        System.out.println("Ajout NPC " + npc.getSkin());
    }

    @Override
    public List<PandoniaNPC> getNPCs() {
        return npcs;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
