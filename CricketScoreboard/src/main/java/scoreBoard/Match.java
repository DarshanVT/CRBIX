package scoreBoard;

import java.util.ArrayList;
import java.util.List;

public class Match {
	String matchName;
    Team teamA;
    Team teamB;

    public Match(String matchName, Team a, Team b) {
        this.matchName = matchName;
        this.teamA = a;
        this.teamB = b;
    }

    public static List<Match> getWorldCupMatches() {
        List<Match> list = new ArrayList<>();

        list.add(new Match("India vs Pakistan",
                new Team("India"), new Team("Pakistan")));
        list.add(new Match("Australia vs England",
                new Team("Australia"), new Team("England")));
        list.add(new Match("South Africa vs New Zealand",
                new Team("South Africa"), new Team("New Zealand")));
        list.add(new Match("Sri Lanka vs Bangladesh",
                new Team("Sri Lanka"), new Team("Bangladesh")));
        list.add(new Match("India vs Australia",
                new Team("India"), new Team("Australia")));
        list.add(new Match("Pakistan vs South Africa",
                new Team("Pakistan"), new Team("South Africa")));
        list.add(new Match("England vs New Zealand",
                new Team("England"), new Team("New Zealand")));
        list.add(new Match("India vs Sri Lanka",
                new Team("India"), new Team("Sri Lanka")));
        list.add(new Match("Australia vs Pakistan",
                new Team("Australia"), new Team("Pakistan")));
        list.add(new Match("Bangladesh vs Afghanistan",
                new Team("Bangladesh"), new Team("Afghanistan")));
        list.add(new Match("India vs New Zealand",
                new Team("India"), new Team("New Zealand")));
        list.add(new Match("England vs South Africa",
                new Team("England"), new Team("South Africa")));

        return list;
    }

}
