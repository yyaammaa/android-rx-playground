package com.yyaammaa.rxplayground.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 16:9のFrameLayout
 */
public class HdFrameLayout extends FrameLayout {

  public HdFrameLayout(final Context context) {
    super(context);
  }

  public HdFrameLayout(final Context context, final AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(int widthSpec, int heightSpec) {
    int previewWidth = MeasureSpec.getSize(widthSpec);
    int previewHeight = MeasureSpec.getSize(heightSpec);
    int hPadding = getPaddingLeft() + getPaddingRight();
    int vPadding = getPaddingTop() + getPaddingBottom();

    // Resize the preview frame with correct aspect ratio.
    previewWidth -= hPadding;
    previewHeight -= vPadding;

    previewHeight = (int) (previewWidth * 9f / 16f);

    // Add the padding of the border.
    previewWidth += hPadding;
    previewHeight += vPadding;

    // Ask children to follow the new preview dimension.
    super.onMeasure(
        MeasureSpec.makeMeasureSpec(previewWidth, MeasureSpec.EXACTLY),
        MeasureSpec.makeMeasureSpec(previewHeight, MeasureSpec.EXACTLY));
  }
}
