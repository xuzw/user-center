package moca.user_center.feature.user.http_handler;

import moca.user_center.base.dao.SingletonDao;
import moca.user_center.base.http_handler.BaseHttpHandler;
import moca.user_center.base.request.JsonRequest;
import moca.user_center.base.response.JsonResponse;
import moca.user_center.feature.user.dao.UserDao;

public class VerifyHttpHandler extends BaseHttpHandler {

    /**
     * 结果码：错误的用户名或密码
     */
    private static final int RESULT_CODE_ERROR_USERNAME_OR_PASSWORD = 1;

    @Override
    protected void doGet(JsonRequest request, JsonResponse response) throws Exception {
        doPost(request, response);
    }

    @Override
    protected void doPost(JsonRequest request, JsonResponse response) throws Exception {
        UserDao userDao = SingletonDao.get(UserDao.class);
        String userName = request.get("userName", String.class);
        String password = request.get("password", String.class);
        boolean success = userDao.verifyPassword(userName, password);
        if (success) {
            response.success();
        } else {
            response.fail(RESULT_CODE_ERROR_USERNAME_OR_PASSWORD);
        }
    }

}
