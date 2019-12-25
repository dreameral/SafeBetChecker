public class Odds {
  private final double homeWins;
  private final double x;
  private final double awayWins;

  public Odds(double homeWins, double x, double awayWins) {
    this.homeWins = Utilities.formatDouble(homeWins);
    this.awayWins = Utilities.formatDouble(awayWins);
    this.x = Utilities.formatDouble(x);
  }

  public double getHomeWins() {
    return homeWins;
  }

  public double getAwayWins() {
    return awayWins;
  }

  public double getX() {
    return x;
  }
}
