package iotoolbox.net.coap;

import iotoolbox.net.coap.exeception.CoAPException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CoAPUrl {
    private String url;
    private Scheme scheme = Scheme.coap;
    private String host;
    private int port;
    private String path;
    private Map<String, String> queryParamMap = new HashMap<>();

    public CoAPUrl(String host, int port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public CoAPUrl(String url) throws CoAPException {
        url = url.trim();
        this.url = url;
        boolean supportScheme = false;
        for (Scheme scheme : Scheme.values()) {
            if (url.startsWith(scheme.name())) {
                this.scheme = scheme;
                url = url.replace(scheme.name(), "http");
                supportScheme = true;
            }
        }
        if (!supportScheme) throw new CoAPException("url error: not support scheme");
        URL trans = null;
        try {
            trans = new URL(url);
        } catch (MalformedURLException e) {
            throw new CoAPException("url error: not support ," + e.getMessage());
        }
        this.host = trans.getHost();
        this.port = this.scheme.defaultPort == trans.getPort() ? this.scheme.defaultPort : trans.getPort();
        this.path = trans.getPath().startsWith("/") ? trans.getPath().replace("/",""):trans.getPath();
        for (String query : trans.getQuery().split("&")) {
            queryParamMap.put(query.split("=")[0], query.split("=")[1]);
        }
    }

    public void addQueryParam(String key, String value) {
        this.queryParamMap.put(key, value);
    }

    public String getUrl() {
        if (url != null) return url;
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(scheme).append("://").append(host).append(":").append(host).append("/").append(path).append("?");
        for (String s : queryParamMap.keySet()) {
            uriBuilder.append("s").append("=").append(queryParamMap.get(s));
        }
        return uriBuilder.toString();
    }

    public Scheme getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParamMap() {
        return queryParamMap;
    }

    public enum Scheme {
        coap(5683),
        coaps(5684);
        public int defaultPort;

        Scheme(int defaultPort) {
            this.defaultPort = defaultPort;
        }

        public static Scheme findByName(String name) {
            for (Scheme scheme : Scheme.values()) {
                if (scheme.name().equals(name)) return scheme;
            }
            return null;
        }
    }
}
