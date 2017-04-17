package com.translator.system.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by nsity on 20.03.17.
 */

public abstract class ADBWorker {

    protected static final int NULL = 0;
    protected static final int ROLLBACK = 1;
    protected static final int ABORT = 2;
    protected static final int FAIL = 3;
    protected static final int IGNORE = 4;
    protected static final int REPLACE = 5;
    protected static final String LOCK = "dblock";
    private static final String[] CONFLICT_VALUES = new String[]
            {"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    protected Context context;

    public ADBWorker(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    //public abstract int save(JSONArray objects, boolean dropAllData);

    protected int insert(String tableName, int nullColumnHack, ArrayList<ContentValues> values) {
        if ((values == null) || (values.size() == 0))
            return 0;

        /**
         * build sql query and array bind data
         */
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT");
        sql.append(CONFLICT_VALUES[nullColumnHack]);
        sql.append(" INTO ");
        sql.append(tableName);
        sql.append('(');

        Object[][] bindArgs = null;
        ContentValues bindValues = values.get(0);
        int size = (bindValues != null && bindValues.size() > 0)
                ? bindValues.size() : 0;
        if (size > 0) {
            bindArgs = new Object[values.size()][size];
            int i = 0;
            for (String colName : bindValues.keySet()) {
                sql.append((i > 0) ? "," : "");
                sql.append(colName);
                for (int j = 0; j < values.size(); j++) {
                    bindArgs[j][i] = values.get(j).get(colName);
                }
                i++;
            }
            sql.append(')');
            sql.append(" VALUES (");
            for (i = 0; i < size; i++) {
                sql.append((i > 0) ? ",?" : "?");
            }
        } else {
            sql.append(") VALUES (NULL");
        }
        sql.append(')');

        return insertExec(sql.toString(), bindArgs, values.size());
    }

    private int insertExec(String sql, Object[][] bindArgs, int dataSize) {
        synchronized (LOCK) {
            if ((database == null) || (!database.isOpen()))
                database = dbHelper.getWritableDatabase();

            database.beginTransaction();
            SQLiteStatement stmt = database.compileStatement(sql);

            int insertedRecord = 0;
            try {
                if ((bindArgs != null) && (bindArgs.length == dataSize)) {
                    /**
                     * bind args and insert data in DB
                     */
                    for (int i = 0; i < dataSize; i++) {
                        for (int j = 0; j < bindArgs[i].length; j++) {
                            if (bindArgs[i][j] == null) {
                                stmt.bindLong(j + 1, 0);
                            } else if (bindArgs[i][j] instanceof Float || bindArgs[i][j] instanceof Double) {
                                stmt.bindDouble(j + 1, (Double) bindArgs[i][j]);
                            } else if (bindArgs[i][j] instanceof Long || bindArgs[i][j] instanceof Integer
                                    || bindArgs[i][j] instanceof Short || bindArgs[i][j] instanceof Byte) {
                                stmt.bindLong(j + 1, (Integer) bindArgs[i][j]);
                            } else if (bindArgs[i][j] instanceof Boolean) {
                                stmt.bindString(j + 1, (Boolean) bindArgs[i][j] ? "1" : "0");
                            } else {
                                stmt.bindString(j + 1, bindArgs[i][j].toString());
                            }
                        }

                        insertedRecord += (stmt.executeInsert() == -1) ? 0 : 1;
                        stmt.clearBindings();
                    }
                    database.setTransactionSuccessful();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();
            }

            stmt.close();
            database.close();

            return insertedRecord;
        }
    }

    protected void execSQL(String query) {
        synchronized (LOCK) {
            if ((database == null) || (!database.isOpen()))
                database = dbHelper.getWritableDatabase();

            database.beginTransaction();
            try {
                database.execSQL(query);
                database.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();
            }

            database.close();
        }
    }

    protected long insert(String tableName, ContentValues values) {
        synchronized (LOCK) {
            if ((database == null) || (!database.isOpen()))
                database = dbHelper.getWritableDatabase();

            long inserted = 0;
            database.beginTransaction();
            try {
                inserted = database.insert(tableName, null, values);
                database.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();
            }

            database.close();
            return inserted;
        }
    }

    protected int update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        synchronized (LOCK) {
            if ((database == null) || (!database.isOpen()))
                database = dbHelper.getWritableDatabase();

            int insertingReords = 0;
            database.beginTransaction();
            try {
                insertingReords = database.update(tableName, values, whereClause, whereArgs);
                database.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();
            }

            database.close();


            return insertingReords;
        }
    }

    protected void delete(String tableName, String whereClause, String[] args) {
        synchronized (LOCK) {
            if ((database == null) || (!database.isOpen())) {
                database = dbHelper.getWritableDatabase();
            }

            database.beginTransaction();
            try {
                database.delete(tableName, whereClause, args);
                database.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();
            }

            database.close();
        }
    }

    protected Cursor getCursor(String query, String[] args) {
        synchronized (LOCK) {
            if ((database == null) || (!database.isOpen()))
                database = dbHelper.getReadableDatabase();

            Cursor cursor = null;
            database.beginTransaction();
            try {
                cursor = database.rawQuery(query, args);
                database.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                database.endTransaction();
            }
            //database.close();

            return cursor;
        }
    }

    public void closeDB() {
        database.close();
    }

    public static void deleteAllTables(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(LanguageDBInterface.LANGUAGE_TABLE_NAME, null, null);
        db.delete(TranslationDBInterface.TRANSLATION_TABLE_NAME, null, null);
        db.delete(DictionaryDBInterface.DICTIONARY_TABLE_NAME, null, null);
        db.delete(CacheDBInterface.CACHE_TABLE_NAME, null, null);

        db.close();
    }

    @Override
    protected void finalize() {
        try {
            if ((database != null) && (database.isOpen()))
                database.close();
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
