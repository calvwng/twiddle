package com.cpe409.twiddle.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpe409.twiddle.R;

public class LoginPagerAdapter extends PagerAdapter {
  private Context context_;

  private static String[] viewpageTexts = {
      "Easily discover new adventures on the go",
      "Share your adventures with the world",
      "Experience your adventures with friends & family"};
  private static String[] viewpageTitles = {"Explore",
      "Share",
      "Adventure"};
  private static int[] viewpageIcons = new int[]{R.drawable.ic_action_explore, R.drawable.ic_social_share,
      R.drawable.ic_maps_place};
  private static int[] viewpageBackgrounds = new int[]{R.drawable.bubbleball,
      R.drawable.beach, R.drawable.adventure};

  public LoginPagerAdapter(Context context) {
    this.context_ = context;
  }

  @Override
  public int getCount() {
    return 3;
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == ((RelativeLayout) object);
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    LayoutInflater inflater = (LayoutInflater) context_
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View itemView = inflater.inflate(R.layout.viewpager_item, container, false);

    TextView title_text = (TextView) itemView.findViewById(R.id.information_title);
    TextView desc_text = (TextView) itemView.findViewById(R.id.page_information);
    ImageView icon = (ImageView) itemView.findViewById(R.id.page_icon);
    ImageView background = (ImageView) itemView.findViewById(R.id.page_background);

    title_text.setText(viewpageTitles[position]);
    desc_text.setText(viewpageTexts[position]);
    icon.setImageResource(viewpageIcons[position]);
    background.setImageResource(viewpageBackgrounds[position]);

    ((ViewPager) container).addView(itemView);
    return itemView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    // Remove viewpager_item.xml from ViewPager
    ((ViewPager) container).removeView((RelativeLayout) object);
  }

}