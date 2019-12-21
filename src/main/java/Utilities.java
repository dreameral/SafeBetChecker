import java.text.*;
import java.util.Calendar;
import java.util.Date;

public class Utilities {

  public static void setProxy() {
    System.setProperty("http.proxyHost", "127.0.0.1");
    System.setProperty("https.proxyHost", "127.0.0.1");
    System.setProperty("http.proxyPort", "8888");
    System.setProperty("https.proxyPort", "8888");
    System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\user\\Documents\\Ennio\\Other\\Fiddler\\FiddlerKeyStore");
    System.setProperty("javax.net.ssl.trustStorePassword", "123456");
  }

  public static void calculateEvent1X2(Event event) {
    int sitesCount = event.getSitesCount();
    for (int i = 0; i < sitesCount - 2; i++) {
      Site site1 = event.getSite(i);

      for (int j = i + 1; j < sitesCount - 1; j++) {
        Site site2 = event.getSite(j);

        for (int k = j + 1; k < sitesCount; k++) {
          Site site3 = event.getSite(k);

          checkSafeBet(event, site1, site2, site3);
        }
      }
    }
  }

  public static void checkSafeBet(Event event, Site site1, Site site2, Site site3) {
    // case1: site1 - 1, site2 - x, site3 - 2
    if (calculateOdds(site1.getHomeWins(), site2.getX(), site3.getAwayWins())) {
      printSafeBet(event, site1, site2, site3);
    }

    // case2: site1 - 1, site2 - 2, site3 - x
    if (calculateOdds(site1.getHomeWins(), site2.getAwayWins(), site3.getX())) {
      printSafeBet(event, site1, site3, site2);
    }

    //case3: site1 - x, site2 - 1, site3 - 2
    if (calculateOdds(site1.getX(), site2.getHomeWins(), site3.getAwayWins())) {
      printSafeBet(event, site2, site1, site3);
    }

    // case4: site1 - x, site2 - 2, site3 - 1
    if (calculateOdds(site1.getX(), site2.getAwayWins(), site3.getHomeWins())) {
      printSafeBet(event, site3, site1, site2);
    }

    //case5: site1 - 2, site2 - 1, site3 - x
    if (calculateOdds(site1.getAwayWins(), site2.getHomeWins(), site3.getX())) {
      printSafeBet(event, site2, site3, site1);
    }

    //case6: site1 - 2, site2 - x, site3 - 1
    if (calculateOdds(site1.getAwayWins(), site2.getX(), site3.getHomeWins())) {
      printSafeBet(event, site3, site2, site1);
    }
  }

  public static boolean calculateOdds(double odd1, double odd2, double odd3) {
    double prob1 = 1 / odd1;
    double prob2 = 1 / odd2;
    double prob3 = 1 / odd3;

    double sum = prob1 + prob2 + prob3;
    return sum < 1;
  }

  public static void printSafeBet(Event event, Site homeWinsSite, Site xSite, Site awayWinsSite) {
    double betOnHome = 10 / homeWinsSite.getHomeWins();
    double betOnX = 10 / xSite.getX();
    double betOnAway = 10 / awayWinsSite.getAwayWins();
    double totalBet = formatDouble(betOnHome + betOnX + betOnAway);

    double profitX = betOnX * xSite.getX() - totalBet;
    double profitHome = betOnHome * homeWinsSite.getHomeWins() - totalBet;
    double profitAway = betOnAway * awayWinsSite.getAwayWins() - totalBet;

    System.out.println("SAFE BET FOUND: ");
    System.out.println("LEAGUE: " + event.getLeague().getName());
    System.out.println("MATCH: " + event.getHome() + " - " + event.getAway());
    System.out.println("MATCHTIME: " + event.getStartTime());
    System.out.println("Bet that the home wins on bookmaker " + homeWinsSite.getName() + " : Bet " + formatDouble(betOnHome) + " and profits " + formatDouble(profitHome));
    System.out.println("Bet x on bookmaker " + xSite.getName() + " : Bet " + formatDouble(betOnX) + " and profits " + formatDouble(profitX));
    System.out.println("Bet that the away wins on bookmaker " + awayWinsSite.getName() + " : Bet " + formatDouble(betOnAway) + " and profits " + formatDouble(profitAway));
    System.out.println();
    System.out.println();
  }

  public static double formatDouble(double value) {
    NumberFormat formatter = new DecimalFormat("#0.00");
    return Double.parseDouble(formatter.format(value));
  }

  public static String[] removeElement(String[] actual, int index) {
    if (actual.length == 0)
      return actual;

    String[] retVal = new String[actual.length - 1];
    boolean found = false;

    for (int i = 0; i < actual.length; i++) {
      if (i == index) {
        found = true;
        continue;
      }

      retVal[found ? i - 1 : i] = actual[i];
    }

    return retVal;
  }

  public static int indexOf(String[] theArray, String value) {
    for (int i = 0; i < theArray.length; i++) {
      if (value.equalsIgnoreCase(theArray[i]))
        return i;
    }

    return -1;
  }

  public static Date getDate(String dateAsString) throws ParseException {
    if (dateAsString == null)
      return null;

    String date = dateAsString.substring(0, dateAsString.indexOf("T"));
    DateFormat dateFormat = new SimpleDateFormat(StaticVariables.DATE_FORMAT);

    Date retVal = dateFormat.parse(date);

    return retVal;
  }

  public static boolean isWithinDays(Date date, int days) {
    Date currentDate = new Date();
    if (currentDate.after(date)) {
      // this should not happen but just in case, we don't need a match that is played
      return false;
    }

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    calendar.add(Calendar.DATE, -days);
    Date updatedDate = calendar.getTime();

    return currentDate.after(updatedDate);
  }

}
