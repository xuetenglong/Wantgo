package demo.play.com.demo;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import static demo.play.com.demo.Comment.urls;

/**
 * Created by Mr_Wrong on 15/10/6.
 */
public class LargePicFragment extends Fragment {
    private SecondActivity activity;
    private int index;
    private ImageView image;
    View view;
//    CircleProgressView progressView;//进度条

    public static Fragment newFragment(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        LargePicFragment fragment = new LargePicFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (SecondActivity) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = getArguments().getInt("index");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_viewer, container, false);
        image = view.findViewById(R.id.image);
//        progressView = view.findViewById(R.id.progressView);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.supportFinishAfterTransition();
            }
        });


        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
        ViewCompat.setTransitionName(view,index+"");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


//        ProgressInterceptor.addListener(urls.get(index), new ProgressListener() {
//            @Override
//            public void onProgress(int progress) {
//                progressView.setProgress(progress);
//            }
//        });

        Glide.with(activity)
                .load(urls.get(index))
//                .dontAnimate()
                .into(image);

    }

    public View getSharedElement() {
        return image;
    }

    public View getSharedElementView() {
        return view;
    }


}
