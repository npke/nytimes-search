package kenp.happycoding.thenewyorktimes.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Article {
  @SerializedName("web_url")
  public String webUrl;

  @SerializedName("snippet")
  public String snippet;

  @SerializedName("headline")
  public Headline headline;

  @SerializedName("multimedia")
  public List<Multimedia> multimedias;

  public class Headline {
    public String main;
    public String name;
  }

  public class Multimedia {
    public String url;
    public String type;
  }
}
