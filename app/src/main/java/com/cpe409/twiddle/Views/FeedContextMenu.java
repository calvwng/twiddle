package com.cpe409.twiddle.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cpe409.twiddle.R;
import com.cpe409.twiddle.shared.UtilHelper;

/**
 * Created by Michael on 2/24/2015.
 */
public class FeedContextMenu extends LinearLayout {
  private final int CONTEXT_MENU_WIDTH = UtilHelper.dpToPx(240);
  private int feedItem = -1;

  private Button reportButton;
  private Button shareButton;
  private Button favoritesButton;
  private Button cancelButton;

  private OnFeedContextMenuItemClickListener onItemClickListener;

  public FeedContextMenu(Context context) {
    super(context);
    init();
  }

  private void init() {
    View v = LayoutInflater.from(getContext()).inflate(R.layout.view_context_menu, this, true);
    reportButton = (Button) v.findViewById(R.id.btnReport);
    shareButton = (Button) v.findViewById(R.id.btnShare);
    favoritesButton = (Button) v.findViewById(R.id.btnFavorite);
    cancelButton = (Button) v.findViewById(R.id.btnCancel);

    reportButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (onItemClickListener != null) {
          onItemClickListener.onReportClick(feedItem);
        }
      }
    });

    shareButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (onItemClickListener != null) {
          onItemClickListener.onShareClick(feedItem);
        }
      }
    });

    favoritesButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (onItemClickListener != null) {
          onItemClickListener.onFavoritesClick(feedItem);
        }
      }
    });

    cancelButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (onItemClickListener != null) {
          onItemClickListener.onCancelClick(feedItem);
        }
      }
    });

    setBackgroundResource(R.drawable.bg_container_shadow);
    setOrientation(VERTICAL);
    setLayoutParams(new LayoutParams(CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
  }

  public void bindToItem(int feedItem) {
    this.feedItem = feedItem;
  }

  public void dismiss() {
    ((ViewGroup) getParent()).removeView(FeedContextMenu.this);
  }


  public void setOnFeedMenuItemClickListener(OnFeedContextMenuItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public interface OnFeedContextMenuItemClickListener {
    public void onReportClick(int feedItem);

    public void onShareClick(int feedItem);

    public void onFavoritesClick(int feedItem);

    public void onCancelClick(int feedItem);
  }
}