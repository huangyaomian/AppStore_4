package com.hym.appstore.common.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.hym.appstore.R;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

//import com.bumptech.glide.BitmapRequestBuilder;
//import com.bumptech.glide.DrawableRequestBuilder;
//import com.bumptech.glide.GenericRequestBuilder;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.signature.StringSignature;

/**
 * 菜鸟窝http://www.cniao5.com 一个高端的互联网技能学习平台
 *
 * @author Ivan
 * @version V1.0
 * @Package com.cniao5.cniao5market.common.imageloader
 * @Description: ${TODO}(用一句话描述该文件做什么)
 * @date
 */

public class ImageLoader {


    //默认配置
    public static ImageLoadConfig defConfig = new ImageLoadConfig.Builder()
            .setCropType(ImageLoadConfig.CENTER_CROP)
            .setAsBitmap(true)
            .setPlaceHolderResId(R.drawable.vector_drawable_init_pic)
//            .setErrorResId(R.drawable.bg_error)
            .setDiskCacheStrategy(ImageLoadConfig.DiskCache.ALL)
            .setPrioriy(ImageLoadConfig.LoadPriority.HIGH)
            .setCrossFade(true)
            .setRoundedCorners(true)
            .build();

    /**
     * 加载String类型的资源
     * SD卡资源："file://"+ Environment.getExternalStorageDirectory().getPath()+"/test.jpg"<p/>
     * assets资源："file:///android_asset/f003.gif"<p/>
     * raw资源："Android.resource://com.frank.glide/raw/raw_1"或"android.resource://com.frank.glide/raw/"+R.raw.raw_1<p/>
     * drawable资源："android.resource://com.frank.glide/drawable/news"或load"android.resource://com.frank.glide/drawable/"+R.drawable.news<p/>
     * ContentProvider资源："content://media/external/images/media/139469"<p/>
     * http资源："http://img.my.csdn.net/uploads/201508/05/1438760757_3588.jpg"<p/>
     * https资源："https://img.alicdn.com/tps/TB1uyhoMpXXXXcLXVXXXXXXXXXX-476-538.jpg_240x5000q50.jpg_.webp"<p/>
     *
     * @param view
     * @param imageUrl
     * @param config
     * @param listener
     */
    public static void loadStringRes(ImageView view, String imageUrl, ImageLoadConfig config, LoaderListener listener) {
        load(view.getContext(), view, imageUrl, config, listener);
    }

    public static void loadFile(ImageView view, File file, ImageLoadConfig config, LoaderListener listener) {
        load(view.getContext(), view, file, config, listener);
    }

    public static void loadResId(ImageView view, Integer resourceId, ImageLoadConfig config, LoaderListener listener) {
        load(view.getContext(), view, resourceId, config, listener);
    }

    public static void loadUri(ImageView view, Uri uri, ImageLoadConfig config, LoaderListener listener) {
        load(view.getContext(), view, uri, config, listener);
    }


    public static void load(String url , ImageView imageView){

        load(imageView.getContext(),imageView,url,null,null);

    }

    public static void load(String url , ImageView imageView,ImageLoadConfig config){

        load(imageView.getContext(),imageView,url,config,null);

    }



    public static void loadGif(ImageView view, String gifUrl, ImageLoadConfig config, LoaderListener listener) {
        load(view.getContext(), view, gifUrl, ImageLoadConfig.parseBuilder(config).setAsGif(true).build(), listener);
    }

    public static void loadTarget(Context context, Object objUrl, ImageLoadConfig config, final LoaderListener listener) {
        load(context, null, objUrl, config, listener);
    }

    private static void load(Context context, ImageView view, Object objUrl, ImageLoadConfig config, final LoaderListener listener) {
        if (null == objUrl) {
            throw new IllegalArgumentException("objUrl is null");
        }
        if (null == config) {
            config = defConfig;
        }
        try {
            RequestBuilder builder = getRequestBuilder(context, objUrl, config);
          /*  if (config.isAsGif()) {//gif类型
//                GifRequestBuilder request = Glide.with(context).asGif().load(objUrl);
                RequestBuilder<GifDrawable> request = Glide.with(context).asGif().load(objUrl);
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    request.centerCrop();
                } else {
                    request.fitCenter();
                }
                builder = request;
            } else if (config.isAsBitmap()) {  //bitmap 类型
//                BitmapRequestBuilder request = Glide.with(context).asBitmap().load(objUrl);
                RequestBuilder<Bitmap> request = Glide.with(context).asBitmap().load(objUrl);
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    request.centerCrop();
                } else {
                    request.fitCenter();
                }
                //transform bitmap
                if (config.isRoundedCorners()) {
                    request.transform(new RoundedCornersTransformation(50, 50));
                } else if (config.isCropCircle()) {
                    request.transform(new CropCircleTransformation());
                } else if (config.isGrayscale()) {
                    request.transform(new GrayscaleTransformation());
                } else if (config.isBlur()) {
                    request.transform(new BlurTransformation(8, 8));
                } else if (config.isRotate()) {
//                    request.transform(new RotateTransformation(context, config.getRotateDegree()));
                }
                builder = request;
            } else if (config.isCrossFade()) { // 渐入渐出动画
                DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
//                DrawableRequestBuilder request = Glide.with(context).load(objUrl).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory));
                RequestBuilder<Drawable> request = Glide.with(context).load(objUrl).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory));
                if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                    request.centerCrop();
                } else {
                    request.fitCenter();
                }
                builder = request;
            }*/
            //缓存设置
            builder.diskCacheStrategy(config.getDiskCacheStrategy().getStrategy()).
                    skipMemoryCache(config.isSkipMemoryCache()).
                    priority(config.getPrioriy().getPriority());
            builder.dontAnimate();
            if (null != config.getTag()) {
                builder.signature(new ObjectKey(config.getTag()));
//                builder.signature(new StringSignature(config.getTag()));
            } else {
//                builder.signature(new StringSignature(objUrl.toString()));
                builder.signature(new ObjectKey(objUrl.toString()));
            }
           if (null != config.getAnimator()) {
               builder.transition(GenericTransitionOptions.with(config.getAnimator()));
//                builder.animate(config.getAnimator());
            } else if (null != config.getAnimResId()) {
//                builder.animate(config.getAnimResId());
               builder.transition(GenericTransitionOptions.with(config.getAnimResId()));
            }
            if (config.getThumbnail() > 0.0f) {
                builder.thumbnail(config.getThumbnail());
            }
            if (null != config.getErrorResId()) {
                builder.error(config.getErrorResId());
            }
            if (null != config.getPlaceHolderResId()) {
                builder.placeholder(config.getPlaceHolderResId());
            }
            if (null != config.getSize()) {
                builder.override(config.getSize().getWidth(), config.getSize().getHeight());
            }
            if (null != listener) {
                setListener(builder, listener);
            }
            if (null != config.getThumbnailUrl()) {
//                BitmapRequestBuilder thumbnailRequest = Glide.with(context).asBitmap().load(config.getThumbnailUrl());
                RequestBuilder<Bitmap> thumbnailRequest = Glide.with(context).asBitmap().load(config.getThumbnailUrl());
                builder.thumbnail(thumbnailRequest).into(view);
            } else {
                setTargetView(builder, config, view);
            }
        } catch (Exception e) {
            view.setImageResource(config.getErrorResId());
        }
    }

    private static RequestBuilder getRequestBuilder(Context context, Object objUrl, ImageLoadConfig config){
        if (config.isAsGif()) {//gif类型
//                GifRequestBuilder request = Glide.with(context).asGif().load(objUrl);
            RequestBuilder<GifDrawable> request = Glide.with(context).asGif().load(objUrl);
            if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                request.centerCrop();
            } else {
                request.fitCenter();
            }
            return request;
        } else if (config.isAsBitmap()) {  //bitmap 类型
//                BitmapRequestBuilder request = Glide.with(context).asBitmap().load(objUrl);
            RequestBuilder<Bitmap> request = Glide.with(context).asBitmap().load(objUrl);
            if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                request.centerCrop();
            } else {
                request.fitCenter();
            }
            //transform bitmap
            if (config.isRoundedCorners()) {
//                request.transform(new RoundedCornersTransformation(50, 50));
                request.transform(new GlideRoundTransform());
            } else if (config.isCropCircle()) {
                request.transform(new CropCircleTransformation());
            } else if (config.isGrayscale()) {
                request.transform(new GrayscaleTransformation());
            } else if (config.isBlur()) {
                request.transform(new BlurTransformation(8, 8));
            } else if (config.isRotate()) {
//                    request.transform(new RotateTransformation(context, config.getRotateDegree()));
            }
            return request;
        } else if (config.isCrossFade()) { // 渐入渐出动画
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
//                DrawableRequestBuilder request = Glide.with(context).load(objUrl).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory));
            RequestBuilder<Drawable> request = Glide.with(context).load(objUrl).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory));
            if (config.getCropType() == ImageLoadConfig.CENTER_CROP) {
                request.centerCrop();
            } else {
                request.fitCenter();
            }
            return request;
        }
        return null;
    }

    private static void setListener(RequestBuilder request, final LoaderListener listener) {
        request.listener(new RequestListener() {

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                if (!e.getMessage().equals("divide by zero")) {
                    listener.onError();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                listener.onSuccess();
                return false;
            }

        });
    }

    private static void setTargetView(RequestBuilder<Bitmap> request, ImageLoadConfig config, ImageView view) {
        //set targetView
        if (null != config.getSimpleTarget()) {
            request.into(config.getSimpleTarget());
        } else if (null != config.getViewTarget()) {
//            request.into(config.getViewTarget());
            request.into(view);
        } else if (null != config.getNotificationTarget()) {
            request.into(config.getNotificationTarget());
        } else if (null != config.getAppWidgetTarget()) {
            request.into(config.getAppWidgetTarget());
        } else {
            request.into(view);
        }
    }

    /**
     * 加载bitmap
     *
     * @param context
     * @param url
     * @param listener
     */
    public static void loadBitmap(Context context, Object url, final BitmapLoadingListener listener) {
        if (url == null) {
            if (listener != null) {
                listener.onError();
            }
        } else {
            Glide.with(context).
                    asBitmap().
                    load(url).
                    diskCacheStrategy(DiskCacheStrategy.NONE).
                    dontAnimate().
                    into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (listener != null) {
                                listener.onSuccess(resource);
                            }
                        }
                    });
        }
    }

    /**
     * 高优先级加载
     *
     * @param url
     * @param imageView
     * @param listener
     */
    public static void loadImageWithHighPriority(Object url, ImageView imageView, final LoaderListener listener) {
        if (url == null) {
            if (listener != null) {
                listener.onError();
            }
        } else {
            Glide.with(imageView.getContext()).
                    asBitmap().
                    load(url).
                    priority(Priority.HIGH).
                    dontAnimate().
                    listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            if (null != listener) {
                                listener.onError();
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            if (null != listener) {
                                listener.onSuccess();
                            }
                            return false;
                        }
                    }).into(imageView);

        }
    }

    /**
     * 取消所有正在下载或等待下载的任务。
     */
    public static void cancelAllTasks(Context context) {
        Glide.with(context).pauseRequests();
    }

    /**
     * 恢复所有任务
     */
    public static void resumeAllTasks(Context context) {
        Glide.with(context).resumeRequests();
    }

    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    public static void clearDiskCache(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(context).clearDiskCache();
            }
        }).start();
    }
    /**
     * 清除所有缓存
     * @param context
     */
    public static void cleanAll(Context context) {
        clearDiskCache(context);
        Glide.get(context).clearMemory();
    }
    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     */
//    public static synchronized long getDiskCacheSize(Context context) {
//        long size = 0L;
//        File cacheDir = PathUtils.getDiskCacheDir(context, CacheConfig.IMG_DIR);
//
//        if (cacheDir != null && cacheDir.exists()) {
//            File[] files = cacheDir.listFiles();
//            if (files != null) {
//                File[] arr$ = files;
//                int len$ = files.length;
//
//                for (int i$ = 0; i$ < len$; ++i$) {
//                    File imageCache = arr$[i$];
//                    if (imageCache.isFile()) {
//                        size += imageCache.length();
//                    }
//                }
//            }
//        }
//
//        return size;
//    }

    public static void clearTarget(Context context, String uri) {
//        if (SimpleGlideModule.cache != null && uri != null) {
//            SimpleGlideModule.cache.delete(new StringSignature(uri));
//            Glide.get(context).clearMemory();
//        }
    }

    public static void clearTarget(View view) {
//        Glide.clear(view);
        Glide.with(view).clear(view);
    }

//    public static File getTarget(Context context, String uri) {
//        return SimpleGlideModule.cache != null && uri != null ? SimpleGlideModule.cache.get(new StringSignature(uri)) : null;
//    }
}