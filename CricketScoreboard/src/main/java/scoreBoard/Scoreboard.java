package scoreBoard;

import java.util.*;

public class Scoreboard {

    Match match;
    int oversLimit = 1;

    Player[] battingTeamPlayers = new Player[11];
    Player[] bowlingTeamPlayers = new Player[11];

    int strikerIndex = 0;
    int nonStrikerIndex = 1;
    int bowlerIndex = 0;

    int runs = 0;
    int wickets = 0;

    List<String> history = new ArrayList<>();

    public Scoreboard(Match match) {
        this.match = match;

        for (int i = 0; i < 11; i++) {
            battingTeamPlayers[i] = new Player("Batsman " + (i + 1));
            bowlingTeamPlayers[i] = new Player("Bowler " + (i + 1));
        }
    }

    public void startMatch() {
        System.out.println("\n FIRST INNINGS START =\n");
        int team1Runs = startInnings(match.teamA.teamName);

        int target = team1Runs + 1;
        System.out.println("\nTARGET FOR " + match.teamB.teamName + ": " + target);

        resetForNextInnings();

        System.out.println("\n SECOND INNINGS START \n");
        int team2Runs = startInnings(match.teamB.teamName);

        System.out.println("\n MATCH RESULT ");
        System.out.println(match.teamA.teamName + " scored: " + team1Runs);
        System.out.println(match.teamB.teamName + " scored: " + team2Runs);

        if (team2Runs >= target) {
            System.out.println("\nWinner Team : " + match.teamB.teamName + "\n Congrajulations ! You Won the Match");
        } else {
            System.out.println("\\nWinner Team : " + match.teamA.teamName + "\n Congrajulations ! You Won the Match");
        }

        showHistory();
    }

    public int startInnings(String battingTeam) {
        Scanner sc = new Scanner(System.in);

        runs = 0;
        wickets = 0;

        System.out.println("Batting: " + battingTeam);

        for (int over = 1; over <= oversLimit; over++) {
            System.out.println("\nOver " + over + "/" + oversLimit);

            int ball = 1;
            while (ball <= 6) {

                if (wickets == 10) {
                    System.out.println("\nInnings Over: All 10 wickets down!");
                    history.add(battingTeam + " ALL OUT");
                    return runs;
                }

                System.out.print("Ball " + ball + " outcome (0,1,2,3,4,6, W, NB, WD): ");
                String inp = sc.nextLine().trim().toUpperCase();

               
                if (!inp.matches("0|1|2|3|4|6|W|NB( [0-6])?|WD")) {
                    System.out.println(" Invalid input! Allowed: 0,1,2,3,4,6,W,NB,NB x,WD");
                    continue;
                }
                if (inp.startsWith("NB")) {

                    int extraRuns = 1;
                    int batsmanRuns = 0;

                    
                    String[] parts = inp.split(" ");
                    if (parts.length == 2) {

                        if (!parts[1].matches("0|1|2|3|4|6")) {
                            System.out.println(" Invalid NB runs! Allowed: 0,1,2,3,4,6");
                            continue; // retry same ball
                        }

                        batsmanRuns = Integer.parseInt(parts[1]);
                    }

                    runs += extraRuns + batsmanRuns;

                    
                    battingTeamPlayers[strikerIndex].runs += batsmanRuns;

                    if (batsmanRuns > 0) {
                        battingTeamPlayers[strikerIndex].balls++;
                    }

                    history.add("NO BALL: +1 extra, +" + batsmanRuns + " batsman run(s)");

                    System.out.println("NO BALL! +1 Extra, +" + batsmanRuns + " Batsman Runs");

                    
                    if (batsmanRuns % 2 != 0) {
                        int temp = strikerIndex;
                        strikerIndex = nonStrikerIndex;
                        nonStrikerIndex = temp;
                    }

                    printScore(battingTeam);
                    continue; 
                }

                
                if (inp.equals("WD")) {
                    runs += 1;
                    history.add("WIDE BALL (+1)");
                    System.out.println("WIDE BALL! +1 Run");
                    printScore(battingTeam);
                    continue; // ball does NOT count
                }

                
                if (inp.equals("W")) {
                    wickets++;
                    history.add(battingTeam + " WICKET at Over " + over + " Ball " + ball);
                    System.out.println("WICKET!");

                    strikerIndex++;
                    if (strikerIndex == 11) {
                        System.out.println("\nInnings Over: No batsmen left!");
                        return runs;
                    }

                } else {
                    int r = Integer.parseInt(inp);
                    runs += r;

                    battingTeamPlayers[strikerIndex].runs += r;
                    battingTeamPlayers[strikerIndex].balls++;

                    history.add(battingTeam + " scored " + r + " run(s)");

                    if (r % 2 != 0) {
                        int temp = strikerIndex;
                        strikerIndex = nonStrikerIndex;
                        nonStrikerIndex = temp;
                    }
                }

                printScore(battingTeam);
                ball++;
            }

            // END OF OVER — rotate strike
            int temp = strikerIndex;
            strikerIndex = nonStrikerIndex;
            nonStrikerIndex = temp;
        }

        System.out.println("\nInnings Over: Overs completed!");
        history.add(battingTeam + " Overs Completed");
        return runs;
    }

    public void resetForNextInnings() {

        for (int i = 0; i < 11; i++) {
            battingTeamPlayers[i] = new Player("Batsman " + (i + 1));
        }
        strikerIndex = 0;
        nonStrikerIndex = 1;
        wickets = 0;
        runs = 0;
    }

    public void printScore(String teamName) {
        System.out.println("\n LIVE SCORE (" + teamName + ") ");
        System.out.println("Score: " + runs + "/" + wickets);
        System.out.println("Striker: " + battingTeamPlayers[strikerIndex].name
                + " - " + battingTeamPlayers[strikerIndex].runs
                + "(" + battingTeamPlayers[strikerIndex].balls + ")");
        System.out.println("Non-Striker: " + battingTeamPlayers[nonStrikerIndex].name
                + " - " + battingTeamPlayers[nonStrikerIndex].runs
                + "(" + battingTeamPlayers[nonStrikerIndex].balls + ")");
        System.out.println("Bowler: " + bowlingTeamPlayers[bowlerIndex].name);
        
    }

    public void showHistory() {
        System.out.println("\n MATCH HISTORY \n");
        for (String event : history) {
            System.out.println(event);
        }
    }
}
