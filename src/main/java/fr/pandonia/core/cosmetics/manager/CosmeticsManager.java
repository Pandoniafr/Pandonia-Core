package fr.pandonia.core.cosmetics.manager;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.cosmetics.CosmeticType;
import fr.pandonia.api.cosmetics.arena.IArenaBlock;
import fr.pandonia.api.cosmetics.armor.IArmor;
import fr.pandonia.api.cosmetics.arrow.IArrow;
import fr.pandonia.api.cosmetics.gadget.IGadget;
import fr.pandonia.api.cosmetics.killanimation.IKillAnimation;
import fr.pandonia.api.cosmetics.manager.ICosmeticsManager;
import fr.pandonia.api.cosmetics.pet.IPet;
import fr.pandonia.api.cosmetics.pet.IPetArmorStand;
import fr.pandonia.api.player.IPPlayer;
import fr.pandonia.api.player.settings.IPlayerSettings;
import fr.pandonia.core.cosmetics.Cosmetic;
import fr.pandonia.core.cosmetics.arena.ArenaBlock;
import fr.pandonia.core.cosmetics.armor.Armor;
import fr.pandonia.core.cosmetics.arrow.Arrow;
import fr.pandonia.core.cosmetics.gadget.Gadget;
import fr.pandonia.core.cosmetics.killanimation.KillAnimation;
import fr.pandonia.core.cosmetics.pet.Pet;
import fr.pandonia.core.menus.cosmetics.CosmeticsArenaBlocksMenu;
import fr.pandonia.tools.menu.PandoniaMenu;
import fr.pandonia.tools.playerequiparmor.ArmorType;
import org.bson.Document;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class CosmeticsManager implements ICosmeticsManager {

    private PandoniaAPI instance;

    private boolean enabled;

    private List<IPet> pets;
    private List<IArenaBlock> arenaBlocks;
    private List<IPetArmorStand> petArmorStands;
    private List<IArrow> arrows;
    private List<IGadget> gadgets;
    private List<IKillAnimation> killAnimations;
    private List<IArmor> armors;

    public CosmeticsManager(PandoniaAPI instance) {
        this.instance = instance;

        this.enabled = false;

        this.pets = new ArrayList<>();
        this.arenaBlocks = new ArrayList<>();
        this.petArmorStands = new ArrayList<>();
        this.arrows = new ArrayList<>();
        this.gadgets = new ArrayList<>();
        this.killAnimations = new ArrayList<>();
        this.armors = new ArrayList<>();

        this.startUpdateAllPets();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled){
            removeAllPets();
        }
    }

    @Override
    public void registerCosmetics(){
        this.pets.clear();
        this.arenaBlocks.clear();
        this.petArmorStands.clear();
        this.arrows.clear();
        this.gadgets.clear();
        this.killAnimations.clear();
        this.armors.clear();
        instance.getMongoConnection().getMongoDatabase().getCollection("cosmetics").find().forEach((Consumer<Document>) document -> {
            if (document.getString("type") != null){
                CosmeticType type = CosmeticType.valueOf(document.getString("type"));
                if (type.equals(CosmeticType.PET)){
                    pets.add(Pet.fromDocument(document));
                }else if(type.equals(CosmeticType.ARENA_BLOCK)){
                    arenaBlocks.add(ArenaBlock.fromDocument(document));
                }else if(type.equals(CosmeticType.ARROW)){
                    arrows.add(Arrow.fromDocument(document));
                }else if(type.equals(CosmeticType.GADGET)){
                    gadgets.add(Gadget.fromDocument(document));
                }else if(type.equals(CosmeticType.KILL_ANIMATION)){
                    killAnimations.add(KillAnimation.fromDocument(document));
                }else if(type.equals(CosmeticType.ARMOR)){
                    armors.add(Armor.fromDocument(document));
                }
            }
        });
    }

    @Override
    public void onJoin(IPPlayer pp){
        if (pp != null && enabled){
            Player p = instance.getPlugin().getServer().getPlayer(pp.getUUID());
            if (p != null){
                IPlayerSettings ps = instance.getPlayerSettingsManager().getPlayerSettings(p.getUniqueId());
                if (ps != null){
                    if (ps.getActivePetID() > 0){
                        IPet pet = getPet(ps.getActivePetID());
                        if (pet != null){
                            setPet(pet, p);
                        }
                    }else if(ps.getActivePetID() == -1){
                        setPet(getOwnPet(p), p);
                    }
                }
            }

        }
    }

    @Override
    public void onQuit(UUID uuid){
        removePet(uuid);
    }

    @Override
    public void startUpdateAllPets(){
        instance.getPlugin().getServer().getScheduler().runTaskTimer(instance.getPlugin(), this::updateAllPets, 20, 1);
    }

    @Override
    public void updateAllPets(){
        if (enabled){
            petArmorStands.forEach(IPetArmorStand::update);
        }
    }

    @Override
    public void setPet(IPet pet, Player player){
        removePet(player.getUniqueId());
        petArmorStands.add(new fr.pandonia.core.cosmetics.pet.PetArmorStand(pet, player));
    }

    @Override
    public void removePet(UUID uuid){
        for (IPetArmorStand pas : new ArrayList<>(petArmorStands)){
            if (pas.getPlayer().getUniqueId().equals(uuid)){
                pas.remove();
                petArmorStands.remove(pas);
            }
        }
    }

    @Override
    public void removeAllPets(){
        for (IPetArmorStand pas : new ArrayList<>(petArmorStands)){
            pas.remove();
            petArmorStands.remove(pas);
        }
    }

    @Override
    public IPet getPet(int id){
        for (IPet pet : pets){
            if (pet.getID() == id){
                return pet;
            }
        }
        return null;
    }

    @Override
    public IPet getPet(String name){
        for (IPet pet : pets){
            if (pet.getName().equalsIgnoreCase(name)){
                return pet;
            }
        }
        return null;
    }

    @Override
    public IPet getOwnPet(Player p){
        return new fr.pandonia.core.cosmetics.pet.Pet(new Cosmetic(-1, CosmeticType.PET, "§3" + p.getName(), 0, 0, null), "other", p.getName(), Material.LEATHER_CHESTPLATE, Color.WHITE);
    }

    @Override
    public List<IPet> getPetsFromCategory(String category){
        List<IPet> pets = new ArrayList<>();
        for (IPet pet : this.pets){
            if (pet.getCategory().equalsIgnoreCase(category)){
                pets.add(pet);
            }
        }
        return pets;
    }

    @Override
    public IArenaBlock getArenaBlock(int id, int data){
        for(IArenaBlock arenaBlock : arenaBlocks){
            if(arenaBlock.getBlockID() == id && arenaBlock.getBlockData() == data){
                return arenaBlock;
            }
        }
        return null;
    }

    @Override
    public IArenaBlock getArenaBlock(int id){
        for(IArenaBlock arenaBlock : arenaBlocks){
            if(arenaBlock.getID() == id){
                return arenaBlock;
            }
        }
        return null;
    }

    @Override
    public List<IArrow> getArrows() {
        return arrows;
    }

    @Override
    public IArrow getArrow(int id){
        for (IArrow arrow : arrows) {
            if(arrow.getID() == id){
                return arrow;
            }
        }
        return null;
    }

    @Override
    public IArrow getArrow(String name){
        for (IArrow arrow : arrows) {
            if(name.contains(arrow.getName())){
                return arrow;
            }
        }
        return null;
    }

    @Override
    public IGadget getGadget(int id){
        for (IGadget gadget : gadgets) {
            if(gadget.getID() == id){
                return gadget;
            }
        }
        return null;
    }

    @Override
    public IGadget getGadget(ItemStack is){
        for (IGadget gadget : gadgets) {
            if(gadget.getItem().getType() == is.getType() && is.getItemMeta().hasDisplayName() && gadget.getItem().hasItemMeta() && is.getItemMeta().getDisplayName().equalsIgnoreCase(gadget.getItem().getItemMeta().getDisplayName())){
                return gadget;
            }
        }
        return null;
    }

    @Override
    public IGadget getGadget(String name){
        for (IGadget gadget : gadgets) {
            if(name.contains(gadget.getName())){
                return gadget;
            }
        }
        return null;
    }

    @Override
    public List<IGadget> getGadgets() {
        return gadgets;
    }

    @Override
    public IKillAnimation getKillAnimation(int id){
        for (IKillAnimation killAnimation : killAnimations) {
            if(killAnimation.getID() == id){
                return killAnimation;
            }
        }
        return null;
    }

    @Override
    public IKillAnimation getKillAnimation(String name){
        for (IKillAnimation killAnimation : killAnimations) {
            if(name.contains(killAnimation.getName())){
                return killAnimation;
            }
        }
        return null;
    }

    @Override
    public List<IKillAnimation> getKillAnimations() {
        return killAnimations;
    }

    @Override
    public IArmor getArmor(int id){
        for (IArmor armor : armors) {
            if(armor.getID() == id){
                return armor;
            }
        }
        return null;
    }

    @Override
    public IArmor getArmor(ItemStack itemStack){
        for (IArmor armor : armors) {
            ItemStack piece = armor.getPiece(ArmorType.matchType(itemStack));
            if(piece.getType().equals(itemStack.getType())){
                if(piece.getType().toString().contains("LEATHER") && itemStack.getType().toString().contains("LEATHER")){
                    if(((LeatherArmorMeta)itemStack.getItemMeta()).getColor().equals(((LeatherArmorMeta)piece.getItemMeta()).getColor())){
                        return armor;
                    }
                }else{
                    return armor;
                }
            }
        }
        return null;
    }

    @Override
    public List<IArmor> getArmors() {
        return armors;
    }

    @Override
    public List<IPet> getPets() {
        return pets;
    }

    @Override
    public List<IArenaBlock> getArenaBlocks() {
        return arenaBlocks;
    }

    @Override
    public List<IPetArmorStand> getPetArmorStands() {
        return petArmorStands;
    }

    @Override
    public PandoniaMenu getArenaBlockMenu(PandoniaMenu back, Player p){
        return new CosmeticsArenaBlocksMenu(back, p);
    }

}
