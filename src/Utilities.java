import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Utilities {

  public static void setProxy() {
    System.setProperty("http.proxyHost", "127.0.0.1");
    System.setProperty("https.proxyHost", "127.0.0.1");
    System.setProperty("http.proxyPort", "8888");
    System.setProperty("https.proxyPort", "8888");
  }

  public static void calculateEvent(Event event) {
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
      weCanWin(event, site1, site2, site3);
    }

    // case2: site1 - 1, site2 - 2, site3 - x
    if (calculateOdds(site1.getHomeWins(), site2.getAwayWins(), site3.getX())) {
      weCanWin(event, site1, site3, site2);
    }

    //case3: site1 - x, site2 - 1, site3 - 2
    if (calculateOdds(site1.getX(), site2.getHomeWins(), site3.getAwayWins())) {
      weCanWin(event, site2, site1, site3);
    }

    // case4: site1 - x, site2 - 2, site3 - 1
    if (calculateOdds(site1.getX(), site2.getAwayWins(), site3.getHomeWins())) {
      weCanWin(event, site3, site1, site2);
    }

    //case5: site1 - 2, site2 - 1, site3 - x
    if (calculateOdds(site1.getAwayWins(), site2.getHomeWins(), site3.getX())) {
      weCanWin(event, site2, site3, site1);
    }

    //case6: site1 - 2, site2 - x, site3 - 1
    if (calculateOdds(site1.getAwayWins(), site2.getX(), site3.getHomeWins())) {
      weCanWin(event, site3, site2, site1);
    }
  }

  public static boolean calculateOdds(double odd1, double odd2, double odd3) {
    double prob1 = 1 / odd1;
    double prob2 = 1 / odd2;
    double prob3 = 1 / odd3;

    double sum = prob1 + prob2 + prob3;
    return sum < 1;
  }

  public static void weCanWin(Event event, Site homeWinsSite, Site xSite, Site awayWinsSite) {
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

}
