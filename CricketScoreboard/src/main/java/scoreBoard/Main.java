package scoreBoard;

import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);

        
        List<Match> matches = Match.getWorldCupMatches();

        System.out.println("==== WORLD CUP MATCH LIST ====");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + ". " + matches.get(i).matchName);
        }

        System.out.print("\nChoose match (1-12): ");
        int choice = sc.nextInt();
        Match selectedMatch = matches.get(choice - 1);

        System.out.println("\nYou selected: " + selectedMatch.matchName);

        
        Scoreboard sb = new Scoreboard(selectedMatch);
        sb.startMatch();

	}
}
