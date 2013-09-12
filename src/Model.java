package com.project.app.model.interfaces;

import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;

import java.util.List;

public abstract interface Model{
    // The name of the Id Field
    public abstract String getIdField();

    // The content of the Id Field
    public abstract long getIdValue();

    // Actions against Data Structure
    public abstract boolean save(Model model); // Add or Update.
    public abstract boolean delete(long id);
    public abstract Model read(long paramLong);
    public abstract List<Model> find(String paramString);

    // Parsers
    public abstract List<Model> cursor2ListOptions(Cursor paramCursor);
    public abstract void parseCursor(Cursor paramCursor);
    public abstract ContentValues parse2ContentValues();

    // Populate
    public abstract View populateItem(View view);
    public abstract View populateListItem(View view);

    //public abstract String getSelectSQL(long paramLong);
}
