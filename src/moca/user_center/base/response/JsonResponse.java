package moca.user_center.base.response;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import moca.user_center.base.request.JsonRequest;

import com.alibaba.fastjson.JSONObject;

public class JsonResponse {

    /**
     * 结果码：成功
     */
    public static final int RESULT_CODE_SUCCESS = 0;

    /**
     * 结果码：未知错误
     */
    public static final int RESULT_CODE_UNKNOWN_ERROR = 9999;

    private JsonRequest jsonRequest;
    private JSONObject jsonObject = new JSONObject();
    private Map<String, Object> metaDate = new HashMap<String, Object>();
    private int httpStatusCode = 200;

    public JsonResponse() {
    }

    public JsonResponse(JsonRequest jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    public void success() {
        setResultCode(RESULT_CODE_SUCCESS);
    }

    public void fail() {
        setResultCode(RESULT_CODE_UNKNOWN_ERROR);
    }

    public void fail(int resultCode) {
        setResultCode(resultCode);
    }

    public void setResultCode(int resultCode) {
        metaDate.put("result", resultCode);
        setTimestamp();
    }

    public void setTimestamp() {
        setTimestamp(new Timestamp(new Date().getTime()));
    }

    public void setTimestamp(Timestamp timestamp) {
        metaDate.put("timestamp", timestamp);
    }

    public void set(String key, Object value) {
        jsonObject.put(key, value);
    }

    public String toJsonString() {
        jsonObject.put("metaDate", metaDate);
        return jsonObject.toJSONString();
    }

    public JsonRequest getJsonRequest() {
        return jsonRequest;
    }

    public void setJsonRequest(JsonRequest jsonRequest) {
        this.jsonRequest = jsonRequest;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Map<String, Object> getMetaDate() {
        return metaDate;
    }

    public void setMetaDate(Map<String, Object> metaDate) {
        this.metaDate = metaDate;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

}
