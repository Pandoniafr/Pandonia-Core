package fr.pandonia.core;

import fr.pandonia.api.PandoniaAPI;
import fr.pandonia.api.battlepass.manager.IBPItemManager;
import fr.pandonia.api.commands.ICommandsManager;
import fr.pandonia.api.config.PandoniaConfiguration;
import fr.pandonia.api.cosmetics.manager.ICosmeticsManager;
import fr.pandonia.api.data.mongo.IMongoConnection;
import fr.pandonia.api.data.redis.IRedisConnection;
import fr.pandonia.api.data.redis.pubsub.IPSReader;
import fr.pandonia.api.data.redis.pubsub.IPSWriter;
import fr.pandonia.api.games.manager.GameHistoryManager;
import fr.pandonia.api.guild.manager.IGuildManager;
import fr.pandonia.api.npc.manager.INPCManager;
import fr.pandonia.api.player.battlepass.manager.IPlayerBattlePassManager;
import fr.pandonia.api.player.friends.manager.IFriendsManager;
import fr.pandonia.api.player.manager.IPlayerManager;
import fr.pandonia.api.player.owning.manager.IPlayerOwningManager;
import fr.pandonia.api.player.settings.manager.IPlayerSettingsManager;
import fr.pandonia.api.player.stats.manager.IPlayerStatsManager;
import fr.pandonia.api.proxy.ProxyLink;
import fr.pandonia.api.pubsub.manager.IMessageReceiverManager;
import fr.pandonia.api.rank.manager.IRankManager;
import fr.pandonia.api.sanction.infos.loader.ISanctionsInfosLoader;
import fr.pandonia.api.server.IInstanceInfo;
import fr.pandonia.api.server.ServerStatus;
import fr.pandonia.api.server.manager.IServerManager;
import fr.pandonia.api.tablist.manager.ITeamTagManager;
import fr.pandonia.core.battlepass.manager.BPItemManager;
import fr.pandonia.core.commands.manager.CommandsManager;
import fr.pandonia.core.config.PandoniaCredentials;
import fr.pandonia.core.cosmetics.manager.CosmeticsManager;
import fr.pandonia.core.data.mongo.MongoConnection;
import fr.pandonia.core.data.mongo.MongoCredentials;
import fr.pandonia.core.data.redis.RedisConnection;
import fr.pandonia.core.data.redis.RedisCredentials;
import fr.pandonia.core.data.redis.pubsub.PSReader;
import fr.pandonia.core.data.redis.pubsub.PSWriter;
import fr.pandonia.core.guild.manager.GuildManager;
import fr.pandonia.core.images.manager.ImageMapManager;
import fr.pandonia.core.listeners.manager.ListenersManager;
import fr.pandonia.core.listeners.pluginmessage.PMManager;
import fr.pandonia.core.npc.manager.NPCManager;
import fr.pandonia.core.player.battlepass.manager.PlayerBattlePassManager;
import fr.pandonia.core.player.friends.manager.FriendsManager;
import fr.pandonia.core.player.manager.PlayerManager;
import fr.pandonia.core.player.nick.manager.NickManager;
import fr.pandonia.core.player.owning.manager.PlayerOwningManager;
import fr.pandonia.core.player.settings.manager.PlayerSettingsManager;
import fr.pandonia.core.player.stats.manager.PlayerStatsManager;
import fr.pandonia.core.pubsub.manager.MessageReceiverManager;
import fr.pandonia.core.rank.manager.RankManager;
import fr.pandonia.core.sanction.infos.loader.SanctionsInfosLoader;
import fr.pandonia.core.scoreboard.manager.ScoreboardManager;
import fr.pandonia.core.server.InstanceInfo;
import fr.pandonia.core.server.ServerDataManager;
import fr.pandonia.core.server.manager.ServerManager;
import fr.pandonia.core.tablist.manager.TeamTagManager;
import fr.pandonia.tools.MojangAPIUtil;
import fr.pandonia.tools.npc.NPCLib;
import org.bson.Document;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PandoniaCore extends PandoniaAPI {

    public PandoniaCore(JavaPlugin plugin) {
        super(plugin);
    }

    private PandoniaConfiguration configuration;

    //Bases de données
    private IRedisConnection redisConnection;
    private IMongoConnection mongoConnection;

    //Initialisation prioritaire
    private IRankManager rankManager;
    private ISanctionsInfosLoader sanctionsInfosLoader;

    //Initialisation normale
    private IPlayerManager playerManager;
    private IPlayerSettingsManager playerSettingsManager;
    private IPlayerOwningManager playerOwningManager;
    private IPlayerStatsManager playerStatsManager;
    private ICosmeticsManager cosmeticsManager;
    private IBPItemManager battlePassItemsManager;
    private IPlayerBattlePassManager playerBattlePassManager;
    private IFriendsManager friendsManager;
    private ImageMapManager imageMapManager;
    private ServerDataManager serverDataManager;
    private IServerManager serverManager;
    public ScoreboardManager scoreboardManager;
    private ITeamTagManager teamTagManager;
    private ICommandsManager commandsManager;
    private GameHistoryManager gameHistoryManager;
    private IGuildManager guildManager;
    private NickManager nickManager;
    private INPCManager npcManager;

    //Initialisation secondaire
    private IMessageReceiverManager messageReceiverManager;
    private IPSWriter psWriter;
    private IPSReader psReader;
    private NPCLib npcLib;

    private ProxyLink proxyLink;
    private IInstanceInfo instanceInfo;

    @Override
    public void onLoad() {}

    @Override
    public void onEnable() {

        //Databases
        configuration = new PandoniaConfiguration(new File("/home/pandonia/config.json"));
        Document config = configuration.getParsedFileContent();
        Document mongoConfig = config.get("mongo", Document.class);
        Document redisConfig = config.get("redis", Document.class);
        PandoniaCredentials credentials = new PandoniaCredentials(mongoConfig.getString("host"), mongoConfig.getInteger("port"), mongoConfig.getString("username"), mongoConfig.getString("password"), mongoConfig.getString("database"), redisConfig.getString("host"), redisConfig.getInteger("port"), redisConfig.getString("username"), redisConfig.getString("password"));
        redisConnection = new RedisConnection(new RedisCredentials(credentials.getRedisHost(), credentials.getRedisPort(), credentials.getRedisPassword(), credentials.getRedisUsername()), 0);
        redisConnection.init();
        mongoConnection = new MongoConnection(new MongoCredentials(credentials.getMongoHost(), credentials.getMongoPort(), credentials.getMongoPassword(), credentials.getMongoUsername(), credentials.getMongoDatabase()));
        mongoConnection.init();

        //Managers
        rankManager = new RankManager(this);
        rankManager.register();
        sanctionsInfosLoader = new SanctionsInfosLoader(this);
        sanctionsInfosLoader.load();

        playerManager = new PlayerManager(this);
        playerSettingsManager = new PlayerSettingsManager(this);
        playerOwningManager = new PlayerOwningManager(this);
        playerStatsManager = new PlayerStatsManager(this);
        battlePassItemsManager = new BPItemManager(this);
        playerBattlePassManager = new PlayerBattlePassManager(this);
        cosmeticsManager = new CosmeticsManager(this);
        friendsManager = new FriendsManager(this);
        imageMapManager = new ImageMapManager(this);
        serverDataManager = new ServerDataManager(this);
        serverManager = new ServerManager(this);
        scoreboardManager = new ScoreboardManager(this);
        teamTagManager = new TeamTagManager();
        gameHistoryManager = new GameHistoryManager(this);
        guildManager = new GuildManager(this);
        guildManager.startTask();
        nickManager = new NickManager(this);
        npcManager = new NPCManager(this);
        MojangAPIUtil.setPlugin(getPlugin());
        messageReceiverManager = new MessageReceiverManager(this);
        ((MessageReceiverManager) messageReceiverManager).register();
        psWriter = new PSWriter(redisConnection);
        psReader = new PSReader(redisConnection, (MessageReceiverManager) messageReceiverManager, "proxy");
        psReader.register();
        new PMManager(this);
        new ListenersManager(this).register();
        commandsManager  = new CommandsManager(this);
        commandsManager.register();

        proxyLink = new ProxyLink(this);
        instanceInfo = new InstanceInfo(this);

        //Registering
        ((fr.pandonia.core.pubsub.manager.MessageReceiverManager) messageReceiverManager).register();
        cosmeticsManager.registerCosmetics();

        PandoniaAPI.get().getProxyLink().setServerStatus(ServerStatus.STARTED);
        if (getInstanceInfo().getHost() != null){
            PandoniaAPI.get().getProxyLink().changeServer(getInstanceInfo().getHost().getUUID(), getInstanceInfo().getName());
        }

    }

    @Override
    public void onDisable() {
        scoreboardManager.onDisable();
    }

    @Deprecated
    @Override
    public void enableNPCs(){
        npcLib = new NPCLib(getPlugin());
    }

    public static PandoniaCore getCore(){
        return (PandoniaCore) get();
    }

    @Override
    public PandoniaConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public IRedisConnection getRedisConnection() {
        return redisConnection;
    }

    @Override
    public IMongoConnection getMongoConnection() {
        return mongoConnection;
    }

    @Override
    public IRankManager getRankManager() {
        return rankManager;
    }

    @Override
    public ISanctionsInfosLoader getSanctionsInfosLoader() {
        return sanctionsInfosLoader;
    }

    @Override
    public IPlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public IPlayerSettingsManager getPlayerSettingsManager() {
        return playerSettingsManager;
    }

    @Override
    public IPlayerOwningManager getPlayerOwningManager() {
        return playerOwningManager;
    }

    @Override
    public IPlayerStatsManager getPlayerStatsManager() {
        return playerStatsManager;
    }

    @Override
    public IBPItemManager getBattlePassItemsManager() {
        return battlePassItemsManager;
    }

    @Override
    public IPlayerBattlePassManager getPlayerBattlePassManager() {
        return playerBattlePassManager;
    }

    @Override
    public ICosmeticsManager getCosmeticsManager() {
        return cosmeticsManager;
    }

    @Override
    public IFriendsManager getFriendsManager() {
        return friendsManager;
    }

    @Override
    public ImageMapManager getImageMapManager() {
        return imageMapManager;
    }

    public ServerDataManager getServerDataManager() {
        return serverDataManager;
    }

    @Override
    public IServerManager getServerManager() {
        return serverManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    @Override
    public ITeamTagManager getTeamTagManager() {
        return teamTagManager;
    }

    @Override
    public GameHistoryManager getGameHistoryManager() {
        return gameHistoryManager;
    }

    @Override
    public IMessageReceiverManager getMessageReceiverManager() {
        return messageReceiverManager;
    }

    @Override
    public NPCLib getNPCLib() {
        return npcLib;
    }

    public void setNPCLib(NPCLib npcLib) {
        this.npcLib = npcLib;
    }

    @Override
    public IPSWriter getPSWriter() {
        return psWriter;
    }

    @Override
    public IPSReader getPSReader() {
        return psReader;
    }

    @Override
    public ProxyLink getProxyLink() {
        return proxyLink;
    }

    @Override
    public IInstanceInfo getInstanceInfo() {
        return instanceInfo;
    }

    @Override
    public ICommandsManager getCommandsManager() {
        return commandsManager;
    }

    @Override
    public IGuildManager getGuildManager() {
        return guildManager;
    }

    @Override
    public INPCManager getNPCManager() {
        return npcManager;
    }

    public NickManager getNickManager() {
        return nickManager;
    }

}
