package xyz.blackice.rxokhttp;

/**
 * Created by Ann on 11/8/17.
 */

public class RequestBuilder {

    private String url;
    private int timeout;
    private Class clazz;

    public RequestBuilder(){
    }

    public String getUrl() {
        return url;
    }

    public RequestBuilder setUrl(String url) {
        this.url = url;
        return this;

    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
