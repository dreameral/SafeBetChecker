import java.util.ArrayList;
import java.util.Vector;

public class Event {
  private League league;
  private Sport sport;
  private ArrayList<Site> sites;
  private String home;
  private String away;
  private String startTime;

  public Event(String home, String away, String startTime) {
    this.home = home;
    this.away = away;
    this.startTime = startTime;
    sites = new ArrayList<>();
  }

  public void addSite(Site site) {
    sites.add(site);
  }

  public League getLeague() {
    return league;
  }

  public void setLeague(League league) {
    this.league = league;
  }

  public Sport getSport() {
    return sport;
  }

  public void setSport(Sport sport) {
    this.sport = sport;
  }

  public String getHome() {
    return home;
  }

  public void setHome(String home) {
    this.home = home;
  }

  public String getAway() {
    return away;
  }

  public void setAway(String away) {
    this.away = away;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public int getSitesCount() {return sites.size();}

  public Site getSite(int index) {
    return sites.get(index);
  }
}
