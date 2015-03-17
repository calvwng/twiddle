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

import java.util.List;

/**
 * Created by Calvin on 3/16/2015.
 */
public class CommentsListAdapter extends BaseAdapter {
  private LayoutInflater mInflater;
  private List<Comment> mComments;

  public CommentsListAdapter(Context context, List<Comment> comments) {
    mInflater = LayoutInflater.from(context);
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
    if(convertView == null) {
      view = mInflater.inflate(R.layout.comment_row_layout, parent, false);
      holder = new ViewHolder();
      holder.profileIcon = (ImageView)view.findViewById(R.id.profile_icon);
      holder.name = (TextView)view.findViewById(R.id.comment_author);
      holder.text = (TextView)view.findViewById(R.id.comment_text);
      view.setTag(holder);
    }
    else {
      holder = (ViewHolder)view.getTag();
    }

    Comment comment = mComments.get(position);
//    holder.profileIcon.setImageBitmap(comment.getProfileIconBitmap()); // Use Picasso instead
    holder.name.setText(comment.getAuthor().getName());
    holder.text.setText(comment.getText());

    return view;
  }

  private class ViewHolder {
    public ImageView profileIcon;
    public TextView name, text;
  }
}
