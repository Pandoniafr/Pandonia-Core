package fr.pandonia.core.player.settings;

import fr.pandonia.api.player.settings.IPlayerArmorSettings;
import fr.pandonia.tools.playerequiparmor.ArmorType;
import org.bson.Document;

import java.lang.reflect.Field;

public class PlayerArmorSettings implements IPlayerArmorSettings {

    private int activeHelmetID;
    private int activeChestplateID;
    private int activeLeggingsID;
    private int activeBootsID;

    public PlayerArmorSettings(int activeHelmetID, int activeChestplateID, int activeLeggingsID, int activeBootsID) {
        this.activeHelmetID = activeHelmetID;
        this.activeChestplateID = activeChestplateID;
        this.activeLeggingsID = activeLeggingsID;
        this.activeBootsID = activeBootsID;
    }

    @Override
    public int getActivePieceID(ArmorType armorType){
        for (Field field : this.getClass().getDeclaredFields()) {
            if(field.getName().toLowerCase().contains(armorType.name().toLowerCase())){
                try {
                    return (int) field.get(this);
                } catch (IllegalAccessException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    @Override
    public void setActivePieceID(ArmorType armorType, int value){
        for (Field field : this.getClass().getDeclaredFields()) {
            if(field.getName().toLowerCase().contains(armorType.name().toLowerCase())){
                try {
                    field.set(this, value);
                } catch (IllegalAccessException e) {
                    return;
                }
            }
        }
    }

    @Override
    public Document toDocument(){
        return new Document("activeHelmetID", activeHelmetID).append("activeChestplateID", activeChestplateID).append("activeLeggingsID", activeLeggingsID).append("activeBootsID", activeBootsID);
    }

    public static PlayerArmorSettings getDefault(){
        return new PlayerArmorSettings(0, 0, 0, 0);
    }

    public static PlayerArmorSettings fromDocument(Document document){
        if(document != null){
            return new PlayerArmorSettings(document.getInteger("activeHelmetID"), document.getInteger("activeChestplateID"), document.getInteger("activeLeggingsID"), document.getInteger("activeBootsID"));
        }else{
            return getDefault();
        }
    }
}