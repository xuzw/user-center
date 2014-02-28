package moca.user_center.feature.user.http_handler;

import java.sql.SQLException;

import moca.user_center.base.config.UserCenterProperties;
import moca.user_center.base.dao.SingletonDao;
import moca.user_center.base.http_handler.BaseHttpHandler;
import moca.user_center.base.request.JsonRequest;
import moca.user_center.base.response.JsonResponse;
import moca.user_center.feature.user.dao.UserDao;

public class RegisterHttpHandler extends BaseHttpHandler {

    /**
     * 结果码：用户名太短
     */
    private static final int RESULT_CODE_USERNAME_TOO_SHORT = 1;

    /**
     * 结果码：用户名太长
     */
    private static final int RESULT_CODE_USERNAME_TOO_LONG = 2;

    /**
     * 结果码：用户名无效、不合法
     */
    private static final int RESULT_CODE_USERNAME_INVALID = 3;

    /**
     * 结果码：重复注册
     */
    private static final int RESULT_CODE_USERNAME_DUPLICATE = 10;

    private static final int USERNAME_LENGTH_MIN;
    private static final int USERNAME_LENGTH_MAX;
    private static final String USERNAME_FORMAT;

    static {
        USERNAME_LENGTH_MIN = UserCenterProperties.getInt("Feature.user.http_handler.RegisterHttpHandler.username.length.min", 5);
        USERNAME_LENGTH_MAX = UserCenterProperties.getInt("Feature.user.http_handler.RegisterHttpHandler.username.length.max", 15);
        USERNAME_FORMAT = UserCenterProperties.get("Feature.user.http_handler.RegisterHttpHandler.username.format", "^[a-zA-Z0-9]*$");
    }

    @Override
    protected void doGet(JsonRequest request, JsonResponse response) throws Exception {
        doPost(request, response);
    }

    @Override
    protected void doPost(JsonRequest request, JsonResponse response) throws Exception {
        String userName = request.get("userName", String.class);

        if (userName.length() < USERNAME_LENGTH_MIN) {
            response.fail(RESULT_CODE_USERNAME_TOO_SHORT);
            return;
        }

        if (userName.length() > USERNAME_LENGTH_MAX) {
            response.fail(RESULT_CODE_USERNAME_TOO_LONG);
            return;
        }

        if (!userName.matches(USERNAME_FORMAT)) {
            response.fail(RESULT_CODE_USERNAME_INVALID);
            return;
        }

        String password = request.get("password", String.class);
        if (existUser(userName)) {
            response.fail(RESULT_CODE_USERNAME_DUPLICATE);
            return;
        }

        UserDao userDao = SingletonDao.get(UserDao.class);
        userDao.insert(userName, password);
        response.success();
    }

    private boolean existUser(String userName) throws SQLException {
        UserDao userDao = SingletonDao.get(UserDao.class);
        return userDao.selectByUsername(userName) != null;
    }

}
