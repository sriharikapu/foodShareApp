package com.fan.framework.base;

import com.fan.framework.db.DatabaseHelper;
import com.fan.framework.utils.FFUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * dao接口
 */
@SuppressWarnings("unchecked")
public class FFDao {
    /**
     * 保存一个对象
     *
     * @param object
     * @return 保存结果
     */
    public static <T> boolean save(T object) {
        try {
            Dao<T, ?> dao = (Dao<T, ?>) DatabaseHelper.getInstance().getDao(object.getClass());
            dao.create(object);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 开始事务
     *
     * @return 保存结果
     */
    public static void beginTransaction() {
        DatabaseHelper.getInstance().getWritableDatabase().beginTransaction();
    }

    /**
     * 事务
     *
     * @return 保存结果
     */
    public static void setTransactionSuccessful() {
        DatabaseHelper.getInstance().getWritableDatabase().setTransactionSuccessful();
    }

    /**
     * 事务
     *
     * @return 保存结果
     */
    public static void endTransaction() {
        DatabaseHelper.getInstance().getWritableDatabase().endTransaction();
    }

    /**
     * 创建或更新一个对象
     * @param object
     * @return
     */
    public static <T> boolean saveOrUpdate(T object) {
        try {
            Dao<T, ?> dao = (Dao<T, ?>) DatabaseHelper.getInstance().getDao(object.getClass());
            CreateOrUpdateStatus a = dao.createOrUpdate(object);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查找所有对象
     *
     * @param clazz
     * @return
     */
    public static <T> List<T> findAll(Class<T> clazz) {
        try {
            Dao<T, ?> dao = (Dao<T, ?>) DatabaseHelper.getInstance().getDao(clazz);
            return dao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    /**
     * 查找所有对象
     *
     * @param clazz
     * @return
     */
    public static <T> T findById(Class<T> clazz, String idName, Object id) {
        try {
            Dao<T, ?> dao = (Dao<T, ?>) DatabaseHelper.getInstance().getDao(clazz);
            return dao.queryBuilder().where().eq(idName, id).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查找所有对象
     *
     * @param clazz
     * @return
     */
    public static <T> int countOf(Class<T> clazz, String idName, Object id) {
        try {
            Dao<T, ?> dao = (Dao<T, ?>) DatabaseHelper.getInstance().getDao(clazz);
            return (int)dao.queryBuilder().where().eq(idName, id).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删除所有对象
     *
     * @param clazz
     * @return
     */
    public static <T> boolean deleteAll(Class<T> clazz, String idName, Object id) {
        try {
            Dao<T, ?> dao = (Dao<T, ?>) DatabaseHelper.getInstance().getDao(clazz);
            DeleteBuilder<T, ?> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(idName, id);
            deleteBuilder.delete();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查找所有对象
     *
     * @param clazz
     * @return
     */
    public static <T> List<T> findAllById(Class<T> clazz, String idName, Object id) {
        try {
            Dao<T, ?> dao = (Dao<T, ?>) DatabaseHelper.getInstance().getDao(clazz);
            List<T> list = dao.queryBuilder().where().eq(idName, id).query();
            if (FFUtils.isListEmpty(list)) {
                return new ArrayList<T>();
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    /**
     * 更新一个对象
     *
     * @param object
     * @return
     */
    public static <T> boolean update(T object) {
        try {
            Dao<T, ?> dao = (Dao<T, ?>) DatabaseHelper.getInstance().getDao(object.getClass());
            dao.update(object);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除一个对象
     *
     * @param object
     * @return
     */
    public static <T> boolean delete(T object) {
        try {
            Dao<T, ?> dao = (Dao<T, ?>) DatabaseHelper.getInstance().getDao(object.getClass());
            dao.delete(object);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 清除表内容
     *
     * @return
     */
    public static <T> boolean deleteAll(Class<T> clazz) {
        try {
            TableUtils.clearTable(DatabaseHelper.getInstance().getConnectionSource(), clazz);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据SQL语句来查询
     *
     * @param sql
     * @return
     */
    public static boolean executeSql(String sql) {
        DatabaseHelper.getInstance().getReadableDatabase().execSQL(sql);
        return true;
    }

    /**
     * 存储或更新所有
     * @param list
     * @return
     */
    public static <T> boolean[] saveOrUpdataAll(List<T> list) {
        boolean[] bs = new boolean[]{false, false};
        if (list == null || list.size() == 0) {
            return bs;
        }
        try {
            Dao<T, ?> dao = (Dao<T, ?>) DatabaseHelper.getInstance().getDao(list.get(0).getClass());
            for (T t : list) {
                CreateOrUpdateStatus createOrUpdate = dao.createOrUpdate(t);
                bs[0] = createOrUpdate.isCreated() || bs[0];
                bs[1] = createOrUpdate.isUpdated() || bs[1];
            }
            return bs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bs;
    }

    public static <T,K> boolean deleteByID(Class<T> clazz, K id) {

        try {
            Dao<T, K> dao = (Dao<T, K>) DatabaseHelper.getInstance().getDao(clazz);
            dao.deleteById(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
