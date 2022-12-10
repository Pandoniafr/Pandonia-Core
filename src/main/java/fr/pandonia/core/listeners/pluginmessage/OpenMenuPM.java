package fr.pandonia.core.listeners.pluginmessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.pandonia.tools.menu.PandoniaMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;

public class OpenMenuPM implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        System.out.println("get data");

        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);

        UUID uuid = UUID.fromString(in.readUTF());
        Player p = Bukkit.getPlayer(uuid);
        if(p != null){
            System.out.println("player non null");
            try {
                Class clazz = getClass().getClassLoader().loadClass(in.readUTF());
                ((PandoniaMenu) clazz.newInstance()).open(p);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println(uuid);
        }
    }
}
