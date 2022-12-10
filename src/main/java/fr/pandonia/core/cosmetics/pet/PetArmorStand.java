package fr.pandonia.core.cosmetics.pet;

import fr.pandonia.api.cosmetics.pet.IPet;
import fr.pandonia.api.cosmetics.pet.IPetArmorStand;
import fr.pandonia.tools.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

public class PetArmorStand implements IPetArmorStand {

    private IPet pet;
    private Player player;

    private ArmorStand armorStand;

    public PetArmorStand(IPet pet, Player player) {
        this.pet = pet;
        this.player = player;
        final Location spawnloc = player.getLocation().clone().add(new Vector(0.5, 1, 0.5));
        armorStand = (ArmorStand) spawnloc.getWorld().spawnEntity(spawnloc, EntityType.ARMOR_STAND);
        ItemStack skull;
        if (Bukkit.getPlayer(pet.getHeadTexture()) != null){
            skull = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setSkullOwner(pet.getHeadTexture()).toItemStack();
        }else{
            skull = new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(pet.getHeadTexture()).toItemStack();
        }
        armorStand.setHelmet(skull);
        armorStand.setCustomName(pet.getName());
        armorStand.setCustomNameVisible(true);
        if(pet.getChestplate() != Material.AIR && pet.getChestplate().toString().contains("CHESTPLATE")) {
            ItemStack chestplate = new ItemStack(pet.getChestplate());
            if (pet.getChestplate().equals(Material.LEATHER_CHESTPLATE) && pet.getChestplateColor() != null){
                final LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
                meta.setColor(pet.getChestplateColor());
                chestplate.setItemMeta(meta);
            }
            armorStand.setChestplate(chestplate);
        }
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setCanPickupItems(false);
        armorStand.setBasePlate(false);
        armorStand.setSmall(true);
    }

    @Override
    public void update() {
        double yaw = player.getLocation().getYaw() + 90;
        if(yaw > 180) yaw-=360;
        double angle  = (((yaw + 90)  * Math.PI) / 180);

        double x = Math.cos(angle) * 0.75;
        double z = Math.sin(angle) * 0.75;
        this.armorStand.teleport(player.getLocation().clone().add(new Vector(x, 1, z)));
    }

    @Override
    public void remove(){
        armorStand.remove();
    }

    @Override
    public IPet getPet() {
        return pet;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public ArmorStand getArmorStand() {
        return armorStand;
    }

}
