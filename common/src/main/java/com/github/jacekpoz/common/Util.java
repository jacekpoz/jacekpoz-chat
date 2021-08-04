package com.github.jacekpoz.common;

import com.github.jacekpoz.common.sendables.User;
import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Util {

    private Util() {/*nope*/}

    public static String timestampToString(Timestamp date) {
        LocalDateTime d = date.toLocalDateTime();
        return String.format("<html> %02d:%02d:%02d<br>%02d-%02d-%04d</html>",
                d.getHour(), d.getMinute(), d.getSecond(),
                d.getDayOfMonth(), d.getMonthValue(), d.getYear());
    }

    public static String userListToString(List<User> users) {
        List<String> nicknames = users.stream()
                .map(User::getNickname)
                .collect(Collectors.toList());
        return usernamesToString(nicknames);
    }

    public static String usernamesToString(List<String> usernames) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        int i;
        for (i = 0; i < usernames.size(); i++) {
            if (i < 5) sb.append(usernames.get(i));
            else break;
            if (i < 4) sb.append("<br>");
        }
        if (i < usernames.size() - 1) sb.append("... +").append(usernames.size() - i);
        sb.append("</html>");
        return sb.toString();
    }

    public static List<User> compareUsernames(String inputUsername, List<User> userInfos) {
        StringMetric metric = StringMetrics.damerauLevenshtein();

        List<User> similarUsernames = new ArrayList<>();
        for (User ui : userInfos) {
            float result = metric.compare(inputUsername, ui.getNickname());
            System.out.println(result);
            if (result > 0.5 || ui.getNickname().startsWith(inputUsername))
                similarUsernames.add(ui);
        }

        return similarUsernames;
    }
}
