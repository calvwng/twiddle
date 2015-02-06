package com.cpe409.twiddle.adapters;

import com.cpe409.twiddle.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginPagerAdapter extends PagerAdapter {
  private Context context_;

  private static String[] viewpageTexts = {
      "Easily organize events on the go and keep everyone in the loop",
      "Watch your friends cast their votes in real time with real-time updates",
      "Vote amongst the group to filter and find the best activity for everyone" };
  private static String[] viewpageTitles = { "Plan events effortlessly", "Keep it real",
      "Everybody has a say" };
  private static int[] viewpageIcons = new int[] { R.drawable.ic_launcher ,R.drawable.ic_launcher,
      R.drawable.ic_launcher };
  private static int[] viewpageBackgrounds = new int[] { android.R.color.white,
      android.R.color.white, android.R.color.white };

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