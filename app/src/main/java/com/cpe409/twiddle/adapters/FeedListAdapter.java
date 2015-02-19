package com.cpe409.twiddle.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Michael on 2/17/2015.
 * View and animations pulled from:
 * http://frogermcs.github.io/InstaMaterial-concept-part-5-like_action_effects/
 */
public class FeedListAdapter extends BaseAdapter {

  private Context context;
  private List<Feed> feedList;

  private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
  private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
  private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
  private static final String TAG = FeedListAdapter.class.getSimpleName();

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


    holder.likeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!CurrentUser.getInstance().isLoggedIn()) {
          Toast.makeText(context, "Please log in before liking.", Toast.LENGTH_SHORT).show();
          return;
        }

        Feed feed = holder.feed;

        // Like the feed
        if (!feed.getIsLiked()) {
          likeFeed(feed);
          feed.setLikesCount(feed.getLikesCount() + 1);
          feed.setLiked(true);
          holder.likesCount.setText(feed.getLikesCount() + " likes");
          animateHeartButton(holder);
          animatePhotoLike(holder);
        } // Unlike the feed
        else {
          unlikeFeed(feed);
          feed.setLikesCount(feed.getLikesCount() - 1);
          feed.setLiked(false);
          holder.likesCount.setCurrentText(feed.getLikesCount() + " likes");
          holder.likeButton.setImageResource(R.drawable.ic_heart_outline_grey);
        }
      }
    });

    if (feed.getIsLiked()) {
      holder.likeButton.setImageResource(R.drawable.ic_heart_red);
    } else {
      holder.likeButton.setImageResource(R.drawable.ic_heart_outline_grey);
    }

    holder.likesCount.setCurrentText(feed.getLikesCount() + " likes");
    return view;
  }

  private void animateHeartButton(final ViewHolder holder) {
    AnimatorSet animatorSet = new AnimatorSet();

    ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.likeButton, "rotation", 0f, 360f);
    rotationAnim.setDuration(300);
    rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

    ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.likeButton, "scaleX", 0.2f, 1f);
    bounceAnimX.setDuration(300);
    bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

    ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.likeButton, "scaleY", 0.2f, 1f);
    bounceAnimY.setDuration(300);
    bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
    bounceAnimY.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationStart(Animator animation) {
        holder.likeButton.setImageResource(R.drawable.ic_heart_red);
      }
    });

    animatorSet.play(rotationAnim);
    animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        resetLikeAnimationState(holder);
      }
    });

    animatorSet.start();
  }

  private void animatePhotoLike(final ViewHolder holder) {
    holder.feedBgLike.setVisibility(View.VISIBLE);
    holder.feedLike.setVisibility(View.VISIBLE);

    holder.feedBgLike.setScaleY(0.1f);
    holder.feedBgLike.setScaleX(0.1f);
    holder.feedBgLike.setAlpha(1f);
    holder.feedLike.setScaleY(0.1f);
    holder.feedLike.setScaleX(0.1f);

    AnimatorSet animatorSet = new AnimatorSet();

    ObjectAnimator bgScaleYAnim = ObjectAnimator.ofFloat(holder.feedBgLike, "scaleY", 0.1f, 1f);
    bgScaleYAnim.setDuration(200);
    bgScaleYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
    ObjectAnimator bgScaleXAnim = ObjectAnimator.ofFloat(holder.feedBgLike, "scaleX", 0.1f, 1f);
    bgScaleXAnim.setDuration(200);
    bgScaleXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
    ObjectAnimator bgAlphaAnim = ObjectAnimator.ofFloat(holder.feedBgLike, "alpha", 1f, 0f);
    bgAlphaAnim.setDuration(200);
    bgAlphaAnim.setStartDelay(150);
    bgAlphaAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

    ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(holder.feedLike, "scaleY", 0.1f, 1f);
    imgScaleUpYAnim.setDuration(300);
    imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
    ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(holder.feedLike, "scaleX", 0.1f, 1f);
    imgScaleUpXAnim.setDuration(300);
    imgScaleUpXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

    ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(holder.feedLike, "scaleY", 1f, 0f);
    imgScaleDownYAnim.setDuration(300);
    imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
    ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(holder.feedLike, "scaleX", 1f, 0f);
    imgScaleDownXAnim.setDuration(300);
    imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

    animatorSet.playTogether(bgScaleYAnim, bgScaleXAnim, bgAlphaAnim, imgScaleUpYAnim, imgScaleUpXAnim);
    animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(imgScaleUpYAnim);

    animatorSet.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        resetLikeAnimationState(holder);
      }
    });
    animatorSet.start();
  }

  private void resetLikeAnimationState(ViewHolder holder) {
    holder.feedBgLike.setVisibility(View.GONE);
    holder.feedLike.setVisibility(View.GONE);
  }

  //TODO: Create cloud code that handles these two queries into one query.
  private void likeFeed(Feed feed){
    ParseObject like = new ParseObject("Like");
    like.put("user", ParseUser.getCurrentUser());
    like.put("adventureId", feed.getObjId());
    like.saveInBackground();

    ParseQuery<ParseObject> query = ParseQuery.getQuery("Adventure");
    query.getInBackground(feed.getObjId(), new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject parseObject, ParseException e) {
        parseObject.put("likes", parseObject.getInt("likes") + 1);
        parseObject.saveInBackground();
      }
    });
  }

  //TODO: Create cloud code that handles these two queries into one query.
  private void unlikeFeed(Feed feed) {
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
    query.whereEqualTo("user", ParseUser.getCurrentUser());
    query.whereEqualTo("adventureId", feed.getObjId());
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        for (ParseObject obj : parseObjects) {
          obj.deleteInBackground();
        }
      }
    });

    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Adventure");
    query2.getInBackground(feed.getObjId(), new GetCallback<ParseObject>() {
      @Override
      public void done(ParseObject parseObject, ParseException e) {
        parseObject.put("likes", parseObject.getInt("likes") - 1);
        parseObject.saveInBackground();
      }
    });
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


