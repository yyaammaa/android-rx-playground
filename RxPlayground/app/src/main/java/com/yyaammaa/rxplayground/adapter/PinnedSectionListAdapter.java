package com.yyaammaa.rxplayground.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yyaammaa.rxplayground.texture.Section;
import com.yyaammaa.rxplayground.view.PinnedSectionListView;
import com.yyaammaa.rxplayground.viewholder.SectionTextItemViewHolder;
import com.yyaammaa.rxplayground.viewholder.TrackItemViewHolder;
import com.yyaammaa.rxplayground.wasabeat.model.Track;

import java.util.ArrayList;
import java.util.List;

public class PinnedSectionListAdapter extends BaseAdapter
    implements PinnedSectionListView.PinnedSectionListAdapter {

  public enum Type {
    SONG, TEXT
  }

  private final Context mContext;

  private List<Object> mItems;

  public PinnedSectionListAdapter(Context context) {
    super();

    mContext = context;
    mItems = new ArrayList<>();
  }

  @Override
  public boolean isItemViewTypePinned(int viewType) {
    return viewType == Type.SONG.ordinal();
  }

  @Override
  public int getViewTypeCount() {
    return Type.values().length;
  }

  @Override
  public int getItemViewType(int position) {
    Object item = mItems.get(position);
    if (item instanceof Track) {
      return Type.SONG.ordinal();
    }
    return Type.TEXT.ordinal();
  }

  @Override
  public int getCount() {
    return mItems.size();
  }

  @Override
  public Object getItem(int position) {
    return mItems.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Object item = mItems.get(position);
    if (item instanceof Track) {
      TrackItemViewHolder trackItemViewHolder;
      if (convertView == null) {
        trackItemViewHolder = new TrackItemViewHolder(mContext);
        convertView = trackItemViewHolder.getRootView();
        convertView.setTag(trackItemViewHolder);
      } else {
        trackItemViewHolder = (TrackItemViewHolder) convertView.getTag();
      }
      trackItemViewHolder.bind((Track) item);
    } else {
      SectionTextItemViewHolder sectionTextItemViewHolder;
      if (convertView == null) {
        sectionTextItemViewHolder = new SectionTextItemViewHolder(mContext);
        convertView = sectionTextItemViewHolder.getRootView();
        convertView.setTag(sectionTextItemViewHolder);
      } else {
        sectionTextItemViewHolder = (SectionTextItemViewHolder) convertView.getTag();
      }
      sectionTextItemViewHolder.bind((Section) item);
    }

    return convertView;
  }

  public void addAll(List<Section> sections) {
    mItems.clear();
    for (Section sec : sections) {
      mItems.add(sec.track);
      mItems.add(sec);
    }
    notifyDataSetChanged();
  }
}
