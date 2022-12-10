package fr.pandonia.core.pubsub.manager;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.application.PandoniaApplication;
import fr.pandonia.api.events.PubSubMessageEvent;
import fr.pandonia.api.pubsub.MessageReceiver;
import fr.pandonia.api.pubsub.Way;
import fr.pandonia.api.pubsub.manager.IMessageReceiverManager;
import fr.pandonia.core.pubsub.list.*;
import org.bson.Document;
import org.bson.json.JsonParseException;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageReceiverManager extends JedisPubSub implements IMessageReceiverManager {

    private Map<Way, Class<? extends MessageReceiver>> receivers;

    private PandoniaApplication instance;

    public MessageReceiverManager(PandoniaApplication instance) {
        this.instance = instance;

        this.receivers = new HashMap<>();
    }

    public void register(){
        registerMessageReceiver(new Way("proxy", "reloadRanks"), ReloadRanksMR.class);
        registerMessageReceiver(new Way("proxy", "stopServer"), StopServerMR.class);
        registerMessageReceiver(new Way("proxy", "getAccount"), GetAccountMR.class);
        registerMessageReceiver(new Way("proxy", "sendFriendAccept"), SendFriendAcceptMR.class);
        registerMessageReceiver(new Way("proxy", "sendModifyPlayer"), SendModifyPlayerMR.class);
        registerMessageReceiver(new Way("proxy", "askToSave"), AskToSaveMR.class);
        registerMessageReceiver(new Way("proxy", "annonceServer"), AnnonceServerMR.class);
        registerMessageReceiver(new Way("proxy", "sendFriendNotification"), SendFriendNotificationMR.class);
        registerMessageReceiver(new Way("proxy", "updatePlayerSettings"), UpdatePlayerSettingsMR.class);
        registerMessageReceiver(new Way("proxy", "updateGuild"), UpdateGuildMR.class);
        registerMessageReceiver(new Way("proxy", "updateHostStatus"), UpdateHostStatusMR.class);
    }

    @Override
    public void registerMessageReceiver(Way way, Class<? extends MessageReceiver> receiver) {
        if (!get(way).isPresent()){
            receivers.put(way, receiver);
        }
    }

    @Override
    public Optional<Class<? extends MessageReceiver>> get(Way way) {
        for (Map.Entry<Way, Class<? extends MessageReceiver>> entry : receivers.entrySet()){
            Way entryWay = entry.getKey();
            if (entryWay.getChannel().equals(way.getChannel()) && entryWay.getSubChannel().equals(way.getSubChannel())){
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }

    @Override
    public void execute(Way way, Document message){
        Optional<Class<? extends MessageReceiver>> get = get(way);
        if (get.isPresent()){
            try {
                get.get().getConstructor(Document.class).newInstance(message).execute();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<Way, Class<? extends MessageReceiver>> getReceivers() {
        return receivers;
    }

    @Override
    public void onMessage(String channel, String message) {
        try {
            Document json = Document.parse(message);
            if (instance instanceof PandoniaAPI){
                ((PandoniaAPI)instance).getPlugin().getServer().getPluginManager().callEvent(new PubSubMessageEvent(channel, json.getString("subChannel"), (Document) json.get("message")));
            }
            execute(new Way(channel, json.getString("subChannel")), json.get("message", Document.class));
        }catch (JsonParseException e){
            e.printStackTrace();
            System.out.println(channel);
            System.out.println(message);
        }
    }
}
