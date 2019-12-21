public class Odds {
  private double homeWins;
  private double x;
  private double awayWins;

  public Odds(double homeWins, double x, double awayWins) {
    this.homeWins = homeWins;
    this.awayWins = awayWins;
    this.x = x;
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
