package com.cpe409.twiddle.shared;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;

import com.cpe409.twiddle.model.Feed;
import com.cpe409.twiddle.views.FeedContextMenu;


/**
 * Created by froger_mcs on 16.12.14.
 */
public class FeedContextMenuManager implements View.OnAttachStateChangeListener, ListView.OnScrollListener {

  private static FeedContextMenuManager instance;

  private FeedContextMenu contextMenuView;

  // ListView scroll speed
  private int previousFirstVisibleItem = 0;
  private long previousEventTime = 0;
  private float speed = 0;

  private boolean isContextMenuDismissing;
  private boolean isContextMenuShowing;

  public static FeedContextMenuManager getInstance() {
    if (instance == null) {
      instance = new FeedContextMenuManager();
    }
    return instance;
  }

  private FeedContextMenuManager() { }

  public void toggleContextMenuFromView(View openingView, Feed feed, FeedContextMenu.OnFeedContextMenuItemClickListener listener) {
    if (contextMenuView == null) {
      showContextMenuFromView(openingView, feed, listener);
    } else {
      hideContextMenu();
    }
  }

  private void showContextMenuFromView(final View openingView, Feed feed, FeedContextMenu.OnFeedContextMenuItemClickListener listener) {
    if (!isContextMenuShowing) {
      isContextMenuShowing = true;
      contextMenuView = new FeedContextMenu(openingView.getContext(), feed.isFavorited());
      contextMenuView.bindToItem(feed);
      contextMenuView.addOnAttachStateChangeListener(this);
      contextMenuView.setOnFeedMenuItemClickListener(listener);

      ((ViewGroup) openingView.getRootView().findViewById(android.R.id.content)).addView(contextMenuView);

      contextMenuView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
          contextMenuView.getViewTreeObserver().removeOnPreDrawListener(this);
          setupContextMenuInitialPosition(openingView);
          performShowAnimation();
          return false;
        }
      });
    }
  }

  private void setupContextMenuInitialPosition(View openingView) {
    final int[] openingViewLocation = new int[2];
    openingView.getLocationOnScreen(openingViewLocation);
    int additionalBottomMargin = UtilHelper.dpToPx(16);
    contextMenuView.setTranslationX(openingViewLocation[0] - contextMenuView.getWidth() / 3);
    contextMenuView.setTranslationY(openingViewLocation[1] - contextMenuView.getHeight() + additionalBottomMargin);
  }

  /**
   * Displays the context menu with animation
   */
  private void performShowAnimation() {
    contextMenuView.setPivotX(contextMenuView.getWidth() / 2);
    contextMenuView.setPivotY(contextMenuView.getHeight());
    contextMenuView.setScaleX(0.1f);
    contextMenuView.setScaleY(0.1f);
    contextMenuView.animate()
        .scaleX(1f).scaleY(1f)
        .setDuration(150)
        .setInterpolator(new OvershootInterpolator())
        .setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            isContextMenuShowing = false;
          }
        });
  }

  public void hideContextMenu() {
    if (!isContextMenuDismissing) {
      isContextMenuDismissing = true;
      performDismissAnimation();
    }
  }

  private void performDismissAnimation() {
    contextMenuView.setPivotX(contextMenuView.getWidth() / 2);
    contextMenuView.setPivotY(contextMenuView.getHeight());
    contextMenuView.animate()
        .scaleX(0.1f).scaleY(0.1f)
        .setDuration(150)
        .setInterpolator(new AccelerateInterpolator())
        .setStartDelay(100)
        .setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            if (contextMenuView != null) {
              contextMenuView.dismiss();
            }
            isContextMenuDismissing = false;
          }
        });
  }

  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    if (contextMenuView != null) {
      hideContextMenu();
      contextMenuView.setTranslationY(contextMenuView.getTranslationY() - dy);
    }
  }

  @Override
  public void onScrollStateChanged(AbsListView view, int scrollState) {

  }

  @Override
  public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    if (previousFirstVisibleItem != firstVisibleItem){
      long currTime = System.currentTimeMillis();
      long timeToScrollOneElement = currTime - previousEventTime;
      speed = ((float)1/timeToScrollOneElement)*1000;

      previousFirstVisibleItem = firstVisibleItem;
      previousEventTime = currTime;
    }

    if (contextMenuView != null) {
      hideContextMenu();
      contextMenuView.setTranslationY(contextMenuView.getTranslationY() - speed);
    }
  }

  @Override
  public void onViewAttachedToWindow(View v) {

  }

  @Override
  public void onViewDetachedFromWindow(View v) {
    contextMenuView = null;
  }
}