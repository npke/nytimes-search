package kenp.happycoding.thenewyorktimes.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import kenp.happycoding.thenewyorktimes.R;

public class ArticleDetailActivity extends AppCompatActivity {

  @BindView(R.id.web_view)
  WebView webView;

  @BindView(R.id.progressBar)
  ProgressBar progressBar;

  String articleUrl, articleHeading;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_article_detail);

    ButterKnife.bind(this);

    Intent startIntent = getIntent();
    articleUrl = startIntent.getStringExtra("WEB_URL");
    articleHeading = startIntent.getStringExtra("TITLE");

    getSupportActionBar().setTitle(articleHeading);

    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    webView.setWebViewClient(new WebViewClient() {
      @Override
      public void onPageFinished(WebView view, String url) {
        progressBar.setVisibility(View.GONE);
      }

    });

    webView.loadUrl(articleUrl);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_webview, menu);

    MenuItem item = menu.findItem(R.id.menu_item_share);
    ShareActionProvider shareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");

    shareIntent.putExtra(Intent.EXTRA_TEXT, articleUrl);

    shareAction.setShareIntent(shareIntent);
    return true;
  }
}
