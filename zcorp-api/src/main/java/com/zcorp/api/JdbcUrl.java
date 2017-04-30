package com.zcorp.api;

public final class JdbcUrl {

    public static String mysql(String host, int port, String database, boolean requireSSL, boolean debug) {
        StringBuilder builder = new StringBuilder();
        builder.append("jdbc:mysql://");
        builder.append(host);
        builder.append(":");
        builder.append(port);
        builder.append("/");
        builder.append(database);
        builder.append("?verifyServerCertificate=");
        builder.append(requireSSL ? "true" : "false");
        builder.append("&useSSL=");
        builder.append(requireSSL ? "true" : "false");
        if (debug) {
            builder.append("&profileSQL=true");
        }
        return builder.toString();
    }

    private JdbcUrl() {
    }
}
