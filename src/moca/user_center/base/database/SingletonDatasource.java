package moca.user_center.base.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class SingletonDatasource {

    public static final String DEFAULT_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String BONECP_CONFIG_XML_FILE_PATH = "/bonecp.xml";
    private static final Logger logger = LoggerFactory.getLogger(SingletonDatasource.class);
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ReadLock readLock = lock.readLock();
    private static final WriteLock writeLock = lock.writeLock();
    private static BoneCPDataSource dataSource;
    private static QueryRunner queryRunner;

    /**
     * equals {@link #init(String, BoneCPDataSource) init(null, null)}
     * 
     * @return
     */
    public static boolean init() {
        return init(null, null);
    }

    /**
     * 使用指定的JdbcDriver, 和{@link BoneCPDataSource}初始化{@link SingletonDatasource}
     * 
     * @param jdbcDriver
     * @param dataSource
     * @return
     */
    public static boolean init(String jdbcDriver, BoneCPDataSource dataSource) {
        setDataSource(dataSource);
        return loadJdbcDriver(jdbcDriver) && isConnect();
    }

    /**
     * 加载指定的JdbcDriver
     * 
     * @param jdbcDriver
     *            如果为null, 则使用默认值{@link #DEFAULT_JDBC_DRIVER}
     * @return 是否成功加载
     */
    public static boolean loadJdbcDriver(String jdbcDriver) {
        try {
            String _jdbcDriver = (jdbcDriver == null) ? DEFAULT_JDBC_DRIVER : jdbcDriver;
            logger.info("Load Jdbc Driver, jdbcDriver={}", _jdbcDriver);
            Class.forName(_jdbcDriver);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 查询{@link BoneCPDataSource}
     * 
     * @return
     */
    public static BoneCPDataSource getDataSource() {
        readLock.lock();
        try {
            return SingletonDatasource.dataSource;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 设置{@link BoneCPDataSource}
     * 
     * @param dataSource
     *            如果为null, 则使用默认值
     */
    public static void setDataSource(BoneCPDataSource dataSource) {
        writeLock.lock();
        FileInputStream bonecpConfigXmlFileInputStream = null;
        try {
            if (dataSource == null) {
                String bonecpConfigXmlFilePath = SingletonDatasource.class.getResource(BONECP_CONFIG_XML_FILE_PATH).getFile();
                bonecpConfigXmlFileInputStream = new FileInputStream(new File(bonecpConfigXmlFilePath));
                BoneCPConfig defaultBoneCPConfig = new BoneCPConfig(bonecpConfigXmlFileInputStream, "bonecp-config");
                dataSource = new BoneCPDataSource(defaultBoneCPConfig);
            }
            logger.trace("Set DataSource, dataSource={}", dataSource);
            SingletonDatasource.dataSource = dataSource;
            queryRunner = new QueryRunner(dataSource);
        } catch (Exception e) {
            logger.error(null, e);
        } finally {
            if (bonecpConfigXmlFileInputStream != null) {
                try {
                    bonecpConfigXmlFileInputStream.close();
                } catch (IOException e) {
                    logger.error(null, e);
                }
            }
            writeLock.unlock();
        }
    }

    /**
     * 查询{@link QueryRunner}
     * 
     * @return
     */
    public static QueryRunner getQueryRunner() {
        readLock.lock();
        try {
            return queryRunner;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 是否已连接
     * 
     * @return
     */
    public static boolean isConnect() {
        readLock.lock();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            return true;
        } catch (SQLException e) {
            logger.info("Failed To Connect DataSource");
            return false;
        } finally {
            readLock.unlock();
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("Failed To Close Test Connection", e);
            }
        }
    }

    /**
     * 关闭
     */
    public static void close() {
        logger.info("Closing Datasource");
        writeLock.lock();
        try {
            if (dataSource != null) {
                dataSource.close();
            }
        } finally {
            writeLock.unlock();
        }
        logger.info("Closed");
    }

}
