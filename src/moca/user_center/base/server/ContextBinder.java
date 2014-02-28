package moca.user_center.base.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpHandler;

public class ContextBinder {

    private Map<String, HttpHandler> map = new HashMap<String, HttpHandler>();

    public void bind(String context, HttpHandler httpHandler) {
        map.put(context, httpHandler);
    }

    public void unbind(String context) {
        map.remove(context);
    }

    public HttpHandler query(String context) {
        return map.get(context);
    }

    public Map<String, HttpHandler> getMap() {
        return Collections.unmodifiableMap(map);
    }

}
