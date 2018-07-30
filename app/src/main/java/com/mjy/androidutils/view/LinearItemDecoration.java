package com.mjy.androidutils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by mjy.
 */

public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;
    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    private boolean mDecorationWillNotDraw;

    private final Rect mBounds = new Rect();

    private int mDrawablePaddingLeft = 0;
    private int mDrawablePaddingTop = 0;
    private int mDrawablePaddingRight = 0;
    private int mDrawablePaddingBottom = 0;

    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public LinearItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
        setLastDecorationWillNotDraw(true);
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    /**
     * @param decorationWillNotDraw if true, the last decoration will not draw.
     */
    public void setLastDecorationWillNotDraw(boolean decorationWillNotDraw) {
        mDecorationWillNotDraw = decorationWillNotDraw;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
    }

    public void setDrawablePadding(int left, int top, int right, int bottom) {
        mDrawablePaddingLeft = left;
        mDrawablePaddingTop = top;
        mDrawablePaddingRight = right;
        mDrawablePaddingBottom = bottom;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    @SuppressLint("NewApi")
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft() + mDrawablePaddingLeft;
            right = parent.getWidth() - parent.getPaddingRight() - mDrawablePaddingRight;
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = mDrawablePaddingLeft;
            right = parent.getWidth() - mDrawablePaddingRight;
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            //the last no draw
            final boolean isLastDecoration = parent.getChildAdapterPosition(child) ==
                    parent.getAdapter().getItemCount() - 1;
            if (mDecorationWillNotDraw && isLastDecoration){
                continue;
            }

            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
            final int top = bottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @SuppressLint("NewApi")
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop() + mDrawablePaddingTop;
            bottom = parent.getHeight() - parent.getPaddingBottom()
                    - mDrawablePaddingBottom;
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = mDrawablePaddingTop;
            bottom = parent.getHeight() - mDrawablePaddingBottom;
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            //the last no draw
            final boolean isLastDecoration = parent.getChildAdapterPosition(child) ==
                    parent.getAdapter().getItemCount() - 1;
            if (mDecorationWillNotDraw && isLastDecoration){
                continue;
            }

            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            Log.e("LinearItemDecoration", mBounds.toString());
            final int right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
            final int left = right - mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);

        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }

        final boolean isLastDecoration = parent.getChildAdapterPosition(view) ==
                parent.getAdapter().getItemCount() - 1;
        if (mDecorationWillNotDraw && isLastDecoration) {
            outRect.set(0, 0, 0, 0);
        }
    }

}
