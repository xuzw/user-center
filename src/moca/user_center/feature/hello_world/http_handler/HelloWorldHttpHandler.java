package moca.user_center.feature.hello_world.http_handler;

import moca.user_center.base.http_handler.BaseHttpHandler;
import moca.user_center.base.request.JsonRequest;
import moca.user_center.base.response.JsonResponse;

public class HelloWorldHttpHandler extends BaseHttpHandler {

    @Override
    protected void doGet(JsonRequest request, JsonResponse response) throws Exception {
        doPost(request, response);
    }

    @Override
    protected void doPost(JsonRequest request, JsonResponse response) throws Exception {
        response.set("text", "HelloWorld!");
        response.success();
    }

}
