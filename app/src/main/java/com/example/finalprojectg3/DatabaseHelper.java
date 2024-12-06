package com.example.finalprojectg3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import model.Group;
import model.GroupMember;

import java.util.ArrayList;

import model.Group;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExpenseManager.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Groups (group_id INTEGER PRIMARY KEY AUTOINCREMENT, group_name TEXT NOT NULL)");
        db.execSQL("CREATE TABLE GroupMembers (member_id INTEGER PRIMARY KEY AUTOINCREMENT, group_id INTEGER NOT NULL, member_name TEXT NOT NULL, FOREIGN KEY(group_id) REFERENCES Groups(group_id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Groups");
        db.execSQL("DROP TABLE IF EXISTS GroupMembers");
        onCreate(db);
    }

    // Insert new group
    public long addGroup(String groupName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("group_name", groupName);
        return db.insert("Groups", null, values);
    }

    // Add members to a group
    public long addGroupMember(int groupId, String memberName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("group_id", groupId);
        values.put("member_name", memberName);
        return db.insert("GroupMembers", null, values);
    }

    // Get all groups
    public ArrayList<Group> getGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Groups", null);

        if (cursor.moveToFirst()) {
            do {
                int groupId = cursor.getInt(0);
                String groupName = cursor.getString(1);
                groups.add(new Group(groupId, groupName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return groups;
    }

    // Get members of a specific group
    public ArrayList<GroupMember> getGroupMembers(int groupId) {
        ArrayList<GroupMember> members = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GroupMembers WHERE group_id = ?", new String[]{String.valueOf(groupId)});

        if (cursor.moveToFirst()) {
            do {
                int memberId = cursor.getInt(0);
                String memberName = cursor.getString(2);
                members.add(new GroupMember(memberId, groupId, memberName));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return members;
    }

    // Update group member name
    public long updateGroupMemberName(int memberId, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("member_name", newName);
        return db.update("GroupMembers", values, "member_id = ?", new String[]{String.valueOf(memberId)});
    }

    // Delete a group member
    public long deleteGroupMember(int memberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("GroupMembers", "member_id = ?", new String[]{String.valueOf(memberId)});
    }

    // Delete a group
    public long deleteGroup(int groupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Groups", "group_id = ?", new String[]{String.valueOf(groupId)});
    }
}
