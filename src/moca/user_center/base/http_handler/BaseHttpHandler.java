package moca.user_center.base.http_handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

import moca.user_center.base.request.JsonRequest;
import moca.user_center.base.request.JsonRequestParseFailedException;
import moca.user_center.base.response.JsonResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class BaseHttpHandler implements HttpHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        dispatch(exchange);
    }

    /**
     * 分发Http请求
     * 
     * @param httpExchange
     * @throws IOException
     */
    private void dispatch(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        if ("GET".equalsIgnoreCase(requestMethod)) {
            get(httpExchange);
        } else if ("POST".equalsIgnoreCase(requestMethod)) {
            post(httpExchange);
        }
    }

    private void get(HttpExchange httpExchange) throws IOException {
        logger.trace("Receive GET Http Request, from={}, requestUri={}", httpExchange.getRemoteAddress(), httpExchange.getRequestURI());
        JsonRequest request = new JsonRequest();
        JsonResponse response = new JsonResponse(request);
        try {
            String queryString = httpExchange.getRequestURI().getQuery();
            String param = (queryString == null) ? "" : URLDecoder.decode(queryString, "UTF-8");
            request.parse(param, httpExchange);
            doGet(request, response);
        } catch (JsonRequestParseFailedException e) {
            logger.error("Error On Parse GET Http Request, from={}, requestUri={}", httpExchange.getRemoteAddress(), httpExchange.getRequestURI(), e);
            response.fail();
        } catch (Exception e1) {
            logger.error("Error On Handle GET Http Request, from={}, requestUri={}", httpExchange.getRemoteAddress(), httpExchange.getRequestURI(), e1);
            response.fail();
        } finally {
            sendResponse(response);
        }
    }

    private void post(HttpExchange httpExchange) throws IOException {
        String requestBody = IOUtils.toString(httpExchange.getRequestBody());
        logger.trace("Receive POST Http Request, from={}, requestUri={}, requestBody={}", httpExchange.getRemoteAddress(), httpExchange.getRequestURI(), requestBody);
        JsonRequest request = new JsonRequest();
        JsonResponse response = new JsonResponse(request);
        try {
            request.parse(requestBody, httpExchange);
            doPost(request, response);
        } catch (JsonRequestParseFailedException e) {
            logger.error("Error On Parse POST Http Request, from={}, requestUri={}, requestBody={}", httpExchange.getRemoteAddress(), httpExchange.getRequestURI(), requestBody, e);
            response.fail();
        } catch (Exception e1) {
            logger.error("Error On Handle POST Http Request, from={}, requestUri={}, requestBody={}", httpExchange.getRemoteAddress(), httpExchange.getRequestURI(), requestBody, e1);
            response.fail();
        } finally {
            sendResponse(response);
        }
    }

    protected abstract void doGet(JsonRequest request, JsonResponse response) throws Exception;

    protected abstract void doPost(JsonRequest request, JsonResponse response) throws Exception;

    public static void sendResponse(JsonResponse jsonResponse) throws IOException {
        sendResponse(jsonResponse.toJsonString(), jsonResponse.getHttpStatusCode(), jsonResponse.getJsonRequest().getHttpExchange());
    }

    public static void sendResponse(String response, int statusCode, HttpExchange httpExchange) throws IOException {
        if (response == null) {
            httpExchange.sendResponseHeaders(statusCode, 0);
        } else {
            httpExchange.sendResponseHeaders(statusCode, response.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }

}
