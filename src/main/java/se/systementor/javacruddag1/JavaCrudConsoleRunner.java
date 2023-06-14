package se.systementor.javacruddag1;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JsonParserFactory;
import se.systementor.javacruddag1.models.Player;
import se.systementor.javacruddag1.models.Team;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@SpringBootApplication
public class JavaCrudConsoleRunner implements CommandLineRunner {

    private static Logger LOG = LoggerFactory
            .getLogger(JavaCrudConsoleRunner.class);

    @Override
    public void run(String... args) throws Exception {

        jsonDOM();

        var scan = new Scanner(System.in);
        var teams = ReadTeams();


        var team =  teams.get(0);
        TestJsonFactory(team);


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
        WriteTeams(teams);

    }

    void explore(JsonNode node){
        if(node.isArray())
        {
            System.out.println("Array");
            for (Iterator<JsonNode> it = node.iterator(); it.hasNext(); ) {
                var n = it.next();
                explore(n);
            }
        }

        var fields = node.fields();
        while( fields.hasNext()){
            var mapField =  fields.next();
            String fieldName =  mapField.getKey();
            JsonNode value =  mapField.getValue();
            System.out.println(fieldName);
            if(value.isObject())
                explore(value);
            else
                System.out.println(value.asText());
        }

    }

    private void jsonDOM() throws IOException {
        var objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(new URL("https://random-data-api.com/api/v2/users?size=10"));
        explore(node);

    }

    private void TestJsonFactory(Team team) throws IOException {
        var p2 =  new Player(UUID.randomUUID());
        p2.setName("Foppa");
        team.addPlayer(p2);
        p2 =  new Player(UUID.randomUUID());
        p2.setName("Stefan");
        team.addPlayer(p2);

        var jsonFactory = new JsonFactory();
        var generator = jsonFactory.createGenerator(System.out);
        generator.setPrettyPrinter(new DefaultPrettyPrinter());
        generator.writeStartObject();
        generator.writeStringField("name", team.getName());
        generator.writeStringField("city", team.getCity());
        generator.writeNumberField("foundedYear", team.getFoundedYear());


        generator.writeFieldName("players");
        generator.writeStartArray();
        for(var p: team.GetPlayers()){
            generator.writeStartObject();
            generator.writeStringField("id", p.getId().toString());
            generator.writeStringField("name", p.getName());
            generator.writeEndObject();
        }
        generator.writeEndArray();



            generator.writeFieldName("extra");
            generator.writeStartObject();
            generator.writeStringField("another", "Test");
            generator.writeNumberField("salary", 1112.123);
            generator.writeEndObject();

        generator.writeEndObject();
        generator.flush();
    }

    private List<Team> ReadTeams() throws IOException {
        if(!Files.exists(Path.of("hockey.json"))) return new ArrayList<Team>();
        ObjectMapper mapper = new ObjectMapper();
        var jsonStr = Files.readString(Path.of("hockey.json"));
        return  new ArrayList(Arrays.asList(mapper.readValue(jsonStr, Team[].class ) ));
    }

    private void WriteTeams(List<Team> teams) throws IOException {
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);


        StringWriter stringWriter = new StringWriter();
        objMapper.writeValue(stringWriter, teams);

        Files.writeString(Path.of("hockey.json"), stringWriter.toString());

    }

    private void seeTeam(List<Team> teams, Scanner scan) {
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

    private void changeTeam(List<Team> teams, Scanner scan) {
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

    private void listAllTeams(List<Team> teams) {
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
