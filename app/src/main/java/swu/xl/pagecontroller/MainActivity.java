package swu.xl.pagecontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化
        PagerController pagerController = findViewById(R.id.page_controller);

        //设置动画
        pagerController.setPageChangeAnimation(new PagerController.PageChangeAnimation() {
            @Override
            public void changeAnimation(View last_dot, View current_dot) {
                //上一个点不做动画

                //针对当前点的动画
                ObjectAnimator scale = ObjectAnimator.ofFloat(current_dot, "scaleX", 1.0f, 1.2f, 1.0f);
                scale.setDuration(500);
                scale.setInterpolator(new BounceInterpolator());
                scale.start();
            }
        });

        //监听页面切换
        pagerController.setPageChangeListener(new PagerController.PageChangeListener() {
            @Override
            public void pageHasChange(int currentPage) {
                System.out.println("当前页面："+(currentPage+1));
            }
        });
    }
}
