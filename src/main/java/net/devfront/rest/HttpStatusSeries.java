package net.devfront.rest;

/**
 * Enumeration of HTTP status series.
 */
public enum HttpStatusSeries {
    INFORMATIONAL(1),
    SUCCESSFUL(2),
    REDIRECTION(3),
    CLIENT_ERROR(4),
    SERVER_ERROR(5);

    private final int value;

    HttpStatusSeries(int value) {
        this.value = value;
    }

    public static HttpStatusSeries valueOf(int status) {
        int seriesCode = status / 100;
        for (HttpStatusSeries series : values()) {
            if (series.value == seriesCode) {
                return series;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + status + "]");
    }
}
