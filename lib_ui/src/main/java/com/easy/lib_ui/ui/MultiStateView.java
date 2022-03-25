package com.easy.lib_ui.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easy.lib_ui.R;

/**
 * 公司名称: ~漫漫人生路~总得错几步~
 * 创建作者: Android 孟从伦
 * 创建时间: 2022/01/17
 * 功能描述:
 */
public class MultiStateView extends FrameLayout {

    public static final int STATE_LOADING = 2;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_EMPTY = 0;
    public static final int STATE_NET_ERROR = -1;
    public static final int STATE_UNKNOWN = -2;
    public static final int STATE_NEW_STATE = -3;

    private Context mContext;
    //四种状态默认的viewid
    private int mLoadingViewId;
    private int mSuccessViewId;
    private int mEmptyViewId;
    private int mUnKnownViewId;
    private int mNetErrorViewId;
    private Drawable mEmptyDrawable;
    private String mEmptyDesc;

    //四种展示的view
    private View mLoadingView;
    private View mSuccessView;
    private View mNetErrorView;
    private View mEmptyView;
    private View mUnKnownView;
    private View newStateView;//这是新的状态页面

    private LayoutInflater mInflater;
    private ViewGroup.LayoutParams params;
    private OnReLodListener mOnReLodListener;//重新加载的监听

    private int currentState;

    public MultiStateView(@NonNull Context context) {
        this(context, null);
    }

    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView);

        mLoadingViewId = array.getResourceId(R.styleable.MultiStateView_msv_loadingView, R.layout.base_multi_state_loading);
        mSuccessViewId = array.getResourceId(R.styleable.MultiStateView_msv_successView, 0);
        mEmptyViewId = array.getResourceId(R.styleable.MultiStateView_msv_emptyView, R.layout.base_multi_state_empty);
        mEmptyDrawable = array.getDrawable(R.styleable.MultiStateView_msv_emptyViewImage);
        mEmptyDesc = array.getString(R.styleable.MultiStateView_msv_emptyViewText);
        mUnKnownViewId = array.getResourceId(R.styleable.MultiStateView_msv_unknownView, R.layout.base_multi_state_unknow);
        mNetErrorViewId = array.getResourceId(R.styleable.MultiStateView_msv_netErrorView, R.layout.base_multi_state_neterror);

        array.recycle();
        mInflater = LayoutInflater.from(context);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        showLoading();
    }
    //++++++++++++++++++++++++++++++++加载页面++++++++++++++++++++

    /**
     * 显示加载中的状态
     */
    public void showLoading() {
        if (null == mLoadingView) {
            mLoadingView = mInflater.inflate(mLoadingViewId, null);
        }
        if (mLoadingView != null&&currentState!=STATE_LOADING) {
            removeAllViews();
            addView(mLoadingView, 0, params);
            currentState = STATE_LOADING;
        } else {
            throw new NullPointerException("you have to set loading view before that");
        }
    }

    /**
     * 设置自定义的加载页面
     *
     * @param layoutResID
     */
    public void setLoadingView(@LayoutRes int layoutResID) {
        View inflate = mInflater.inflate(layoutResID, null);
        setLoadingView(inflate);
    }

    public void setLoadingView(View view) {
        if (view == null) {
            throw new NullPointerException("you set loading view is null");
        }
        mLoadingView = view;
    }

    /**
     * 获取加载页面
     */
    public View getLoadingView() {
        if (null == mLoadingView) {
            mLoadingView = mInflater.inflate(mLoadingViewId, null);
        }
        return mLoadingView;
    }


    //++++++++++++++++++++++++++++++++成功页面++++++++++++++++++++

    /**
     * 显示成功状态
     */
    public void showSuccess() {
        if (null == mSuccessView) {
            mSuccessView = mInflater.inflate(mSuccessViewId, null);
        }
        if (mSuccessView != null&&currentState!=STATE_SUCCESS) {
            removeAllViews();
            addView(mSuccessView, 0, params);
            currentState = STATE_SUCCESS;
        } else {
            throw new NullPointerException("you have to set success view before that");
        }
    }

    /**
     * 设置自定义的成功页面
     *
     * @param layoutResID
     */
    public void setSuccessView(@LayoutRes int layoutResID) {
        setSuccessView(mInflater.inflate(layoutResID, null));
    }

    /**
     * 设置自定义的成功页面
     *
     * @param view
     */
    public void setSuccessView(View view) {
        if (view == null) {
            throw new NullPointerException("you set success view is null");
        }
        mSuccessView = view;
    }


    /**
     * 获取成功页面
     */
    public View getSuccessView() {
        if (null == mSuccessView) {
            mSuccessView = mInflater.inflate(mSuccessViewId, null);
        }
        return mSuccessView;
    }


    //++++++++++++++++++++++++++++++++未知错误页面++++++++++++++++++++

    /**
     * 显示未知错误页面
     */
    public void showUnKnown() {
        if (null == mUnKnownView) {
            mUnKnownView = mInflater.inflate(mUnKnownViewId, null);
        }

        if (mUnKnownView != null&&currentState!=STATE_UNKNOWN) {
            removeAllViews();
            addView(mUnKnownView, 0, params);
            currentState = STATE_UNKNOWN;
        } else {
            throw new NullPointerException("you have to set unknown view before that");
        }
    }

    /**
     * 设置自定义的未知错误页面
     *
     * @param layoutResID
     */
    public void setUnKnownView(@LayoutRes int layoutResID) {
        View inflate = mInflater.inflate(layoutResID, null);
        setUnKnownView(inflate);
    }

    /**
     * 设置自定义的未知错误页面
     *
     * @param view
     */
    public void setUnKnownView(View view) {
        if (view == null) {
            throw new NullPointerException("you set unknown view is null");
        }
        mUnKnownView = view;
    }

    /**
     * 获取未知错误页面
     */
    public View getUnKnownView() {
        if (null == mUnKnownView) {
            mUnKnownView = mInflater.inflate(mUnKnownViewId, null);
        }
        return mUnKnownView;
    }


    //++++++++++++++++++++++++++++++++网络错误页面++++++++++++++++++++

    /**
     * 显示加载失败(网络错误)状态 带监听器的
     */
    public void showNetError() {
        if (null == mNetErrorView) {
            mNetErrorView = mInflater.inflate(mNetErrorViewId, null);
        }

        if (mNetErrorView != null&&currentState!=STATE_NET_ERROR) {
            removeAllViews();
            addView(mNetErrorView, 0, params);
            currentState = STATE_NET_ERROR;
            mNetErrorView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReLoading();
                }
            });
        } else {
            throw new NullPointerException("you have to set unknown view before that");
        }
    }


    /**
     * 设置自定义的网络异常
     *
     * @param layoutResID
     */
    public void setNetErrorView(@LayoutRes int layoutResID) {
        View inflate = mInflater.inflate(layoutResID, null);
        setNetErrorView(inflate);
    }

    /**
     * 设置自定义的网络异常
     *
     * @param view
     */
    public void setNetErrorView(View view) {
        if (view == null) {
            throw new NullPointerException("you set net error view is null");
        }
        mNetErrorView = view;
    }


    /**
     * 设置获取网络错误页面
     */
    public View getNetErrorView() {
        if (null == mNetErrorView) {
            mNetErrorView = mInflater.inflate(mNetErrorViewId, null);
        }
        return mNetErrorView;
    }


    //++++++++++++++++++++++++++++++++空页面页面++++++++++++++++++++

    /**
     * 显示无数据状态
     */
    public void showEmpty() {
        if (null == mEmptyView) {
            mEmptyView = mInflater.inflate(mEmptyViewId, null);
        }

        if (mEmptyView != null&&currentState!=STATE_EMPTY) {
            removeAllViews();
            handleDiffEmpty();
            addView(mEmptyView, 0, params);
            currentState = STATE_EMPTY;
        } else {
            throw new NullPointerException("you have to set empty view before that");
        }
    }

    /**
     * 对于网络异常处理
     */
    private void handleDiffEmpty() {
        if (mEmptyView instanceof ViewGroup) {
            ViewGroup mEmptyViews = (ViewGroup) mEmptyView;
            ImageView emptyImage = (ImageView) mEmptyViews.getChildAt(0);
            TextView emptyDesc = (TextView) mEmptyViews.getChildAt(1);
            if (emptyImage != null && mEmptyDrawable != null) {
                emptyImage.setImageDrawable(mEmptyDrawable);
            }
            if (emptyDesc != null && TextUtils.isEmpty(mEmptyDesc)) {
                emptyDesc.setText(mEmptyDesc);
            }
        }
    }

    /**
     * 设置自定义的空页面
     *
     * @param layoutResID
     */
    public void setEmptyView(@LayoutRes int layoutResID) {
        View inflate = mInflater.inflate(layoutResID, null);
        setEmptyView(inflate);
    }

    /**
     * 设置自定义的空页面
     *
     * @param view
     */
    public void setEmptyView(View view) {
        if (view == null) {
            throw new NullPointerException("you set net empty view is null");
        }
        mEmptyView = view;
    }


    /**
     * 设置获取空页面
     */
    public View getEmptyView() {
        if (null == mEmptyView) {
            mEmptyView = mInflater.inflate(mEmptyViewId, null);
        }
        return mEmptyView;
    }


    /**
     * 设置自定义的新增状态页面空页面
     *
     * @param layoutResID
     */
    public void setNewStateView(@LayoutRes int layoutResID) {
        View inflate = mInflater.inflate(layoutResID, null);
        setNewStateView(inflate);
    }


    public void setNewStateView(View view) {
        if (view == null) {
            throw new NullPointerException("you set net new state view is null");
        }
        newStateView = view;
    }

    /**
     * 显示新状态view
     */
    public void showNewStateView() {
        if (newStateView != null&&currentState!=STATE_NEW_STATE) {
            removeAllViews();
            addView(newStateView, 0, params);
            currentState = STATE_NEW_STATE;
        } else {
            throw new NullPointerException("you set new state view is null");
        }
    }


    public View getNewStateView() {
        if (newStateView == null) {
            throw new IllegalArgumentException("you has not set new state view");
        }
        return newStateView;
    }

    public int getCurrentState() {
        return currentState;
    }

    /**
     * 再次加载数据
     */
    private void showReLoading() {
        //第一步重新loading
        if (mOnReLodListener != null) {
            showLoading();
            mOnReLodListener.onReLoad();
        } else {
            //未设置重新加载回调
            Log.e("TAG", "请设置重新加载监听");
        }
    }


    /**
     * 外部回调
     *
     * @param onReLodListener
     */
    public void setOnReLodListener(OnReLodListener onReLodListener) {
        this.mOnReLodListener = onReLodListener;
    }

    /**
     * 重新加载页面的回调接口
     */
    public interface OnReLodListener {
        void onReLoad();
    }

}
