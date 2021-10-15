package com.jn.langx.http.rest;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.net.http.HttpMethod;
import com.jn.langx.util.net.http.HttpStatus;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMaps;

import java.util.List;
import java.util.Map;

public class RestRespBody<T> {
    private boolean success;
    @Nullable
    private T data;
    private int statusCode;
    private long timestamp;
    private String errorCode;
    private String errorMessage;
    @Nullable
    private String url;
    private HttpMethod method;
    @Nullable
    private Map<String, List<String>> responseHeaders;
    @Nullable
    private Map<String, List<String>> requestHeaders;

    public RestRespBody() {

    }

    public RestRespBody(boolean success, int statusCode, T data, String errorCode, String errorMessage) {
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    /**
     * @param responseHeaders the http headers
     * @since 4.0.3
     */
    public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
        if(responseHeaders instanceof MultiValueMap){
            MultiValueMap map = (MultiValueMap) responseHeaders;
            setResponseHeaders(map.toMap());
        }else {
            this.responseHeaders = responseHeaders;
        }
    }

    /**
     * @param responseHeaders the http headers
     * @since 4.0.2
     */
    public void withResponseHeaders(MultiValueMap<String, String> responseHeaders) {
        if (responseHeaders != null) {
            setRequestHeaders(responseHeaders.toMap());
        }
    }

    /**
     * @param responseHeaders the http headers
     * @since 3.6.8
     */
    public void withResponseHeaders(Map<String, String> responseHeaders) {
        withResponseHeaders(MultiValueMaps.toMultiValueMap(responseHeaders));
    }

    public Map<String, List<String>> getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * @param requestHeaders the http headers
     * @since 4.0.2
     */
    public void withRequestHeaders(MultiValueMap<String, String> requestHeaders) {
        if (requestHeaders != null) {
            setRequestHeaders(requestHeaders.toMap());
        }
    }

    /**
     * @param requestHeaders the http headers
     * @since 3.6.8
     */
    public void withRequestHeaders(Map<String, String> requestHeaders) {
        withRequestHeaders(MultiValueMaps.toMultiValueMap(requestHeaders));
    }

    /**
     * @param requestHeaders the http headers
     * @since 4.0.3
     */
    public void setRequestHeaders(Map<String, List<String>> requestHeaders) {
        if (requestHeaders instanceof MultiValueMap) {
            MultiValueMap map = (MultiValueMap) requestHeaders;
            setRequestHeaders(map.toMap());
        } else {
            this.requestHeaders = requestHeaders;
        }
    }

    /**
     * @param method the http method
     * @since 3.6.8
     */
    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    /**
     * @return the http method
     * @since 3.6.8
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * 200
     */
    public static <T> RestRespBody<T> ok() {
        return success(HttpStatus.OK.value(), null);
    }

    /**
     * 200
     */
    public static <T> RestRespBody<T> ok(T data) {
        return success(data);
    }


    /**
     * 200
     */
    public static <T> RestRespBody<T> success(T data) {
        return success(HttpStatus.OK.value(), data);
    }


    /**
     * 200
     */
    public static <T> RestRespBody<T> success(int statusCode, T data) {
        return success(statusCode, data, "", "");
    }

    /**
     * 200
     */
    public static <T> RestRespBody<T> success(int statusCode, T data, String errorCode, String errorMessage) {
        return new RestRespBody<T>(true, statusCode, data, errorCode, errorMessage);
    }

    /**
     * 400
     */
    public static <T> RestRespBody<T> error400(String errorCode, String errorMessage) {
        return badRequest(errorCode, errorMessage);
    }

    /**
     * 400
     */
    public static <T> RestRespBody<T> badRequest(String errorCode, String errorMessage) {
        return new RestRespBody<T>(false, 400, null, errorCode, errorMessage);
    }

    /**
     * 401
     */
    public static <T> RestRespBody<T> error401(String errorCode, String errorMessage) {
        return unauthorized(errorCode, errorMessage);
    }

    public static <T> RestRespBody<T> unauthorized(String errorCode, String errorMessage) {
        return new RestRespBody<T>(false, HttpStatus.UNAUTHORIZED.value(), null, errorCode, errorMessage);
    }

    /**
     * 403
     */
    public static <T> RestRespBody<T> error403(String errorCode, String errorMessage) {
        return forbidden(errorCode, errorMessage);
    }

    /**
     * 403
     */
    public static <T> RestRespBody<T> forbidden(String errorCode, String errorMessage) {
        return new RestRespBody<T>(false, 403, null, errorCode, errorMessage);
    }

    /**
     * 404
     */
    public static <T> RestRespBody<T> error404(String errorCode, String errorMessage) {
        return notFound(errorCode, errorMessage);
    }

    /**
     * 404
     */
    public static <T> RestRespBody<T> notFound(String errorCode, String errorMessage) {
        return new RestRespBody<T>(false, 404, null, errorCode, errorMessage);
    }


    /**
     * 500
     */
    public static <T> RestRespBody<T> error500(String errorCode, String errorMessage) {
        return serverInternalError(errorCode, errorMessage);
    }

    /**
     * 500
     */
    public static <T> RestRespBody<T> serverInternalError(String errorCode, String errorMessage) {
        return error(500, errorCode, errorMessage);
    }

    /**
     * 502
     */
    public static <T> RestRespBody<T> error502(String errorCode, String errorMessage) {
        return badGateway(errorCode, errorMessage);
    }

    /**
     * 502
     */
    public static <T> RestRespBody<T> badGateway(String errorCode, String errorMessage) {
        return error(502, errorCode, errorMessage);
    }

    /**
     * 503
     */
    public static <T> RestRespBody<T> error503(String errorCode, String errorMessage) {
        return serviceUnavailable(errorCode, errorMessage);
    }

    /**
     * 503
     */
    public static <T> RestRespBody<T> serviceUnavailable(String errorCode, String errorMessage) {
        return error(503, errorCode, errorMessage);
    }

    /**
     * 504
     */
    public static <T> RestRespBody<T> error504(String errorCode, String errorMessage) {
        return gatewayTimeout(errorCode, errorMessage);
    }

    /**
     * 504
     */
    public static <T> RestRespBody<T> gatewayTimeout(String errorCode, String errorMessage) {
        return error(504, errorCode, errorMessage);
    }

    /**
     * 505
     */
    public static <T> RestRespBody<T> error505(int statusCode, String errorCode, String errorMessage) {
        return httpVersionNotSupported(errorCode, errorMessage);
    }


    /**
     * 505
     */
    public static <T> RestRespBody<T> httpVersionNotSupported(String errorCode, String errorMessage) {
        return error(505, errorCode, errorMessage);
    }

    /**
     * 511
     */
    public static <T> RestRespBody<T> error511(int statusCode, String errorCode, String errorMessage) {
        return new RestRespBody<T>(false, statusCode, null, errorCode, errorMessage);
    }

    /**
     * 511
     */
    public static <T> RestRespBody<T> unAuthentication(String errorCode, String errorMessage) {
        return error(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value(), errorCode, errorMessage);
    }

    public static <T> RestRespBody<T> error(int statusCode, String errorCode, String errorMessage) {
        return new RestRespBody<T>(false, statusCode, null, errorCode, errorMessage);
    }

}
