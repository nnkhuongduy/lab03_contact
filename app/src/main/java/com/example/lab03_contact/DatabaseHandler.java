package com.example.lab03_contact;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }


    // Getting single contact
    @SuppressLint("Range")
    public Contact getContact(int id) {
        // array of columns to fetch
        String[] columns = {
                KEY_ID,
                KEY_NAME,
                KEY_PH_NO,
        };
        Contact contact = new Contact("", "");

        // selection criteria
        String selection = KEY_ID + " = ?";

        // selection argument
        String[] selectionArgs = {String.valueOf(id)};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order

        if (cursor.moveToFirst()) {
            contact.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            contact.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(KEY_PH_NO)));
        }
        cursor.close();
        db.close();

        return contact;
    }

    // Getting All Contacts
    @SuppressLint("Range")
    public ArrayList<Contact> getAllContacts() {
        // array of columns to fetch
        String[] columns = {
                KEY_ID,
                KEY_NAME,
                KEY_PH_NO,
        };
        // sorting orders
        String sortOrder =
                KEY_NAME + " ASC";
        ArrayList<Contact> list = new ArrayList<Contact>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact("", "");
                contact.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(KEY_PH_NO)));
                // Adding record to list
                list.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return list
        return list;
    }

    // Updating single contact
    public void updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, contact.getId());
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());

        // updating row
        db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    // Deleting single contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete record by id
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }
}