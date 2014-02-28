package moca.user_center.base.request;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.sun.net.httpserver.HttpExchange;

public class JsonRequest {

    private HttpExchange httpExchange;
    private JSONObject jsonObject;
    private Map<String, Object> metaDate;

    public JsonRequest() {
    }

    public JsonRequest(String param, HttpExchange httpExchange) throws JsonRequestParseFailedException {
        parse(param, httpExchange);
    }

    public Object get(String key) {
        return jsonObject.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clz) {
        return (T) jsonObject.get(key);
    }

    public void parse(String param, HttpExchange httpExchange) throws JsonRequestParseFailedException {
        try {
            setHttpExchange(httpExchange);

            Map<String, Object> metaDate = new HashMap<String, Object>();
            metaDate.put("timestamp", new Timestamp(new Date().getTime()));
            setMetaDate(metaDate);

            JSONObject jsonObject = (JSONObject) JSONObject.parse(param);
            setJsonObject(jsonObject);
        } catch (Exception e) {
            throw new JsonRequestParseFailedException(e);
        }
    }

    public HttpExchange getHttpExchange() {
        return httpExchange;
    }

    public void setHttpExchange(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
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

}
