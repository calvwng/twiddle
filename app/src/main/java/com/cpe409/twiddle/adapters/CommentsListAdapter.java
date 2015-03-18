package com.cpe409.twiddle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.model.Comment;
import com.cpe409.twiddle.model.FacebookUser;
import com.cpe409.twiddle.shared.CircleTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Calvin on 3/16/2015.
 */
public class CommentsListAdapter extends BaseAdapter {
  private Context context;
  private List<Comment> mComments;

  public CommentsListAdapter(Context context, List<Comment> comments) {
    this.context = context;
    mComments = comments;
  }

  @Override
  public int getCount() {
    return mComments.size();
  }

  @Override
  public Object getItem(int position) {
    return mComments.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    final ViewHolder holder;
    if (convertView == null) {
      view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
      holder = new ViewHolder();
      holder.profileIcon = (ImageView) view.findViewById(R.id.profile_icon);
      holder.text = (TextView) view.findViewById(R.id.comment_text);
      view.setTag(holder);
    } else {
      holder = (ViewHolder) view.getTag();
    }

    Comment comment = mComments.get(position);
    Picasso.with(context).load(FacebookUser.fbIdtoPhotoUrl(comment.getAuthor().getUserId()))
        .centerCrop().placeholder(R.drawable.ic_action_account_circle)
        .resize(R.dimen.comment_avatar_size, R.dimen.comment_avatar_size).transform(new CircleTransformation()).
        into(holder.profileIcon);
    holder.text.setText(comment.getText());
    return view;
  }

  private class ViewHolder {
    public ImageView profileIcon;
    public TextView name, text;
  }
}
