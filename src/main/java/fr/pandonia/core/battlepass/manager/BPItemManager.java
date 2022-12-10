package fr.pandonia.core.battlepass.manager;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.battlepass.IBPChallenge;
import fr.pandonia.api.battlepass.IBPItem;
import fr.pandonia.api.battlepass.manager.IBPItemManager;
import fr.pandonia.api.cosmetics.pet.IPet;
import fr.pandonia.api.messages.PandoniaMessages;
import fr.pandonia.api.player.owning.IPlayerOwning;
import fr.pandonia.core.PandoniaCore;
import fr.pandonia.core.battlepass.BPChallenge;
import fr.pandonia.core.battlepass.BPItem;
import fr.pandonia.tools.Heads;
import fr.pandonia.tools.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BPItemManager implements IBPItemManager {

    private PandoniaCore instance;

    private List<IBPItem> items;
    private List<IBPChallenge> challenges;

    public BPItemManager(PandoniaCore instance) {
        this.instance = instance;
        this.items = new ArrayList<>();
        this.challenges = new ArrayList<>();
        Bukkit.getScheduler().runTaskLater(instance.getPlugin(), this::register, 20);
    }

    public void register(){
        items.add(new BPItem(0, "Ajouter des amis", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.FOX).toItemStack(), true, null));
        items.add(new BPItem(1, "500 Saphirs", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.SAPHIRS).toItemStack(), false, event -> event.getPlayer().addSaphirs(500)));
        items.add(new BPItem(2, "Blocs de bois (Arena)", new ItemStack(Material.WOOD), false, event -> {
            IPlayerOwning owning = PandoniaAPI.get().getPlayerOwningManager().getPlayerOwning(event.getPlayer().getUUID());
            if (owning != null){
                owning.getPlayerCosmeticsOwning().addCosmetic(1001);
            }
        }));
        items.add(new BPItem(3, "Pet aléatoire",  new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.LUFFY).toItemStack(), false, event -> {
            List<Integer> own = new ArrayList<>(PandoniaAPI.get().getPlayerOwningManager().getPlayerOwning(event.getPlayer().getUUID()).getPlayerCosmeticsOwning().getCosmetics());
            own.removeIf(integer -> integer < 1000);
            List<Integer> notOwned = new ArrayList<>();
            for (IPet pet : PandoniaAPI.get().getCosmeticsManager().getPets()){
                if (!own.contains(pet.getID())  ){
                    notOwned.add(pet.getID());
                }
            }
            if (notOwned.isEmpty()){
                event.getPlayer().sendMessage(PandoniaMessages.getCasino() + "§fVous possédez déjà tous les pets... Vous gagnez alors §3100 Saphirs §fen compensation.");
                event.getPlayer().addSaphirs(100);
            }else{
                Random r = new Random();
                int i = notOwned.get(r.nextInt(notOwned.size()));
                IPet pet = PandoniaAPI.get().getCosmeticsManager().getPet(i);
                if (pet != null){
                    PandoniaAPI.get().getPlayerOwningManager().getPlayerOwning(event.getPlayer().getUUID()).getPlayerCosmeticsOwning().isOwningCosmetic(pet.getID());
                    event.getPlayer().sendMessage(PandoniaMessages.getCasino() + "§fVous obtenez le pet " + pet.getName() + "§f.");
                }else{
                    event.getPlayer().sendMessage(PandoniaMessages.getErreur() + "§fUne erreur est survenue, veuillez contacter un membre du staff...");
                }
            }
        }));
        items.add(new BPItem(4, "3 hosts", new ItemStack(Material.NAME_TAG), false, event -> event.getPlayer().addHosts(3)));
        items.add(new BPItem(5, "2 Lootboxs", new ItemStack(Material.ENDER_CHEST), false, event -> {
            /*event.getPlayer().sendMessage(PandoraMessages.getPrefix() + "§fLa réclamation des §9Boîtes de Pandore §fest pour l'instant désactivé. Nous faisons §9au plus vite§f...");
            throw new BPException();*/
            event.getPlayer().addLootbox(2);
        }));
        items.add(new BPItem(6, "Boost XP (x1.25)", new ItemStack(Material.EXP_BOTTLE), true, event -> event.getBattlePass().setBoosted(true)));
        items.add(new BPItem(7, "Traînée de flèches \"coeur\"", new ItemStack(Material.ARROW), false, event -> {
            IPlayerOwning owning = PandoniaAPI.get().getPlayerOwningManager().getPlayerOwning(event.getPlayer().getUUID());
            if (owning != null){
                owning.getPlayerCosmeticsOwning().addCosmetic(2002);
            }
        }));
        items.add(new BPItem(8, "Gadget \"Gant de Katchan\"", new ItemStack(Material.TNT), false, event -> {
            IPlayerOwning owning = PandoniaAPI.get().getPlayerOwningManager().getPlayerOwning(event.getPlayer().getUUID());
            if (owning != null){
                owning.getPlayerCosmeticsOwning().addCosmetic(3000);
            }
        }));
        items.add(new BPItem(9, "Pack armure en cuir (Lobby)", new ItemBuilder(Material.LEATHER_HELMET).setLeatherArmorColor(Color.ORANGE).toItemStack(), false, event -> {
            IPlayerOwning owning = PandoniaAPI.get().getPlayerOwningManager().getPlayerOwning(event.getPlayer().getUUID());
            if (owning != null){
                owning.getPlayerCosmeticsOwning().addCosmetic(5001);
            }
        }));
        items.add(new BPItem(10, "Blocs de spruce (Arena)", new ItemStack(Material.WOOD, 1, (short) 1), false, event -> {
            IPlayerOwning owning = PandoniaAPI.get().getPlayerOwningManager().getPlayerOwning(event.getPlayer().getUUID());
            if (owning != null){
                owning.getPlayerCosmeticsOwning().addCosmetic(1002);
            }
        }));
        items.add(new BPItem(11, "2 Lootboxs", new ItemStack(Material.ENDER_CHEST), false, event -> {
            /*event.getPlayer().sendMessage(PandoniaMessages.getPrefix() + "§fLa réclamation des §9Lootbox §fest pour l'instant désactivé. Nous faisons §9au plus vite§f...");
            throw new BPException();*/
            event.getPlayer().addLootbox(2);
        }));
        items.add(new BPItem(12, "Pet à son effigie", new ItemStack(Material.AIR), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(-1)){
            @Override
            public ItemStack getItem(Player p) {
                return new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setSkullOwner(p.getName()).toItemStack();
            }
        });
        items.add(new BPItem(13, "2000 Saphirs", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.SAPHIRS).toItemStack(), false, event -> event.getPlayer().addSaphirs(2000)));
        items.add(new BPItem(14, "5 Lootboxs", new ItemStack(Material.ENDER_CHEST), true, event -> {
            /*event.getPlayer().sendMessage(PandoniaMessages.getPrefix() + "§fLa réclamation des §9Lootbox §fest pour l'instant désactivé. Nous faisons §9au plus vite§f...");
            throw new BPException();*/
            event.getPlayer().addLootbox(5);
        }));
        items.add(new BPItem(15, "Pet \"Inosuke\"", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(instance.getCosmeticsManager().getPet(63).getHeadTexture()).toItemStack(), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(63)));
        items.add(new BPItem(16, "Pack armure en or (lobby)", new ItemStack(Material.GOLD_HELMET), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(5000)));
        items.add(new BPItem(17, "Traînée de flèches \"éclair\"", new ItemStack(Material.ARROW), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(2004)));
        items.add(new BPItem(19, "Pet \"Tanjiro\"", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(instance.getCosmeticsManager().getPet(64).getHeadTexture()).toItemStack(), true, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(64)));
        items.add(new BPItem(20, "Pet \"Zenitsu\"", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(instance.getCosmeticsManager().getPet(67).getHeadTexture()).toItemStack(), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(67)));
        items.add(new BPItem(21, "5 hosts", new ItemStack(Material.NAME_TAG), false, event -> event.getPlayer().addHosts(5)));
        items.add(new BPItem(22, "Blocs d'or (Arena)", new ItemStack(Material.GOLD_BLOCK), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(1003)));
        items.add(new BPItem(23, "Pet \"Kyojuro\"", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(instance.getCosmeticsManager().getPet(68).getHeadTexture()).toItemStack(), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(68)));
        items.add(new BPItem(24, "5000 Saphirs", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.SAPHIRS).toItemStack(), true, event -> event.getPlayer().addSaphirs(5000)));
        items.add(new BPItem(25, "Animation \"Feu\" (kill)", new ItemStack(Material.BLAZE_POWDER), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(4001)));
        items.add(new BPItem(26, "Débloque : Création d'une guilde", new ItemBuilder(Material.BANNER).setBannerColor(DyeColor.CYAN).toItemStack(), false, null));
        items.add(new BPItem(27, "5000 Saphirs", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.SAPHIRS).toItemStack(), false, event -> event.getPlayer().addSaphirs(5000)));
        items.add(new BPItem(28, "Gadget \"Bombe de Katchan\"", new ItemBuilder(Material.FIREBALL).toItemStack(), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(3001)));
        items.add(new BPItem(29, "Boost x1.25 de Guilde (1 mois)", new ItemBuilder(Material.BANNER).setBannerColor(DyeColor.ORANGE).setGlow(true).toItemStack(), true, null));
        items.add(new BPItem(30, "Boost x1.5 de Guilde (1 mois)", new ItemBuilder(Material.BANNER).setBannerColor(DyeColor.RED).setGlow(true).toItemStack(), false, null));
        items.add(new BPItem(31, "Blocs d'émeraude (Arena)", new ItemStack(Material.EMERALD_BLOCK), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(1005)));
        items.add(new BPItem(32, "Traînée de flèches \"goutte d'eau\"", new ItemStack(Material.WATER_BUCKET), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(2003)));
        items.add(new BPItem(33, "Traînée de flèches \"notes de musiques\"", new ItemStack(Material.NOTE_BLOCK), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(2001)));
        items.add(new BPItem(34, "Fly dans le lobby (1 mois)", new ItemStack(Material.LEATHER), false, null));
        items.add(new BPItem(35, "Animation \"Pokeball\" (kill)", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.POKEBALL).toItemStack(), false, event -> event.getOwning().getPlayerCosmeticsOwning().addCosmetic(4000)));
        items.add(new BPItem(36, "Alter \"clônage\"", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.POKEBALL).toItemStack(), false, null, "Spawn de 4 clones à côté de soi au lobby"){
            @Override
            public ItemStack getItem(Player p) {
                return new ItemBuilder(Material.SKULL_ITEM, 4, (short) 3).setSkullOwner(p.getName()).toItemStack();
            }
        });
        items.add(new BPItem(37, "Gadget \"Time Out\" (lobby)", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.CLOCK).toItemStack(), false, null, "Tous les joueurs sont freeze sauf le détenteur"));
        items.add(new BPItem(38, "Pack Pet Piliers Slayers", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.TANJIRO).toItemStack(), false, null, "Kagaya, Tomioka, Shinobu, Tengen", "Gyomei, Muichiro, Mitsuri, Obanai"));
        items.add(new BPItem(39, "Pack fruits du démons (lobby)", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.LUFFY).toItemStack(), false, null));
        items.add(new BPItem(40, "Pack de sorts Harry Potter (lobby)", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.HARRY_POTTER).toItemStack(), true, null));
        items.add(new BPItem(41, "Boost x1.50 de Saphirs (3 mois)", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.SAPHIRS).toItemStack(), true, null));
        items.add(new BPItem(42, "Animation \"TNT\" (kill)", new ItemStack(Material.TNT), false, null, "Animation d'explosion au kill"));
        items.add(new BPItem(43, "Pack Pet Lunes Supérieures", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).getSkull(Heads.SAPHIRS).toItemStack(), false, null, "Muzan, Kokushibo, Doma", "Akaza, Nakime, Gyokko, Gyutaro"));
        items.add(new BPItem(44, "10 Lootboxs", new ItemStack(Material.ENDER_CHEST), false, event -> event.getPlayer().addLootbox(10)));
        items.add(new BPItem(45, "Grade §bLEGENDE §r3 mois", new ItemStack(Material.NAME_TAG), true, null));
        items.add(new BPItem(46, "Annonces gratuites", new ItemBuilder(Material.DIAMOND).setGlow(true).toItemStack(), false, null, "Toutes vos annonces de hosts", "aux lobbys sont gratuites"));
        items.add(new BPItem(47, "20 hosts", new ItemStack(Material.NAME_TAG), false, event -> event.getPlayer().addHosts(20)));

        challenges.add(new BPChallenge(1, "Gagner une partie en tant que Héros", "mha", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.IZUKU).toItemStack()));
        challenges.add(new BPChallenge(2, "Gagner une partie en tant que Vilain", "mha", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.TOMURA).toItemStack()));
        challenges.add(new BPChallenge(3, "Gagner une partie en tant que Solo", "mha", 1500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.STAIN).toItemStack()));
        challenges.add(new BPChallenge(4, "Gagner une partie en tant que Préceptes", "mha", 1500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.OVERHAUL).toItemStack()));

        challenges.add(new BPChallenge(5, "Gagner une partie en tant que Pirate", "wano", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.LUFFY).toItemStack()));
        challenges.add(new BPChallenge(6, "Gagner une partie en tant que Pirate Fidèle", "wano", 1000, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.LUFFY).setGlow(true).toItemStack()));
        challenges.add(new BPChallenge(7, "Gagner une partie en tant qu'Empereur", "wano", 1000, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.SHANKS).toItemStack()));

        /*challenges.add(new BPChallenge(8, "Gagner une partie en tant que Griffondor", "hp", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.HARRY_POTTER).toItemStack()));
        challenges.add(new BPChallenge(9, "Gagner une partie en tant que Serpentard", "hp", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.DRAGO).toItemStack()));
        challenges.add(new BPChallenge(10, "Gagner une partie en tant que Serdaigle", "hp", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.LUNA).toItemStack()));
        challenges.add(new BPChallenge(11, "Gagner une partie en tant que Poufsouffle", "hp", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.DIGORY).toItemStack()));
        challenges.add(new BPChallenge(12, "Gagner une partie en tant que Ministère", "hp", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.ARTHUR_WEASLEY).toItemStack()));
        challenges.add(new BPChallenge(13, "Gagner une partie en tant que Moldus", "hp", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.DURSLEY).toItemStack()));
        challenges.add(new BPChallenge(14, "Gagner une partie en tant que Mangemort", "hp", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.VOLDEMORT).toItemStack()));
        challenges.add(new BPChallenge(15, "Gagner une partie en tant que Professeurs", "hp", 500, new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).getSkull(Heads.EARTH).toItemStack()));*/
    }

    @Override
    public List<IBPItem> getItems() {
        return items;
    }

    @Override
    public IBPItem getItem(int level){
        for (IBPItem item : items) {
            if(item.getLevel() == level){
                return item;
            }
        }
        return null;
    }

    @Override
    public List<IBPChallenge> getChallenges() {
        return challenges;
    }

    @Override
    public IBPChallenge getChallenge(String name){
        for (IBPChallenge challenge : challenges) {
            if(challenge.getName().equalsIgnoreCase(name)){
                return challenge;
            }
        }
        return null;
    }

    @Override
    public IBPChallenge getChallenge(int id){
        for (IBPChallenge challenge : challenges) {
            if(challenge.getId() == id){
                return challenge;
            }
        }
        return null;
    }
}
