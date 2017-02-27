package kenp.happycoding.thenewyorktimes.api;

import java.io.IOException;
import java.net.HttpURLConnection;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static kenp.happycoding.thenewyorktimes.api.TheNewYorkTimeService.Creator.getInterceptor;

/**
 * Created by kenp on 25/02/2017.
 */

public interface TheNewYorkTimeService {
  @GET("articlesearch.json")
  Call<SearchArticleResponse> searchArticle(@Query("q") String query, @Query("begin_date") String beginDate,
        @Query("sort") String sort, @Query("fq") String newDesk, @Query("page") int page);

  public static class Creator {
    public static final String BASE_URL = "https://api.nytimes.com/svc/search/v2/";
    public static final String API_KEY = "ba5f485319d24f3685d3462d0ff1af81";

    public static Retrofit retrofit;

    public static TheNewYorkTimeService getService() {

      if (retrofit == null) {
        retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build();
      }

      return retrofit.create(TheNewYorkTimeService.class);
    }

    public static Interceptor getInterceptor() {
      return new Interceptor() {
        @Override public Response intercept(Chain chain) throws IOException {
          Request request = chain.request();

          HttpUrl url = request.url()
              .newBuilder()
              .addQueryParameter("api-key", API_KEY)
              .build();

          request = request.newBuilder()
              .url(url)
              .build();

          return chain.proceed(request);
        }
      };
    }

    public static OkHttpClient getClient() {
      return new OkHttpClient.Builder()
          .addInterceptor(getInterceptor())
          .build();
    }
  }
}
