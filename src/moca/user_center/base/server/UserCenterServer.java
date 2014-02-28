package moca.user_center.base.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

import moca.user_center.base.database.SingletonDatasource;

import com.sun.net.httpserver.HttpServer;

public class UserCenterServer {

    private static final String CONFIG_FILE_PATH = "/user-center.properties";
    private static final String CONFIG_FILE_PATH_CONTEXT_BINDER = "/context-binder.xml";
    private static final String DEFAULT_CONTEXT = "/user-center";
    private static final String DEFAULT_PORT = "9797";

    private HttpServer httpServer;
    private Properties config;
    private int port;
    private String baseContext;
    private ContextBinder contextBinder;

    public void init() throws Exception {
        loadConfig();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(port), 0);
        bindContext();
    }

    private void loadConfig() throws IOException {
        config = new Properties();
        config.load(UserCenterServer.class.getResourceAsStream(CONFIG_FILE_PATH));
        port = Integer.valueOf(config.getProperty("UserCenter.http.port", DEFAULT_PORT));
        baseContext = config.getProperty("UserCenter.http.context.base", DEFAULT_CONTEXT);
    }

    private void bindContext() throws Exception {
        File xmlFile = new File(UserCenterServer.class.getResource(CONFIG_FILE_PATH_CONTEXT_BINDER).getFile());
        contextBinder = ContextBinderBuilder.buildFrom(xmlFile);
        for (String key : contextBinder.getMap().keySet()) {
            httpServer.createContext(baseContext + key, contextBinder.query(key));
        }
    }

    public void start() {
        httpServer.start();
        System.out.println("User Center Server Started At http://localhost:" + httpServer.getAddress().getPort());
    }

    public void stop() {
        httpServer.stop(0);
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public void setHttpServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getBaseContext() {
        return baseContext;
    }

    public void setBaseContext(String baseContext) {
        this.baseContext = baseContext;
    }

    public ContextBinder getContextBinder() {
        return contextBinder;
    }

    public void setContextBinder(ContextBinder contextBinder) {
        this.contextBinder = contextBinder;
    }

    public static void main(String[] args) throws Exception {
        SingletonDatasource.init();
        UserCenterServer userCenterServer = new UserCenterServer();
        userCenterServer.init();
        userCenterServer.start();
    }

}
