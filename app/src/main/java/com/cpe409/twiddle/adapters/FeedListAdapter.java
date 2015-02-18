package com.cpe409.twiddle.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.model.Feed;
import com.cpe409.twiddle.views.RoundImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Michael on 2/17/2015.
 */
public class FeedListAdapter extends BaseAdapter {

  private Context context;
  private List<Feed> feedList;

  public FeedListAdapter(Context context, List<Feed> feedList) {
    this.context = context;
    this.feedList = feedList;
  }

  @Override
  public int getCount() {
    return feedList.size();
  }

  @Override
  public Object getItem(int position) {
    return feedList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return feedList.indexOf(feedList.get(position));
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    final ViewHolder holder;
    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) context
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.item_feed, parent, false);
      holder = new ViewHolder();
      holder.authorImage = (RoundImageView) view.findViewById(R.id.feedUserImage);
      holder.authorName = (TextView) view.findViewById(R.id.feedUserName);
      holder.feedPicture = (ImageView) view.findViewById(R.id.feedPicture);
      holder.feedTitle = (TextView) view.findViewById(R.id.feedTitle);
      holder.feedBgLike = view.findViewById(R.id.feedBgLike);
      holder.feedLike = (ImageView) view.findViewById(R.id.feedLike);
      holder.likeButton = (ImageButton) view.findViewById(R.id.btnLike);
      holder.commentsButton = (ImageButton) view.findViewById(R.id.btnComments);
      holder.moreButton = (ImageButton) view.findViewById(R.id.btnMore);
      holder.likesCount = (TextSwitcher) view.findViewById(R.id.likesCounter);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }
    Feed feed = feedList.get(position);
    holder.feed = feed;
    Picasso.with(context).load(feed.getAuthor().getImageURL()).into(holder.authorImage);
    holder.authorName.setText(feed.getAuthor().getName());
    holder.feedTitle.setText(feed.getTitle());

    String imgUrl = feed.getImgUrl();
    if (imgUrl != null) {
      Picasso.with(context).load(imgUrl).into(holder.feedPicture);
    }

    holder.likesCount.setText(feed.getLikesCount() + " likes");

    return view;
  }


  static class ViewHolder {
    Feed feed;
    RoundImageView authorImage;
    TextView authorName;
    ImageView feedPicture;
    TextView feedTitle;
    View feedBgLike;
    ImageView feedLike;
    ImageButton likeButton;
    ImageButton commentsButton;
    ImageButton moreButton;
    TextSwitcher likesCount;
  }
}


