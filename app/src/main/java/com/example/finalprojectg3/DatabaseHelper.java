package com.example.finalprojectg3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import model.Group;
import model.GroupExpense;
import model.GroupMember;

import java.util.ArrayList;
import java.util.List;

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
        db.execSQL("CREATE TABLE GroupExpenses (expense_id INTEGER PRIMARY KEY AUTOINCREMENT, group_id INTEGER NOT NULL, expense_name TEXT NOT NULL, total_amount REAL NOT NULL, split_amount REAL NOT NULL, FOREIGN KEY(group_id) REFERENCES Groups(group_id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Groups");
        db.execSQL("DROP TABLE IF EXISTS GroupMembers");
        db.execSQL("DROP TABLE IF EXISTS GroupExpenses");
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

    // Add group expense
    public boolean addGroupExpense(String groupName, String expenseName, String totalAmount) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Retrieve group ID from group name
        String query = "SELECT group_id FROM Groups WHERE group_name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{groupName});

        if (cursor.moveToFirst()) {
            int groupId = cursor.getInt(0);
            ContentValues values = new ContentValues();
            values.put("group_id", groupId);
            values.put("expense_name", expenseName);
            values.put("total_amount", Double.parseDouble(totalAmount));
            values.put("split_amount", Double.parseDouble(totalAmount)); // Assuming splitAmount is same as totalAmount for now

            long result = db.insert("GroupExpenses", null, values);
            db.close();
            return result != -1;
        }

        cursor.close();
        db.close();
        return false; // Return false if group not found
    }

    // Get expenses for a specific group
    // Get expenses for a specific group
    public List<GroupExpense> getExpensesByGroupId(int groupId) {
        List<GroupExpense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM GroupExpenses WHERE group_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(groupId)});

        if (cursor.moveToFirst()) {
            // Ensure the columns are valid before accessing them
            int expenseIdCol = cursor.getColumnIndex("expense_id");
            int expenseNameCol = cursor.getColumnIndex("expense_name");
            int totalAmountCol = cursor.getColumnIndex("total_amount");
            int splitAmountCol = cursor.getColumnIndex("split_amount");

            // Check if the column indices are valid (not -1)
            if (expenseIdCol >= 0 && expenseNameCol >= 0 && totalAmountCol >= 0 && splitAmountCol >= 0) {
                do {
                    int expenseId = cursor.getInt(expenseIdCol);
                    String expenseName = cursor.getString(expenseNameCol);
                    double totalAmount = cursor.getDouble(totalAmountCol);
                    double splitAmount = cursor.getDouble(splitAmountCol);

                    expenses.add(new GroupExpense(expenseId, groupId, expenseName, totalAmount, splitAmount));
                } while (cursor.moveToNext());
            } else {
                // Handle error: columns are not found
                Log.e("DatabaseHelper", "Columns not found in the query result");
            }
        }
        cursor.close();
        db.close();
        return expenses;
    }


    public List<String> getGroupNames() {
        List<String> groupNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Groups", null);

        if (cursor.moveToFirst()) {
            // Safely check if the column exists
            int groupNameIndex = cursor.getColumnIndex("group_name");

            if (groupNameIndex >= 0) {
                do {
                    String groupName = cursor.getString(groupNameIndex);  // Access the group_name column safely
                    groupNames.add(groupName);
                } while (cursor.moveToNext());
            } else {
                // Handle error if the column is missing
                Log.e("DatabaseHelper", "Column 'group_name' not found in the result set");
            }
        }
        cursor.close();
        db.close();
        return groupNames;
    }

    public double getTotalExpenseForGroup(int groupId) {
        double totalExpense = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT SUM(total_amount) FROM GroupExpenses WHERE group_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(groupId)});

        if (cursor.moveToFirst()) {
            totalExpense = cursor.getDouble(0);
        } else {
            Log.e("DatabaseHelper", "No expenses found for group ID: " + groupId);
        }

        cursor.close();
        db.close();
        return totalExpense;
    }

    public List<String> getMembersWithSplit(int groupId) {
        List<String> membersWithSplit = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT gm.member_name, ge.total_amount / " +
                "(SELECT COUNT(*) FROM GroupMembers WHERE group_id = ?) AS split_amount " +
                "FROM GroupMembers gm " +
                "JOIN GroupExpenses ge ON gm.group_id = ge.group_id " +
                "WHERE gm.group_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(groupId), String.valueOf(groupId)});

        if (cursor.moveToFirst()) {
            // Safely check if the required columns exist
            int memberNameIndex = cursor.getColumnIndex("member_name");
            int splitAmountIndex = cursor.getColumnIndex("split_amount");

            if (memberNameIndex == -1 || splitAmountIndex == -1) {
                Log.e("DatabaseHelper", "Required columns 'member_name' or 'split_amount' not found in the query result.");
                cursor.close();
                db.close();
                return membersWithSplit; // Return an empty list if columns are missing
            }

            do {
                String memberName = cursor.getString(memberNameIndex);
                double splitAmount = cursor.getDouble(splitAmountIndex);

                membersWithSplit.add(memberName + ": $" + String.format("%.2f", splitAmount));
            } while (cursor.moveToNext());
        } else {
            Log.e("DatabaseHelper", "No data returned for groupId: " + groupId);
        }

        cursor.close();
        db.close();
        return membersWithSplit;
    }


    public List<ABC> getGroup() {
        List<ABC> groups = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT group_id, group_name FROM Groups";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int groupId = cursor.getInt(0);
                String groupName = cursor.getString(1);
                groups.add(new ABC(groupId, groupName));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return groups;
    }

    // Group class inside DatabaseHelper
    public static class ABC {
        private final int groupId;
        private final String groupName;

        public ABC(int groupId, String groupName) {
            this.groupId = groupId;
            this.groupName = groupName;
        }

        public int getGroupId() {
            return groupId;
        }

        public String getGroupName() {
            return groupName;
        }
    }




}
