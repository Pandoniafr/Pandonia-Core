package fr.pandonia.core.sanction.infos.loader;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.sanction.infos.ISanctionCategory;
import fr.pandonia.api.sanction.infos.ISanctionItem;
import fr.pandonia.api.sanction.infos.loader.ISanctionsInfosLoader;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SanctionsInfosLoader implements ISanctionsInfosLoader {

    private PandoniaAPI instance;

    private List<ISanctionCategory> sanctionCategories;
    private List<ISanctionItem> sanctionItems;

    public SanctionsInfosLoader(PandoniaAPI instance) {
        this.instance = instance;

        this.sanctionCategories = new ArrayList<>();
        this.sanctionItems = new ArrayList<>();
    }

    public void load(){
        sanctionCategories = new ArrayList<>();
        sanctionItems = new ArrayList<>();
        instance.getMongoConnection().getMongoDatabase().getCollection("sanctions_categories").find().forEach((Consumer<Document>) document -> {
            sanctionCategories.add(fr.pandonia.core.sanction.infos.SanctionCategory.fromDocument(document));
        });
        instance.getMongoConnection().getMongoDatabase().getCollection("sanctions_items").find().forEach((Consumer<Document>) document -> {
            sanctionItems.add(fr.pandonia.core.sanction.infos.SanctionItem.fromDocument(document));
        });
    }

    public ISanctionCategory getSanctionCategory(String name){
        for (ISanctionCategory category : sanctionCategories){
            if (category.getName().equalsIgnoreCase(name)){
                return category;
            }
        }
        return null;
    }

    public List<ISanctionCategory> getSanctionCategories() {
        return sanctionCategories;
    }

    public ISanctionItem getSanctionItem(String name){
        for (ISanctionItem item : sanctionItems){
            if (item.getName().equalsIgnoreCase(name)){
                return item;
            }
        }
        return null;
    }

    public ISanctionItem getSanctionItem(int id){
        for (ISanctionItem item : sanctionItems){
            if (item.getID() == id){
                return item;
            }
        }
        return null;
    }

    public List<ISanctionItem> getSanctionItemsInCategory(String categoryName){
        List<ISanctionItem> items = new ArrayList<>();
        for (ISanctionItem item : sanctionItems){
            if (item.getCategory().equalsIgnoreCase(categoryName)){
                items.add(item);
            }
        }
        return items;
    }

    public List<ISanctionItem> getSanctionItems() {
        return sanctionItems;
    }

}
