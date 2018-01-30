package com.example.pan.autocall;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionGen;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.version_tv)
    TextView versionTv;
    @BindView(R.id.download_tv)
    TextView downloadTv;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.tel_et)
    EditText telEt;
    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.stop_btn)
    Button stopBtn;
    @BindView(R.id.switch_btn)
    SwitchButton switchBtn;

    private SharedPreferences sharedPreference;
    private String usrid = "usreid";
    private String telStr = "telstr";
    private Handler handler;
    private int seconds = 3;

    private Timer timer;

    private boolean isRunning = false;
    private boolean clickStop = true;

    private String tel1 = "13426421951";
    private String tel2 = "15711055034";

    private String authorEmail = "p774285727@163.com";
    private String downloadUrl = "https://github.com/panyufen/panyufen.github.io/raw/master/kuqi/Android_AutoCall.apk";
    private String clickText = "有新版本";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PermissionGen.needPermission(this, 100, new String[]{Manifest.permission.CALL_PHONE});
        sharedPreference = getSharedPreferences("SHARE_TAG", Context.MODE_PRIVATE);


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (clickStop) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    isRunning = true;
                    switch (message.what) {
                        case 1111:
                            call1();
                            break;
                        case 2222:
                            call2();
                            break;
                        case 3333:
                            call3();
                            break;
                        case 4444:
                            call4();
                            break;
                    }
                }
                return true;
            }
        });

        initView();
        initTelePhoneListener();
    }

    private void initView() {
        String text = downloadTv.getText().toString();
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        int starti = text.indexOf(clickText);
        int endi = starti + clickText.length();
        builder.setSpan(new URLSpan(downloadUrl), starti, endi, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        downloadTv.setText(builder);
        downloadTv.setMovementMethod(LinkMovementMethod.getInstance());

        String versionName = VersionCheckor.getInstance(this).getPackageInfo().versionName;
        versionTv.setText(getString(R.string.version_str, versionName));

        telEt = (EditText) findViewById(R.id.tel_et);
        String telShare = sharedPreference.getString(telStr, "");
        if (!TextUtils.isEmpty(telShare)) {
            telEt.setText(telShare);
            telEt.setSelection(telShare.length());
        }
        stopBtn = (Button) findViewById(R.id.stop_btn);

        VersionCheckor.getInstance(this).checkVersion(new VersionCheckor.CheckInterface() {
            @Override
            public void upgrade(int cv, int nv) {
                downloadTv.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "有新版本！", Toast.LENGTH_LONG).show();
            }
        });

        switchBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                stopBtn.setEnabled(isChecked);
            }
        });
    }

    private void initTelePhoneListener() {
//        TelephoneProcessor telephoneProcessor = new TelephoneProcessor(this);
    }


    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4})
    public void onViewClicked(View view) {
        if (timer == null) {
            isRunning = true;
            switch (view.getId()) {
                case R.id.button1:
                    call1();
                    break;
                case R.id.button2:
                    call2();
                    break;
                case R.id.button3:
                    call3();
                    break;
                case R.id.button4:
                    call4();
                    break;
            }
        }
    }

    @OnClick({R.id.stop_btn})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.stop_btn:
                clickStop = false;
                stopBtn.setText("终止重拨");
                Toast.makeText(this, "成功终止自动重拨", Toast.LENGTH_SHORT).show();
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                break;
        }
    }


    public void call1() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel1));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            clickStop = true;
            startActivityForResult(intent, 1111);
        }

    }

    public void call2() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel2));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            clickStop = true;
            startActivityForResult(intent, 2222);
        }
    }

    public void call3() {
        String tel = tel1;
        int index = new Random().nextInt(2);
        switch (index) {
            case 0:
                tel = tel1;
                break;

            case 1:
                tel = tel2;
                break;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            clickStop = true;
            startActivityForResult(intent, 3333);
        }
    }

    public void call4() {
        String tel = telEt.getText().toString();
        sharedPreference.edit().putString(telStr, tel).apply();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            clickStop = true;
            startActivityForResult(intent, 4444);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("MainActivity", "onActivityResult " + requestCode + " " + resultCode);
        MobclickAgent.onEvent(this, "Call");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (isRunning) {
                    if (getAppIsForeGround()) {
                        isRunning = false;
                        Message message = new Message();
                        message.what = requestCode;
                        if (switchBtn.isChecked()) {
                            handler.sendMessageDelayed(message, 3000);
                            timer = new Timer();
                            seconds = 3;
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            stopBtn.setText("终止重拨（" + seconds + "秒）");
                                        }
                                    });
                                    if (seconds > 0) {
                                        seconds--;
                                    }
                                }
                            }, 0, 1000);
                        } else {
                            handler.sendMessage(message);
                        }
                    }
                }
            }
        }).start();
    }

    private boolean getAppIsForeGround() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runnings = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo running : runnings) {
            if (running.processName.equals(getPackageName())) {
                if (running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        || running.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
