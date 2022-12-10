package fr.pandonia.core.guild;

import fr.pandonia.api.guild.GuildRank;
import fr.pandonia.api.guild.IGuildMember;
import fr.pandonia.core.player.SimplePlayer;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Date;

public class GuildMember extends SimplePlayer implements IGuildMember {

    private GuildRank rank;
    private int xpGathered;
    private int saphirsGiven;
    private Date join;
    private ItemStack head;

    public GuildMember(SimplePlayer sp, GuildRank rank, int xpGathered, int saphirsGiven, Date join) {
        super(sp.getUUID(), sp.getName(), sp.getRank(), sp.getNick(), sp.getCustomRankName(), sp.getCustomRankColor());
        this.rank = rank;
        this.xpGathered = xpGathered;
        this.saphirsGiven = saphirsGiven;
        this.join = join;
    }

    @Override
    public GuildRank getGuildRank() {
        return rank;
    }

    @Override
    public int getXPGathered() {
        return xpGathered;
    }

    @Override
    public int getSaphirsGiven() {
        return saphirsGiven;
    }

    @Override
    public Date getJoin() {
        return join;
    }

    @Override
    public void loadHead(){
        if(getName() != null){
            org.bukkit.inventory.ItemStack s = new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) s.getItemMeta();
            meta.setOwner(getName());
            s.setItemMeta(meta);
            this.head = CraftItemStack.asNMSCopy(s);
        }
    }

    @Override
    public ItemStack getHead() {
        return head;
    }

    @Override
    public Document toDocument(){
        return super.toSimpleDocument().append("guildRank", rank.toString()).append("xpGathered", xpGathered).append("saphirsGiven", saphirsGiven).append("join", join);
    }

    public static GuildMember fromDocument(Document document){
        return new GuildMember(SimplePlayer.fromDocument(document), GuildRank.valueOf(document.getString("guildRank")), document.getInteger("xpGathered"), document.getInteger("saphirsGiven"), document.getDate("join"));
    }
}
