package kenp.happycoding.thenewyorktimes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import java.util.List;
import kenp.happycoding.thenewyorktimes.R;
import kenp.happycoding.thenewyorktimes.models.Article;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int NO_THUMBNAIL = 0;
  private static final int NORMAL = 1;

  private List<Article> mArticles;
  private Context mContext;
  private OnItemClickListener mListener;

  public void clear() {
    mArticles.clear();
  }

  public interface OnItemClickListener {
    void onClick(View itemView, Article article);
  }

  public ArticleAdapter(Context context, List<Article> articles) {
    this.mContext = context;
    this.mArticles = articles;
  }

  public void setArticles(List<Article> articles) {
    this.mArticles = articles;
  }

  public void addArticles(List<Article> articles) {
    this.mArticles.addAll(articles);
  }

  public void setOnItemClickListener(OnItemClickListener listener) {
    this.mListener = listener;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view;
    RecyclerView.ViewHolder holder;

    if (viewType == NORMAL) {
      view = LayoutInflater.from(mContext).inflate(R.layout.item_article, parent, false);
      holder = new ViewHolder(view);
    }
    else {
      view = LayoutInflater.from(mContext).inflate(R.layout.item_article_no_thumbnail, parent, false);
      holder = new NoThumbnailViewHolder(view);
    }

    return holder;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    final Article article = mArticles.get(position);

    if (getItemViewType(position) == NORMAL) {
      bindNormalViewHolder((ViewHolder) holder, article);
    } else {
      bindNoThumbnailViewHolder((NoThumbnailViewHolder) holder, article);
    }
  }

  private void bindNoThumbnailViewHolder(NoThumbnailViewHolder holder, Article article) {
    holder.mSnippet.setText(article.snippet);
    holder.mTitle.setText(article.headline.main);
  }

  private void bindNormalViewHolder(ViewHolder holder, Article article) {
    Glide.with(mContext)
        .load("http://www.nytimes.com/" + article.multimedias.get(0).url)
        .into(holder.mPoster);

    holder.mTitle.setText(article.headline.main);
  }

  @Override public int getItemCount() {
    return mArticles.size();
  }

  @Override public int getItemViewType(int position) {
    Article article = mArticles.get(position);

    return article.multimedias.size() == 0 ? NO_THUMBNAIL : NORMAL;
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_view_poster) ImageView mPoster;

    @BindView(R.id.text_view_title) TextView mTitle;

    public ViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (mListener != null) {
            mListener.onClick(itemView, mArticles.get(getAdapterPosition()));
          }
        }
      });
    }
  }

  public class NoThumbnailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_view_snippet) TextView mSnippet;

    @BindView(R.id.text_view_title) TextView mTitle;

    public NoThumbnailViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
          if (mListener != null) {
            mListener.onClick(itemView, mArticles.get(getAdapterPosition()));
          }
        }
      });
    }
  }
}
