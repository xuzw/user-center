package moca.user_center.base.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import moca.user_center.feature.user.dao.UserDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingletonDao {

    private static final Logger logger = LoggerFactory.getLogger(SingletonDao.class);
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ReadLock readLock = lock.readLock();
    private static final WriteLock writeLock = lock.writeLock();
    private static final Map<Class<?>, Object> typeMaptoDao = new HashMap<Class<?>, Object>();

    static {
        try {
            addDaos();
        } catch (Exception e) {
            logger.error("Failed To Init SingletonDao", e);
        }
    }

    private static void addDaos() throws Exception {
        add(new UserDao());
    }

    /**
     * 是否包含指定类型的Dao
     * 
     * @param type
     * @return
     */
    public static boolean contains(Class<?> type) {
        readLock.lock();
        try {
            return typeMaptoDao.containsKey(type);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 查询指定类型的Dao
     * 
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(Class<?> type) {
        readLock.lock();
        try {
            return (T) typeMaptoDao.get(type);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 设置指定类型的Dao
     * 
     * @param type
     * @param dao
     * @return
     */
    public static Object put(Class<?> type, Object dao) {
        logger.trace("Put Dao, type={}, dao={}", type.getName(), dao);
        writeLock.lock();
        try {
            return typeMaptoDao.put(type, dao);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * equals {@link #put(Class, Object) put(dao.getClass(), dao)}
     * 
     * @param dao
     * @return
     */
    public static Object add(Object dao) {
        return put(dao.getClass(), dao);
    }

    /**
     * 移除指定类型的Dao
     * 
     * @param type
     * @return
     */
    public static Object remove(Class<?> type) {
        logger.trace("Remove Dao, type={}", type);
        writeLock.lock();
        try {
            return typeMaptoDao.remove(type);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 清空全部
     */
    public static void clear() {
        logger.trace("Clear All Dao");
        writeLock.lock();
        try {
            typeMaptoDao.clear();
        } finally {
            writeLock.unlock();
        }
    }

}
