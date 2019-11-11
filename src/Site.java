public class Site {
  private String name;
  private Odds odds;

  public Site(String name, Odds odds) {
    this.name = name;
    this.odds = odds;
  }

  public double getHomeWins() {
    return odds.getHomeWins();
  }

  public double getX() {
    return odds.getX();
  }

  public double getAwayWins() {
    return odds.getAwayWins();
  }

  public String getName() {
    return name;
  }
}