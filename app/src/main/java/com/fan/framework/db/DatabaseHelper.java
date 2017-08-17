package com.fan.framework.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fan.framework.base.FFApplication;
import com.fan.framework.utils.FFLogUtil;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Locale;

/**
 * 创建数据库 ---继承ORMLite框架
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TABLE_NAME = "publish.db"; // 表名
    private final static int DATABASEVERSION = 1; // 数据库的版本
    private static DatabaseHelper instance = null; // 数据库的实例

    public static final Class<?>[] tables = new Class<?>[]{DraftModel.class, PublishModel.class};

    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(FFApplication.app);
            }
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, DATABASEVERSION);
    }

    /**
     * 如果数据有更新的话，就需要修改数据库的版本--与上面同步
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVer, int newVer) {
        // if (oldVer < 11) {
        for (Class<?> class1 : tables) {
            String dropString = "drop table if exists '" + class1.getSimpleName().toLowerCase(Locale.CHINA) + "'";
            db.execSQL(dropString);
            // 生成新表
            try {
                TableUtils.createTable(connectionSource, class1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return;
        // }
        // FFLogUtil.i("tag", " onUpgrade db");
        // for (Class<?> class1 : tables) {
        // updateTable(db, (Class<? extends BaseTable>) class1,
        // connectionSource, oldVer);
        // }
    }

    private void updateTable(SQLiteDatabase db, Class<? extends BaseTable> tableClass, ConnectionSource conn, int oldVersion) {
        BaseTable tableInstance;
        try {
            tableInstance = tableClass.newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
            return;
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
            return;
        }
        if (!tableInstance.needUpdate(oldVersion)) {
            return;
        }
        try {
            db.beginTransaction();
            if (tableInstance.keepOldData(oldVersion)) {
                String tempTable = tableClass.getSimpleName().toLowerCase(Locale.CHINA) + "temptable";
                String sql = "alter table '" + tableClass.getSimpleName().toLowerCase(Locale.CHINA) + "' rename to '" + tempTable + "'";
                db.execSQL(sql);

                // 删除旧表
                String dropString = "drop table if exists '" + tableClass.getSimpleName().toLowerCase(Locale.CHINA) + "'";
                db.execSQL(dropString);

                // 生成新表
                TableUtils.createTable(conn, tableClass);

                String ins = "insert into '" + tableClass.getSimpleName().toLowerCase(Locale.CHINA) + "' (" + tableInstance.getColumn(oldVersion)
                        + ") " + "select " + tableInstance.getOldColumn(oldVersion) + " from " + tempTable;
                db.execSQL(ins);

                // 删除缓存表
                String dropTemp = "drop table if exists '" + tempTable + "'";
                db.execSQL(dropTemp);
            } else {
                // 删除旧表
                String dropString = "drop table if exists '" + tableClass.getSimpleName().toLowerCase(Locale.CHINA) + "'";
                db.execSQL(dropString);

                // 生成新表
                TableUtils.createTable(conn, tableClass);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            FFLogUtil.i("tag", e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        try {
            FFLogUtil.i(DatabaseHelper.class.getName(), "onCreate");
            for (Class<?> class1 : tables) {
                TableUtils.createTable(arg1, class1);
            }
        } catch (SQLException e) {
            FFLogUtil.e(DatabaseHelper.class.getName(), "Can't create database" + e);
            throw new RuntimeException(e);
        }
    }

}
