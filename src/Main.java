import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

public class Main {

  public static void main(String[] args) throws IOException {

    Utilities.setProxy();

    URL url = new URL(StaticVairables.BASE_URL);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    connection.setRequestMethod("GET");

    JSONArray jsonArray = new JSONArray(executeGetRequest());

    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);

      JSONObject leagueObject = jsonObject.getJSONObject("league");
      League league = new League(leagueObject.getString("key"), leagueObject.getString("name"));

      JSONObject sportObject = jsonObject.getJSONObject("sport");
      Sport sport = new Sport(sportObject.getString("key"), sportObject.getString("name"));

      JSONObject eventObject = jsonObject.getJSONObject("event");
      Event event = new Event(eventObject.getString("home"), eventObject.getString("away"), eventObject.getString("start_time"));
      event.setLeague(league);
      event.setSport(sport);

      JSONObject sitesObject = jsonObject.getJSONObject("sites");

      if (!sitesObject.has(StaticVairables.NODE_NAME_1X2))
        continue;

      JSONObject node1x2 = sitesObject.getJSONObject(StaticVairables.NODE_NAME_1X2);

      Set bookmakers = node1x2.keySet();
      Iterator it = bookmakers.iterator();

      while (it.hasNext()) {
        String key = (String) it.next();

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

      Utilities.calculateEvent(event);
    }

  }

  public static String executeGetRequest() throws IOException {
    StringBuilder response = new StringBuilder();

    URL url = new URL(StaticVairables.BASE_URL);
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
