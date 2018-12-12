package demo.play.com.demo;

import android.annotation.TargetApi;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;
import java.util.Map;

import static demo.play.com.demo.Comment.urls;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SecondActivity1 extends AppCompatActivity {

    private ViewPager mPager;
    private int index;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mPager = (ViewPager) findViewById(R.id.viewpager);
        index = (int) getIntent().getExtras().get("index");
        supportPostponeEnterTransition();//延缓执行 然后在fragment里面的控件加载完成后start


        adapter = new PagerAdapter();
        mPager.setAdapter(adapter);
        mPager.setCurrentItem(index);
        if (Build.VERSION.SDK_INT >= 22) {
            //这个可以看做个管道  每次进入和退出的时候都会进行调用  进入的时候获取到前面传来的共享元素的信息
            //退出的时候 把这些信息传递给前面的activity
            //同时向sharedElements里面put view,跟对view添加transitionname作用一样
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    String url = urls.get(mPager.getCurrentItem());
                    LargePicFragment fragment = (LargePicFragment) adapter.instantiateItem(mPager, mPager.getCurrentItem());
                    sharedElements.clear();
                    sharedElements.put(ViewCompat.getTransitionName(fragment.getSharedElement()), fragment.getSharedElement());
                }
            });
        }


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                CanScrollBean can = new CanScrollBean();
//                can.setPosition(position);
//                EventBus.getDefault().post(can);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @TargetApi(22)
    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra("index", mPager.getCurrentItem());
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
    }


    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("index", mPager.getCurrentItem());
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
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
