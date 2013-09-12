package com.project.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.View;

import com.project.app.model.interfaces.Model;

import java.util.List;

public class User extends SQLiteModel {

    public User(Context context){
        super(context);
    }

    @Override
    public void parseCursor(Cursor paramCursor) {

    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public ContentValues parse2ContentValues() {
        return null;
    }

    @Override
    public View populateItem(View view) {
        return null;
    }

    @Override
    public View populateListItem(View view) {
        return null;
    }

    @Override
    public long getIdValue() {
        return 0;
    }

    @Override
    public List<Model> cursor2ListOptions(Cursor paramCursor) {
        return null;
    }
}
