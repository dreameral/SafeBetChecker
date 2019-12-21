import java.util.ArrayList;
import java.util.Date;

public class Event {
  private final String home;
  private final String away;
  private final Date startTime;

  private League league;
  private Sport sport;
  private ArrayList<Site> sites;

  public Event(String home, String away, Date startTime) {
    this.home = home;
    this.away = away;
    this.startTime = startTime;
    sites = new ArrayList<Site>();
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

  public void setSport(Sport sport) {
    this.sport = sport;
  }

  public String getHome() {
    return home;
  }

  public String getAway() {
    return away;
  }

  public Date getStartTime() {
    return startTime;
  }

  public int getSitesCount() {return sites.size();}

  public Site getSite(int index) {
    return sites.get(index);
  }
}
