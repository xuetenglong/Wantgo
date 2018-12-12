package demo.play.com.demo;

import android.app.SharedElementCallback;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;
import java.util.Map;

import static demo.play.com.demo.Comment.urls;

public class SecondActivity extends AppCompatActivity {
    ViewPager viewpager;
    private PagerAdapter adapter;
    private LayoutInflater inflater;
    int index;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        viewpager = findViewById(R.id.viewpager);
        inflater = LayoutInflater.from(this);
        index = (int) getIntent().getExtras().get("index");
        supportPostponeEnterTransition();//延缓执行 然后在fragment里面的控件加载完成后start


        adapter = new PagerAdapter();
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(index);
        if (Build.VERSION.SDK_INT >= 22) {
            //这个可以看做个管道  每次进入和退出的时候都会进行调用  进入的时候获取到前面传来的共享元素的信息
            //退出的时候 把这些信息传递给前面的activity
            //同时向sharedElements里面put view,跟对view添加transitionname作用一样
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    String url = urls.get(viewpager.getCurrentItem());
                    LargePicFragment fragment = (LargePicFragment) adapter.instantiateItem(viewpager, viewpager.getCurrentItem());
                    sharedElements.clear();
                    sharedElements.put(ViewCompat.getTransitionName(fragment.getSharedElementView()), fragment.getSharedElementView());
                }
            });
        }
    }

    public class SimpleFragmentAdapter extends android.support.v4.view.PagerAdapter {
        private int mChildCount = 0;

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View contentView = inflater.inflate(R.layout.picture_image_preview, container, false);
//            ViewCompat.setTransitionName(contentView,images.get(position).getTag());
            // 常规图控件
            final ImageView imageView = contentView.findViewById(R.id.imageView);
            ViewCompat.setTransitionName(contentView,index+"");
            // 长图控件
            if (urls != null) {
                
                RequestOptions options = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(SecondActivity.this)
                        .asBitmap()
                        .load(urls.get(position))
                        .apply(options)
                        .into(new SimpleTarget<Bitmap>(480, 800) {
                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                            }

                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                imageView.setImageBitmap(resource);
                            }
                        });
                contentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        contentView.getViewTreeObserver().removeOnPreDrawListener(this);
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                });

            }
            (container).addView(contentView, 0);
            return contentView;
        }
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public Fragment getItem(int position) {
            return LargePicFragment.newFragment(
                    position);
        }

    }
}
