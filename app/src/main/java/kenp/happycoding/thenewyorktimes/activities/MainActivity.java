package kenp.happycoding.thenewyorktimes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import kenp.happycoding.thenewyorktimes.R;
import kenp.happycoding.thenewyorktimes.adapters.ArticleAdapter;
import kenp.happycoding.thenewyorktimes.api.SearchArticleResponse;
import kenp.happycoding.thenewyorktimes.api.TheNewYorkTimeService;
import kenp.happycoding.thenewyorktimes.models.Article;
import kenp.happycoding.thenewyorktimes.models.SearchQuery;
import kenp.happycoding.thenewyorktimes.utils.EndlessRecyclerViewScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.recycler_view_articles) RecyclerView mRecyclerViewArticles;

  @BindView(R.id.progressBar) ProgressBar progressBar;

  private TheNewYorkTimeService theNewYorkTimeService;
  private ArticleAdapter adapter;
  private SearchQuery searchQuery = new SearchQuery();

  private StaggeredGridLayoutManager layoutManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);

    getSupportActionBar().setIcon(R.drawable.nytimes_little_logo);
    getSupportActionBar().setDisplayUseLogoEnabled(true);

    adapter = new ArticleAdapter(this, new ArrayList<Article>());
    adapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
      @Override public void onClick(View itemView, Article article) {
        Intent intent = new Intent(MainActivity.this, ArticleDetailActivity.class);
        intent.putExtra("WEB_URL", article.webUrl);
        intent.putExtra("TITLE", article.headline.main);

        startActivity(intent);
      }
    });

    layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    mRecyclerViewArticles.setAdapter(adapter);
    mRecyclerViewArticles.setLayoutManager(layoutManager);

    mRecyclerViewArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
      @Override public void onLoadMore(int page, int totalItemsCount) {
        searchQuery.setPage(page);
        fetchArticles();
      }
    });

    theNewYorkTimeService = TheNewYorkTimeService.Creator.getService();

    fetchArticles();
  }

  private void fetchArticles() {
    theNewYorkTimeService.searchArticle(searchQuery.getQuery(), searchQuery.getBeginDateString(),
        searchQuery.getSortOrder(), searchQuery.getNewsdesk(), searchQuery.getPage())
        .enqueue(new Callback<SearchArticleResponse>() {
          @Override public void onResponse(Call<SearchArticleResponse> call,
              Response<SearchArticleResponse> response) {
            onDataResponse(response);
          }

          @Override public void onFailure(Call<SearchArticleResponse> call, Throwable t) {

          }
        });
  }

  private void onDataResponse(Response<SearchArticleResponse> response) {

    progressBar.setVisibility(View.GONE);

    if (response.isSuccessful()) {

      if (response.body().response.articles.size() != 0) {
        if (searchQuery.getPage() == 0) adapter.setArticles(response.body().response.articles);
        else adapter.addArticles(response.body().response.articles);
        adapter.notifyDataSetChanged();
      } else {
        Toast.makeText(this, "No article match your search!", Toast.LENGTH_SHORT).show();
      }

    } else {
      Toast.makeText(this, "Oops! API rate limit exceeded!", Toast.LENGTH_SHORT).show();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main, menu);

    MenuItem searchItem = menu.findItem(R.id.menu_item_search);
    SearchView searchView = (SearchView) searchItem.getActionView();
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        searchQuery.setPage(0);
        searchQuery.setQuery(query);

        adapter.clear();
        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.VISIBLE);

        fetchArticles();
        return true;
      }

      @Override public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_filter:

        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("searchquery", searchQuery);

        startActivityForResult(intent, 1);
        return true;
      default:
        return false;
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 1 && resultCode == RESULT_OK) {
      searchQuery = data.getParcelableExtra("searchquery");

      searchQuery.setPage(0);
      fetchArticles();
    }
  }
}
