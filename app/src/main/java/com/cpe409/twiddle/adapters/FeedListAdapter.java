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
import android.widget.Toast;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.model.CurrentUser;
import com.cpe409.twiddle.model.Feed;
import com.cpe409.twiddle.views.RoundImageView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
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

    holder.likeButton.setOnClickListener(new LikeButtonOnClickListener(position));
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

  public class LikeButtonOnClickListener implements View.OnClickListener {
    private int position;

    public LikeButtonOnClickListener(int position) {
      this.position = position;
    }

    @Override
    public void onClick(View v) {
      // Cast v as ImageButton
      ImageButton likeButton = (ImageButton)v;

      // Get Feed/Adventure affected
      Feed feed = feedList.get(position);

      if (CurrentUser.getInstance().isLoggedIn()) {
        Toast.makeText(context, "Please log in before liking.", Toast.LENGTH_SHORT).show();
        return;
      }
      String fbId = CurrentUser.getInstance().getUserId();

      try {
        // Attempt to retrieve "Like" entry for this Adventure
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
        query.whereEqualTo("fbId", fbId);
        query.whereEqualTo("adventureId", feed.getObjId());
        ParseObject likeEntry = query.getFirst();

        // Remove the entry and decrease adventure's likesCount
        likeEntry.deleteInBackground();
        feed.offsetLikesCount(-1);
        // TODO: Switch to "empty" like button
        likeButton.setImageResource(R.drawable.ic_heart_outline_grey);
      }
      catch (ParseException e) {
        // Add new "Like" entry and increase adventure's likesCount
        ParseObject like = new ParseObject("Like");
        like.put("fbId", fbId);
        like.put("adventureId", feed.getObjId());
        like.saveInBackground();
        feed.offsetLikesCount(1);
        // TODO: Switch to "filled in" like button
        likeButton.setImageResource(R.drawable.ic_heart_small_blue);
      }

      Toast.makeText(context, "likeCount is " + feed.getLikesCount(), Toast.LENGTH_SHORT).show();
    }

    public void test() {}
  }
}


