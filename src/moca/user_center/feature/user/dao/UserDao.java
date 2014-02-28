package moca.user_center.feature.user.dao;

import java.sql.SQLException;
import java.util.UUID;

import moca.user_center.base.dao.SingletonDao;
import moca.user_center.base.database.SingletonDatasource;
import moca.user_center.feature.user.model.User;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.ibatis.jdbc.SQL;

import xuzw.dao.implement.AbstractDao;

public class UserDao extends AbstractDao<User> {

    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_PASSWORD = "password";
    public static final String FIELD_NAME_DATECREATED = "dateCreated";

    public static final String TABLE_NAME = "User";
    public static final String[] PRIMARY_KEY_NAMES = new String[] { FIELD_NAME_ID };

    public static final String SQL_INSERT = "insert";
    private static final String SQL_VERIFY_PASSWORD = "verifyPassword";
    private static final String SQL_SELECT_BY_USERNAME = "selectByUsername";

    public UserDao() {
        setSql(SQL_INSERT, new SQL() //
        .INSERT_INTO(TABLE_NAME) //
        .VALUES(FIELD_NAME_ID, "?") //
        .VALUES(FIELD_NAME_NAME, "?") //
        .VALUES(FIELD_NAME_PASSWORD, "?") //
        .toString());

        setSql(SQL_VERIFY_PASSWORD, new SQL() //
        .SELECT("count(*) AS count").FROM(TABLE_NAME) //
        .WHERE(FIELD_NAME_NAME + " = ?") //
        .WHERE(FIELD_NAME_PASSWORD + " = ?") //
        .toString());

        setSql(SQL_SELECT_BY_USERNAME, new SQL() //
        .SELECT("*").FROM(TABLE_NAME) //
        .WHERE(FIELD_NAME_NAME + " = ?") //
        .toString());
    }

    public static void main(String[] args) throws Exception {
        SingletonDatasource.init();
        UserDao dao = SingletonDao.get(UserDao.class);
        System.out.println(dao.selectByUsername("test"));
    }

    public int insert(String userName, String password) throws SQLException {
        QueryRunner queryRunner = SingletonDatasource.getQueryRunner();
        String sql = getSql(SQL_INSERT);
        return queryRunner.update(sql, UUID.randomUUID().toString(), userName, password);
    }

    public boolean verifyPassword(String userName, String password) throws SQLException {
        QueryRunner queryRunner = SingletonDatasource.getQueryRunner();
        String sql = getSql(SQL_VERIFY_PASSWORD);
        return queryRunner.query(sql, new ScalarHandler<Long>(), userName, password) > 0;
    }

    public User selectByUsername(String userName) throws SQLException {
        QueryRunner queryRunner = SingletonDatasource.getQueryRunner();
        String sql = getSql(SQL_SELECT_BY_USERNAME);
        return queryRunner.query(sql, new BeanHandler<User>(User.class), userName);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getPrimaryKeyNames() {
        return PRIMARY_KEY_NAMES;
    }

}
