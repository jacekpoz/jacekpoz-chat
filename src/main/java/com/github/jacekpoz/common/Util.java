package com.github.jacekpoz.common;

import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

    private Util() {/*nope*/}

    // this method was stolen from here https://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
    public static String getPublicIP() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

        return in.readLine();
    }

    public static String timestampToString(Timestamp date) {
        LocalDateTime d = date.toLocalDateTime();
        return "<html>" + d.getHour() + ":" + d.getMinute() + ":" + d.getSecond() + "<br>" +
                d.getDayOfMonth() + "-" + d.getMonthValue() + "-" + d.getYear() + "</html>";
    }

    public static String userListToString(List<UserInfo> users) {
        List<String> nicknames = users.stream()
                .map(UserInfo::getNickname)
                .collect(Collectors.toList());
        return usernamesToString(nicknames);
    }

    public static String usernamesToString(List<String> usernames) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        for (int i = 0; i < usernames.size(); i++) {
            if (i < 5) sb.append(usernames.get(i)).append("<br>");
            if (i < usernames.size() - 1) sb.append("... +").append(usernames.size() - i);
        }
        sb.append("</html>");
        return sb.toString();
    }

    public static List<UserInfo> compareUsernamesFromID(String inputUsername, List<Long> userIDs) {
        try {
            DatabaseConnector con = new DatabaseConnector(
                    "jdbc:mysql://localhost:3306/" + GlobalStuff.DB_NAME,
                    "chat-client", "DB_Password_0123456789"
            );

            return compareUsernames(inputUsername,
                    userIDs.stream()
                            .map(con::getUser)
                            .collect(Collectors.toList())
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<UserInfo> compareUsernames(String inputUsername, List<UserInfo> userInfos) {
        StringMetric metric = StringMetrics.damerauLevenshtein();

        List<UserInfo> similarUsernames = new ArrayList<>();
        for (UserInfo ui : userInfos) {
            float result = metric.compare(inputUsername, ui.getNickname());
            System.out.println(result);
            if (result > 0.5)
                similarUsernames.add(ui);
        }


        return similarUsernames;
    }
}
