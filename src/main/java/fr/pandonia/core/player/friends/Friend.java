package fr.pandonia.core.player.friends;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.player.friends.IFriend;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.tools.NameFetcher;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Date;
import java.util.UUID;

public class Friend implements IFriend {

    private UUID uuid;
    private String name;
    private Date date;
    private ItemStack head;
    private boolean online;

    public Friend(UUID uuid, String name, Date date, boolean online) {
        this.uuid = uuid;
        this.name = name;
        checkName();
        this.date = date;
        this.online = online;
    }

    public void checkName(){
        if(name == null){
            Bukkit.getScheduler().runTaskAsynchronously(PandoniaAPI.get().getPlugin(), () -> {
                name = NameFetcher.getName(uuid);
                if(PandoniaCore.getCore() != null){
                    if(head == null){
                        loadHead();
                    }
                }
            });
        }
    }

    public Friend(UUID uuid, Date date, boolean online) {
        this.uuid = uuid;
        this.date = date;
        this.online = online;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public ItemStack getHead() {
        return head;
    }

    @Override
    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public void loadHead(){
        if(name != null){
            org.bukkit.inventory.ItemStack s = new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) s.getItemMeta();
            meta.setOwner(name);
            s.setItemMeta(meta);
            head = CraftItemStack.asNMSCopy(s);
        }
    }

    @Override
    public Document toDocument(){
        return new Document("playerUUID", uuid.toString()).append("playerName", name).append("date", date).append("online", online);
    }


    public static IFriend fromDocument(Document document){
        IFriend friend = new Friend(UUID.fromString(document.getString("playerUUID")), document.getString("playerName"), document.getDate("date"), document.getBoolean("online"));
        friend.loadHead();
        return friend;
    }
}