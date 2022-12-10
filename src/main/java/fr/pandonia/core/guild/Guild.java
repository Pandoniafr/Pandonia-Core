package fr.pandonia.core.guild;

import fr.pandonia.api.guild.GuildRank;
import fr.pandonia.api.guild.IGuild;
import fr.pandonia.api.guild.IGuildInvitation;
import fr.pandonia.api.guild.IGuildMember;
import fr.pandonia.core.player.SimplePlayer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Guild implements IGuild {

    private UUID uuid;
    private String name;
    private String tag;
    private List<IGuildMember> members;
    private int saphirs;
    private List<IGuildInvitation> invitations;

    public Guild() {
    }

    public Guild(UUID uuid, String name, String tag, List<IGuildMember> members, int saphirs, List<IGuildInvitation> invitations) {
        this.uuid = uuid;
        this.name = name;
        this.tag = tag;
        this.members = members;
        this.saphirs = saphirs;
        this.invitations = invitations;
    }

    public Guild(String name, String tag, SimplePlayer owner){
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.tag = tag;
        this.members = new ArrayList<IGuildMember>(){{
            add(new GuildMember(owner, GuildRank.CHEF, 0, 0, new Date(System.currentTimeMillis())));
        }};
        this.saphirs = 0;
        this.invitations = new ArrayList<>();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public IGuildMember getOwner(){
        for (IGuildMember member : members) {
            if (member.getGuildRank().equals(GuildRank.CHEF)){
                return member;
            }
        }
        return null;
    }

    @Override
    public IGuildMember getMember(UUID playerUUID){
        for (IGuildMember gm : members){
            if (gm.getUUID().equals(playerUUID)){
                return gm;
            }
        }
        return null;
    }

    @Override
    public IGuildMember getMember(String name){
        for (IGuildMember gm : members){
            if (gm.getName().equals(name)){
                return gm;
            }
        }
        return null;
    }

    @Override
    public List<IGuildMember> getMembers() {
        return members;
    }

    @Override
    public List<IGuildMember> getOnlineMembers(List<UUID> onlineMembers) {
        return members.stream().filter(gm -> onlineMembers.contains(gm.getUUID())).collect(Collectors.toList());
    }

    @Override
    public List<IGuildMember> getMembersOfRank(GuildRank rank){
        List<IGuildMember> players = new ArrayList<>();
        for (IGuildMember member : members) {
            if (member.getGuildRank().equals(rank)){
                players.add(member);
            }
        }
        return players;
    }

    public boolean containsMemberInList(List<UUID> list){
        return members.stream().anyMatch(gm -> list.contains(gm.getUUID()));
    }

    @Override
    public int getSaphirs() {
        return saphirs;
    }

    @Override
    public List<IGuildInvitation> getInvitations() {
        return invitations;
    }

    @Override
    public IGuildInvitation getInvitation(UUID playerUUID){
        for (IGuildInvitation invit : invitations){
            if (invit.getReceiver().getUUID().equals(playerUUID)){
                return invit;
            }
        }
        return null;
    }

    @Override
    public void addInvitation(IGuildInvitation invit){
        invitations.add(invit);
    }

    @Override
    public IGuildInvitation getInvitation(String name){
        for (IGuildInvitation invit : invitations){
            if (invit.getReceiver().getName().equals(name)){
                return invit;
            }
        }
        return null;
    }

    @Override
    public void removeInvitation(UUID playerUUID){
        invitations.removeIf(gi -> gi.getReceiver().getUUID().equals(playerUUID));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Guild guild = (Guild) o;

        return uuid != null ? uuid.equals(guild.uuid) : guild.uuid == null;
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public Document toDocument(){
        List<Document> invitations = new ArrayList<>();
        for (IGuildInvitation invit : this.invitations){
            invitations.add(invit.toDocument());
        }
        return toMongoDocument().append("invitations", invitations);
    }

    @Override
    public Document toMongoDocument(){
        Document document = new Document("uuid", uuid.toString()).append("name", name).append("tag", tag);
        List<Document> members = new ArrayList<>();
        for (IGuildMember member : this.members){
            members.add(member.toDocument());
        }
        document.append("members", members);
        document.append("saphirs", saphirs);
        return document;
    }

    public void update(Document document){
        this.uuid = UUID.fromString(document.getString("uuid"));
        this.name = document.getString("name");
        this.tag = document.getString("tag");
        List<IGuildMember> members = new ArrayList<>();
        for (Document member : document.getList("members", Document.class)){
            members.add(GuildMember.fromDocument(member));
        }
        this.members = members;
        this.saphirs = document.getInteger("saphirs");
        this.invitations = new ArrayList<>();
        if (document.containsKey("invitations")){
            document.getList("invitations", Document.class).forEach(invitDoc -> invitations.add(GuildInvitation.fromDocument(invitDoc)));
        }
    }

    public static Guild fromDocument(Document document){
        Guild guild = new Guild();
        guild.update(document);
        return guild;
    }
}
