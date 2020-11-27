package com.silver.fox.viewadapter;

import android.content.res.Resources;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;



public class ImageViewAdapter {

    /**
     * glide加载图片
     * 1. 根据图片url判断是否是gif图片，如果是则默认加载成gif图，否则加载成静态图片
     * 2. 如果某个地方需要不显示gif，则可以在xml中加入配置 app:forceBitmap="@{true}"
     *
     * @param imageView   target
     * @param url         url,如果传入的实际是一个resourceId，其实也是可以的
     * @param placeholder resId
     * @param error       resId
     */
    @BindingAdapter(value = {"imageUrl", "placeHolder", "errorImage", "noCache"}, requireAll = false)
    public static void loadImage(ImageView imageView, String url, int placeholder, int error, boolean noCache) {
        GlideUrl glideUrl = null;
        if (noCache && !TextUtils.isEmpty(url)) { //构造GlideUrl传入的参数url不能为空
            glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                    .addHeader("cache-control", "no-cache").build());
        }
        int resId;
        try {
            resId = Integer.parseInt(url);
            imageView.getContext().getResources().getDrawable(resId);
        } catch (NumberFormatException | Resources.NotFoundException e) {
            resId = -1;
        }
        Object loadModel = noCache ? glideUrl : (resId == -1 ? url : resId);
        Glide.with(imageView.getContext())
                .load(loadModel)
                .error(error)
                .placeholder(placeholder)
                .skipMemoryCache(noCache)
                .diskCacheStrategy(noCache ? DiskCacheStrategy.NONE : DiskCacheStrategy.ALL).into(imageView);

    }


    @BindingAdapter(value = "imageRes")
    public static void loadRes(ImageView imageView, int resId) {
        imageView.setImageResource(resId);
    }

    @BindingAdapter(value = "backgroundRes")
    public static void setBackground(ImageView imageView, int resId) {
        imageView.setBackgroundResource(resId);
    }

}
