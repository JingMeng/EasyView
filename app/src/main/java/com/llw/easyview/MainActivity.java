package com.llw.easyview;

import com.llw.easyview.databinding.ActivityMainBinding;
import com.llw.easyview.used.MacAddressActivity;
import com.llw.easyview.used.ProgressBarActivity;
import com.llw.easyview.used.TimingTextActivity;
import com.llw.easyview.used.EasyEditTextActivity;

/**
 * 主页面功能跳转
 */
public class MainActivity extends EasyActivity<ActivityMainBinding> {

    @Override
    protected void onCreate() {
        binding.btnMadAddress.setOnClickListener(v -> jumpActivity(MacAddressActivity.class));
        binding.btnProgressBar.setOnClickListener(v -> jumpActivity(ProgressBarActivity.class));
        binding.btnTimingText.setOnClickListener(v -> jumpActivity(TimingTextActivity.class));
        //看看这个就好了
        binding.btnEasyEdittext.setOnClickListener(v -> jumpActivity(EasyEditTextActivity.class));
    }
}