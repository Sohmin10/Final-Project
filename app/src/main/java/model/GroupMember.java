package model;

public class GroupMember {
    private int id;
    private int groupId;
    private String name;

    public GroupMember(int id, int groupId, String name) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
