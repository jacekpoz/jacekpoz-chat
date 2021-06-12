package com.github.jacekpoz.common;

import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringMetricBuilder;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.metrics.StringMetrics;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Util {

    private Util() {/*nope*/}

    // this method was stolen from here https://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
    public static String getPublicIP() throws IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

        return in.readLine();
    }

    public static String localDateTimeToString(LocalDateTime date) {
        return "<html>" + date.getHour() + ":" + date.getMinute() + ":" + date.getSecond() + "<br>" +
                date.getDayOfMonth() + "-" + date.getMonthValue() + "-" + date.getYear() + "</html>";
    }

    public static List<UserInfo> compareUsernames(String inputUsername, List<UserInfo> userInfos) {
        StringMetric metric = StringMetricBuilder
                .with(new CosineSimilarity<>())
                .simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
                .simplify(Simplifiers.replaceNonWord())
                .tokenize(Tokenizers.whitespace())
                .build();

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
