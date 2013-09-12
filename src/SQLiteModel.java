package com.project.app.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.TextView;

import com.project.app.Configuration;
import com.project.app.db.Database;
import com.project.app.model.interfaces.Model;

import java.util.List;

public abstract class SQLiteModel implements Model {

    public String FIELD_ID = "_id";
    public String TAG = Configuration.LOG_TAG;
    Context context;
    long id;
    long _id;
    Database mDbHelper;

    //public SQLiteModel(){}

    public SQLiteModel(Context paramContext){ this.context = paramContext; }

    public void setContext(Context paramContext){
        this.context = paramContext;
    }

    public void clearTable(){
        this.mDbHelper = new Database(this.context);
        try{
            this.mDbHelper.getWritableDatabase().execSQL("DELETE FROM " + getTableName());
        } finally {
            this.mDbHelper.close();
        }
    }


    public List<Model> find(String paramString){
        this.mDbHelper = new Database(this.context);
        List<Model> localList = null;
        try{
            Cursor localCursor;
            localCursor = this.mDbHelper.getReadableDatabase().rawQuery(paramString, null);
            localList = cursor2ListOptions(localCursor);
            localCursor.close();
        } finally {
            this.mDbHelper.close();
        }
        return localList;
    }

    public List<Model> findAll(){
        return find("SELECT * FROM " + getTableName());
    }

    public boolean delete(long id){
        this.mDbHelper = new Database(this.context);
        String deleteQuery = getTableName() + "." + getIdField() + " = ?";
        String[] values = {Long.toString(id)};
        try{
            this.mDbHelper.getWritableDatabase().delete(getTableName(), deleteQuery, values);
        } finally {
            this.mDbHelper.close();
        }
        return true;
    }

    public long getId(){
        return this._id;
    }

    public String getIdField(){
        return this.FIELD_ID;
    }

    public String getSelectSQL(long paramLong){
        return null;
    }

    public String getSqlWithCondition(String field, long value){
        String str = " SELECT *  FROM " + getTableName() + " WHERE " + getTableName() + "." + field + "=" + value;
        return str;
    }

    public String getSqlWithCondition(String field, String value){
        String str = " SELECT *  FROM " + getTableName() + " WHERE " + getTableName() + "." + field + "='" + value + "'";
        return str;
    }

    public String getSqlWithCondition(String field, long value, String field2, long value2){
        String str = " SELECT *  FROM " + getTableName()
                + " WHERE " + getTableName() + "." + field + "=" + value
                + " AND " + getTableName() + "." + field2 + "=" + value2;
        return str;
    }

    public long parseToLong(String paramString){
        long l2 = 0;
        try{
            l2 = Long.parseLong(paramString);
        } catch (Exception ignored){

        }
        return l2;
    }

    public Model read(long paramLong){
        this.mDbHelper = new Database(this.context);
        try{
            Cursor localCursor = this.mDbHelper.getReadableDatabase().rawQuery(getSqlWithCondition(getIdField(), paramLong), null);
            localCursor.moveToFirst();
            parseCursor(localCursor);
            localCursor.close();
            return this;
        }catch(Exception e){
            this._id = -1;
            //Configuration.log(TAG, "SQLiteModel : " + e.toString());
        } finally {
            this.mDbHelper.close();
        }
        return this;
    }

    public Model readFirst(String sql){
        this.mDbHelper = new Database(this.context);
        try{
            Cursor localCursor = this.mDbHelper.getReadableDatabase().rawQuery(sql, null);
            localCursor.moveToFirst();
            parseCursor(localCursor);
            localCursor.close();
            return this;
        }catch(Exception e){
            this._id = -1;
            Configuration.log(TAG, "SQLiteModel : " + e.toString());
        } finally {
            this.mDbHelper.close();
        }
        return this;
    }

    public int updateQuery(String table, ContentValues values, String whereClause, String[] whereArgs){
        int rows = 0;
        this.mDbHelper = new Database(this.context);
        try{
            rows = this.mDbHelper.getWritableDatabase().update(table, values, whereClause, whereArgs);
        } finally {
            this.mDbHelper.close();
        }
        return rows;
    }

    public int deleteQuery(String table, String whereClause, String[] whereArgs){
        int rows = 0;
        this.mDbHelper = new Database(this.context);
        try{
            rows = this.mDbHelper.getWritableDatabase().delete(table, whereClause, whereArgs);
        } finally {
            this.mDbHelper.close();
        }
        return rows;
    }

    public boolean save(Model model){
        boolean success = false;
        this.mDbHelper = new Database(this.context);
        try{
            SQLiteDatabase localSQLiteDatabase = this.mDbHelper.getWritableDatabase();
            ContentValues localContentValues = model.parse2ContentValues();
            // ADD
            try{
                this.id = localSQLiteDatabase.insertOrThrow(getTableName(), null, localContentValues);
            }catch(SQLiteConstraintException e){
                this.id = -1;
            }
            // UPDATE
            if(this.id == -1){
                if( (localSQLiteDatabase.update(getTableName(), localContentValues, model.getIdField() + " = " + model.getIdValue(), null) == 1 ))
                    this.id = model.getIdValue();
            } else {
                this.id = model.getIdValue();
            }
            if (this.id == -1)
                success = false;
            else
                success = true;
        }catch (Exception e){
            Configuration.log(TAG, e.toString());
            Configuration.log(TAG, e.getMessage());
            this.mDbHelper.close();
        }finally{
            this.mDbHelper.close();
        }
        if(success == true)
            this.read(this.id);
        return success;
    }

    public void setTextView(View paramView, int paramInt, String paramString){
        TextView localTextView = (TextView)paramView.findViewById(paramInt);
        if (localTextView != null)
            localTextView.setText(paramString);
    }

    public abstract void parseCursor(Cursor paramCursor);
    public abstract String getTableName();
    public abstract ContentValues parse2ContentValues();
    public abstract View populateItem(View view);
    public abstract View populateListItem(View view);
    public abstract List<Model> cursor2ListOptions(Cursor paramCursor);

}
