package iotoolbox.net.coap.message;

public enum CoAPOption {
    RESERVED(0),
    IF_MATCH(1),
    URI_HOST(3),
    ETAG(4),
    IF_NOT_MATCH(5),
    URI_OBSERVE(6),
    OBJECT_19(19),
    URI_PORT(7),
    LOCATION_PATH(8),
    URI_PATH(11),
    CONTENT_FORMATE(12),
    MAX_AGE(14),
    URI_QUERY(15),
    ACCEPT(17),
    LOCATION_QYEERY(20),
    PROXY_URI(35),
    PROXY_SCHEME(39),
    SIZEL(60);

    public final int optionCode;

    CoAPOption(int optionCode) {
        this.optionCode = optionCode;
    }

    public static CoAPOption findByCode(int optionCode) {
        for (CoAPOption option : CoAPOption.values()) {
            if (option.optionCode == optionCode) return option;
        }
        return null;
    }
}