package kenp.happycoding.thenewyorktimes.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import kenp.happycoding.thenewyorktimes.models.Article;

/**
 * Created by kenp on 25/02/2017.
 */

public class SearchArticleResponse {
  @SerializedName("response")
  public ArticleResponse response;

  public class ArticleResponse {
    @SerializedName("docs")
    public List<Article> articles;
  }
}
