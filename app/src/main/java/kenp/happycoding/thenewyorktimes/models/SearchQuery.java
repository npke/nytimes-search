package kenp.happycoding.thenewyorktimes.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kenp on 25/02/2017.
 */

public class SearchQuery implements Parcelable {

  public static int NEWEST = 0;
  public static int OLDEST = 1;

  private String query;
  private long beginDate;
  private int sortOrder;
  private boolean arts;
  private boolean fashionStyle;
  private boolean sports;
  private int page;

  public SearchQuery() {
    query = null;
    beginDate = 0;
    sortOrder = 0;
    page = 0;
  }

  protected SearchQuery(Parcel in) {
    query = in.readString();
    beginDate = in.readLong();
    sortOrder = in.readInt();
    arts = in.readByte() != 0;
    fashionStyle = in.readByte() != 0;
    sports = in.readByte() != 0;
    page = in.readInt();
  }

  public static final Creator<SearchQuery> CREATOR = new Creator<SearchQuery>() {
    @Override public SearchQuery createFromParcel(Parcel in) {
      return new SearchQuery(in);
    }

    @Override public SearchQuery[] newArray(int size) {
      return new SearchQuery[size];
    }
  };

  public String getQuery() {
    return query;
  }

  public String getBeginDateString() {
    if (beginDate == 0) return null;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    return dateFormat.format(new Date(beginDate));
  }

  public long getBeginDate() {
    return beginDate;
  }

  public String getSortOrder() {
    return sortOrder == NEWEST ? "newest" : "oldest";
  }

  public String getNewsdesk() {
    if (!arts && !sports && !fashionStyle) {
      return null;
    } else {
      String newsdesk = "news_desk:(";
      if (arts) newsdesk += "\"Arts\"";

      if (sports) {
        if (newsdesk.length() != "news_desk:(".length()) newsdesk += ", ";
        newsdesk += "\"Sport\"";
      }

      if (fashionStyle) {
        if (newsdesk.length() != "news_desk:(".length()) newsdesk += ", ";

        newsdesk += "\"Fashion & Style\"";
      }

      newsdesk += ")";

      return newsdesk;
    }
  }

  public boolean isArts() {
    return arts;
  }

  public boolean isFashionStyle() {
    return fashionStyle;
  }

  public boolean isSports() {
    return sports;
  }

  public void setBeginDate(long beginDate) {
    this.beginDate = beginDate;
  }

  public void setSortOrder(int sortOrder) {
    this.sortOrder = sortOrder;
  }

  public void setArts(boolean arts) {
    this.arts = arts;
  }

  public void setFashionStyle(boolean fashionStyle) {
    this.fashionStyle = fashionStyle;
  }

  public void setSports(boolean sports) {
    this.sports = sports;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public int getSortOrderInt() {
    return sortOrder;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(query);
    parcel.writeLong(beginDate);
    parcel.writeInt(sortOrder);
    parcel.writeByte((byte) (arts ? 1 : 0));
    parcel.writeByte((byte) (fashionStyle ? 1 : 0));
    parcel.writeByte((byte) (sports ? 1 : 0));
    parcel.writeInt(page);
  }
}
