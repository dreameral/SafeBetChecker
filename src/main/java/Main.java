import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class Main {

  /**
   * @param args The arguments expected are, the ApiKey, and a list with the bookmakers user wants to search odds on.
   * @throws IOException
   */
  public static void main(String[] args) throws IOException, ParseException {
    if (args.length == 0) {
      System.out.println(StaticVariables.MISSING_ARGUMENTS);
      return;
    }

    String apiKey = args[0];
    String[] bookmakers = Utilities.removeElement(args, 0);

    boolean includeAll = false;

    if (bookmakers.length == 0) {
      System.out.println(StaticVariables.MISSING_ARGUMENTS);
      return;
    } else if (bookmakers[0].equals("*")) {
      includeAll = true;
    } else if (bookmakers.length < 3) {
      System.out.println(StaticVariables.MISSING_ARGUMENTS);
      return;
    }

    JSONArray jsonArray = new JSONArray(executeGetRequest(StaticVariables.BASE_URL + "?apikey=" + apiKey));
    ArrayList<Event> events = getEvents(jsonArray, bookmakers, includeAll);

    jsonArray = null;

    for (Event event : events) {
      if (event.getSitesCount() > 2) {
        // we need at least three bookmakers to calculate three possible results (1, X, 2)
        Utilities.calculateEvent1X2(event);
      }
    }
  }

  public static ArrayList<Event> getEvents(JSONArray jsonArray, String[] bookmakers, boolean includeAll) throws ParseException {
    ArrayList<Event> events = new ArrayList<>();

    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);

      JSONObject leagueObject = jsonObject.getJSONObject("league");
      League league = new League(leagueObject.getString("key"), leagueObject.getString("name"));

      JSONObject sportObject = jsonObject.getJSONObject("sport");
      Sport sport = new Sport(sportObject.getString("key"), sportObject.getString("name"));

      JSONObject eventObject = jsonObject.getJSONObject("event");
      Date startDate = Utilities.getDate(eventObject.getString("start_time"));
      if (!Utilities.isWithinDays(startDate, 7))
        continue;

      Event event = new Event(eventObject.getString("home"), eventObject.getString("away"), startDate);
      event.setLeague(league);
      event.setSport(sport);

      JSONObject sitesObject = jsonObject.getJSONObject("sites");

      if (!sitesObject.has(StaticVariables.NODE_NAME_1X2))
        continue;

      JSONObject node1x2 = sitesObject.getJSONObject(StaticVariables.NODE_NAME_1X2);

      Set sites = node1x2.keySet();
      Iterator it = sites.iterator();

      while (it.hasNext()) {
        String key = (String) it.next();

        if (!includeAll && Utilities.indexOf(bookmakers, key) == -1) {
          // continue in case the bookmaker found is not in the list of the bookmakers specified by the user.
          continue;
        }

        if (!(node1x2.get(key) instanceof JSONObject))
          continue;

        JSONObject siteObject = node1x2.getJSONObject(key);

        if (!siteObject.has("odds"))
          continue;

        JSONObject oddsObject = siteObject.getJSONObject("odds");

        if (!oddsObject.has("1") || !oddsObject.has("X") || !oddsObject.has("2")) {
          continue;
        }

        Odds odds = new Odds(oddsObject.getDouble("1"), oddsObject.getDouble("X"), oddsObject.getDouble("2"));
        Site site = new Site(siteObject.getString("name"), odds);

        event.addSite(site);
      }

      events.add(event);
    }

    return events;
  }

  public static String executeGetRequest(String urlString) throws IOException {
    StringBuilder response = new StringBuilder();

    URL url = new URL(urlString);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    String inputLine;
    boolean jsonTextStarted = false;
    while ((inputLine = in.readLine()) != null) {
      if (!jsonTextStarted && (inputLine.startsWith("{") || inputLine.startsWith("[")))
        jsonTextStarted = true;
      if (jsonTextStarted)
        response.append(inputLine);
    }
    in.close();

    return response.toString();
  }

}
