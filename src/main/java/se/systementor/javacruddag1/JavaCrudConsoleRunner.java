package se.systementor.javacruddag1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import se.systementor.javacruddag1.models.Player;
import se.systementor.javacruddag1.models.Team;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

@SpringBootApplication
public class JavaCrudConsoleRunner implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(JavaCrudConsoleRunner.class);

    @Override
    public void run(String... args) throws Exception {
        var scan = new Scanner(System.in);
        var teams = new ArrayList<Team>();
        while(true){
           showMenu();
           System.out.print("Action:");
           int sel = Integer.parseInt ( scan.nextLine() );
           if(sel == 1) listAllTeams(teams);
           else if(sel == 2) teams.add(createTeam(scan));
           else if(sel == 3) changeTeam(teams, scan);
           else if(sel == 4) seeTeam(teams, scan);
           else if(sel == 9) break;
           else System.out.println("Invalid input, 1-4 or 9 please");
        }
    }

    private void seeTeam(ArrayList<Team> teams, Scanner scan) {
        int num = 1;
        System.out.println("*** SEE TEAM ***");
        for(Team team:teams) {
            System.out.println(String.format("%d. %s ", num,team.getName()));
            num++;
        }
        System.out.print("Select a teamnumber:");
        int sel = Integer.parseInt ( scan.nextLine() );;
        var t = teams.get(sel-1);
        adminTeam(t, scan);


    }

    private void adminTeam(Team t, Scanner scan) {
        while(true) {
            System.out.println("*** TEAM INFO ***");
            System.out.println(t.getName());
            System.out.println("*** Players ***");
            for (Player p : t.GetPlayers()) {
                System.out.println(p.getName());

            }

            showTeamMenu();
            System.out.print("Action:");
            int sel = Integer.parseInt ( scan.nextLine() );
            if(sel == 9) break;
            if(sel == 1) t.addPlayer(createPlayer(t, scan));
            if(sel == 2) editPlayer(t, scan);


        }
    }

    private void editPlayer(Team t, Scanner scan) {
    }

    private Player createPlayer(Team t, Scanner scan) {
        System.out.println("*** CREATE PLAYER ***");
        System.out.print("Namn:");
        String namn = scan.nextLine();
        System.out.print("Founded year:");
        var team = new Player(UUID.randomUUID());
        team.setName(namn);
        return team;

    }

    private void changeTeam(ArrayList<Team> teams, Scanner scan) {
        int num = 1;
        System.out.println("*** UPDATE TEAM ***");
        for(Team team:teams) {
            System.out.println(String.format("%d. %s ", num,team.getName()));
            num++;
        }
        System.out.print("Select a teamnumber:");
        int sel = Integer.parseInt ( scan.nextLine() );;
        var t = teams.get(sel-1);
        System.out.println(String.format("Name: %s", t.getName()));
        System.out.print("Leave blank if no change:");
        String temp = scan.nextLine();
        if(temp.length() > 0)
            t.setName(temp);

        System.out.println(String.format("Founded: %d", t.getFoundedYear()));
        System.out.print("Leave blank if no change:");
        temp = scan.nextLine();
        if(temp.length() > 0)
            t.setFoundedYear(Integer.parseInt(temp));

        System.out.println(String.format("City: %s", t.getCity()));
        System.out.print("Leave blank if no change:");
        temp = scan.nextLine();
        if(temp.length() > 0)
            t.setCity(temp);
    }

    private Team createTeam(Scanner scan) {
        System.out.println("*** CREATE TEAM ***");
        System.out.print("Namn:");
        String namn = scan.nextLine();
        System.out.print("Founded year:");
        int year = Integer.parseInt ( scan.nextLine() );
        System.out.print("Stad:");
        String city = scan.nextLine();
        var team = new Team(UUID.randomUUID(),namn);
        team.setCity(city);
        team.setFoundedYear(year);
        return team;
    }

    private void listAllTeams(ArrayList<Team> teams) {
        System.out.println("*** LIST OF TEAMS ***");
        for(Team team:teams) {
            System.out.println(String.format("%s, %s Grundat:%d", team.getName(), team.getCity(), team.getFoundedYear()));
        }
    }

    private void showTeamMenu() {
        System.out.println("1. Add player");
        System.out.println("2. Change player");
        System.out.println("9. Exit");
    }


    private void showMenu() {
        System.out.println("1. Lista alla lag");
        System.out.println("2. Skapa lag");
        System.out.println("3. Ändra lag");
        System.out.println("4. Se lag");
        System.out.println("9. Exit");
   }
}
