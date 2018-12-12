package demo.play.com.demo;

import android.app.SharedElementCallback;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.library.test.StartUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ImageView image;
    private ArrayList<String> urls = new ArrayList<>();
    private RecycleAdapter adapter;

    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        image = (ImageView) findViewById(R.id.image);
        adapter = new RecycleAdapter(this, this);

        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542359234325&di=d76577449c1abdf96605a1c720df396c&imgtype=0&src=http%3A%2F%2Fimg.oyksoft.com%2Fpic%2F201811%2F01174018_42743b5b9e.jpg");
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542359234319&di=bf8ddc39d485d99e6b30e7f1b99d6288&imgtype=0&src=http%3A%2F%2Fdingyue.nosdn.127.net%2F0rp1dOH4jTONuOOS1cV9oPt5txrI4cKH1HQXdlPH3Vhdh1541156207273compressflag.jpg");
        urls.add("http://imgsrc.baidu.com/imgad/pic/item/d000baa1cd11728bdf999dd4c2fcc3cec2fd2c8b.jpg");
        urls.add("http://img5.duitang.com/uploads/item/201312/05/20131205171922_dVBte.jpeg");
        urls.add("http://img.pconline.com.cn/images/upload/upc/tx/gamephotolib/1410/27/c0/40170771_1414341013392.jpg");
        urls.add("http://images.17173.com/2014/9yin/2014/03/11/20140311092844886.png");
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542359234318&di=853828b15ad006a7236bbd45f83a0cc9&imgtype=0&src=http%3A%2F%2Fwww.17qq.com%2Fimg_qqtouxiang%2F69016928.jpeg");
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542359234316&di=929439b9620eced0ec426bd0ea9d7867&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn14%2F200%2Fw640h360%2F20181115%2F8510-hnvukff3227000.jpg");
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542359234312&di=e505f530aa6ecbca148af578b6386f34&imgtype=0&src=http%3A%2F%2Fd.ifengimg.com%2Fw600%2Fp0.ifengimg.com%2Fpmop%2F2018%2F0929%2FFE644071FB1F06EBCE8F88EE1D23A753180D375B_size154_w800_h494.jpeg");
        urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542359440368&di=ea5554b20ead50972c3e8ed5cf80923d&imgtype=0&src=http%3A%2F%2Fimg.oyksoft.com%2Fpic%2F201810%2F26143721_a1ae9c2e3f.jpg");
        Comment.urls = urls;

        adapter.setDataList(urls);
        adapter.setHasStableIds(true);
        //初始化布局管理器
        final GridLayoutManager lm = new GridLayoutManager(this, 2);
        //设置布局管理器
        recyclerView.setLayoutManager(lm);
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set((int) getResources().getDimension(R.dimen.space_5), (int) getResources().getDimension(R.dimen.space_5), (int) getResources().getDimension(R.dimen.space_5), (int) getResources().getDimension(R.dimen.space_5));
            }
        });
        recyclerView.setAdapter(adapter);
        image.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= 22) {
            setExitSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                    if (bundle != null) {
                        int i = bundle.getInt("index", 0);
                        sharedElements.clear();
                        names.clear();
                        View itemView = lm.findViewByPosition(i);
                        ImageView imageView = itemView.findViewById(R.id.imageView);
                        //注意这里第二个参数，如果防止是的条目的item则动画不自然。放置对应的imageView则完美
                        sharedElements.put(ViewCompat.getTransitionName(imageView), imageView);
                        bundle = null;
                    }
                }
            });
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView:
                int index = (int) view.getTag(R.id.imageView);
                ImageView imvv = (ImageView) view;
                Bitmap bm = ((BitmapDrawable) imvv.getDrawable()).getBitmap();
                Comment.bitmap = bm;
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("index", index);

                if (Build.VERSION.SDK_INT >= 22) {
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(MainActivity.this, view, ViewCompat.getTransitionName(view));// mAdapter.get(position).getUrl()
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
                break;
            case R.id.image:
                StartUtils.startActivity(this);
                break;
        }
    }


    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        bundle = new Bundle(data.getExtras());
    }
}
