package logParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Entry {
    private final String ipAddress;
    private final LocalDateTime time;
    private final int statusCode;
    private final String userAgent;

    public Entry(String ipAddress, LocalDateTime time, int statusCode, String userAgent) {
        this.ipAddress = ipAddress;
        this.time = time;
        this.statusCode = statusCode;
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getUserAgent() {
        return userAgent;
    }
    public static Entry parse(String logLine) {
        try {
          String[] parts = logLine.split("\"");
            String[] ipAndTime = parts[0].split(" ");
            String ip = ipAndTime[0];

            String timePart = parts[0].substring(parts[0].indexOf("[") + 1, parts[0].indexOf("]"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            LocalDateTime time = LocalDateTime.parse(timePart, formatter);

            String[] statusAndSize = parts[2].trim().split(" ");
            int statusCode = Integer.parseInt(statusAndSize[0]);

            String userAgent = parts[5];

            return new Entry(ip, time, statusCode, userAgent);
        } catch (Exception e) {
            return null;
        }
    }
}
