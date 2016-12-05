package com.zzc.androidtrain;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzc.androidtrain.apk_patch.ApkPatchActivity;
import com.zzc.androidtrain.app.BaseActivity;
import com.zzc.androidtrain.async.MyIntentService;
import com.zzc.androidtrain.deviceadmin.DevicePolicySetupActivity;
import com.zzc.androidtrain.fragment.FragmentActivity;
import com.zzc.androidtrain.image_filter.ImageFilterActivity;
import com.zzc.androidtrain.view.GridViewActivity;
import com.zzc.androidtrain.hotfix.BugHotFixTestActivity;
import com.zzc.androidtrain.jnitest.HelloJni;
import com.zzc.androidtrain.net.HttpsTestActivity;
import com.zzc.androidtrain.recycleview_drag_drop.RecyclerViewDragDropActivity;
import com.zzc.androidtrain.renderscript.RenderScriptActivity;
import com.zzc.androidtrain.tts.TTSActivity;
import com.zzc.androidtrain.util.StatusBarUtil;
import com.zzc.androidtrain.util.Toaster;

public class DrawerNavigationActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private static final String TAG = "DrawerNaviActivity";
    private ShareActionProvider mShareActionProvider;
    private TextView tvHello;
    private LinearLayout rlRoot;
    private Button btnFade;
    private IntentFilter mIntentServiceIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitle("RecyclerView");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        tvHello = (TextView) findViewById(R.id.tv_hello);
        rlRoot = (LinearLayout) findViewById(R.id.root);
        btnFade = (Button)findViewById(R.id.btn_fade);

        mIntentServiceIntentFilter = new IntentFilter(Constant.LocalBroadcast.BROADCAST_ACTION_MY_INTENT_SERVICE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_item_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        //设置分享类型
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, "分享数据");
        setShareIntent(shareIntent);

        //搜索控件
        MenuItem itemSearch = menu.findItem(R.id.action_item_search);
        SearchView searchView = (SearchView) itemSearch.getActionView();
        searchView.setOnQueryTextListener(this);


        return true;
    }


    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(DrawerNavigationActivity.this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_item_share) {
            Toast.makeText(this, "点击分享", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_item_choose) {
            Toast.makeText(this, "点击选择", Toast.LENGTH_SHORT).show();
            Intent chooseIntent = new Intent(Intent.ACTION_PICK);
            chooseIntent.setType("*/*");
            startActivityForResult(chooseIntent, 1);
            return true;
        }

        if (id == R.id.action_item_print) {
            startActivity(new Intent(DrawerNavigationActivity.this, PrintActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(DrawerNavigationActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(DrawerNavigationActivity.this, ItemListActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(DrawerNavigationActivity.this, ViewPagerActivity.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(DrawerNavigationActivity.this, SettingsActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            startActivity(new Intent(DrawerNavigationActivity.this, BugHotFixTestActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("查询字符串提交----->" + query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        System.out.println("查询字符串修改----->" + newText);
        return false;
    }

    @Override
    protected void setStatusBar() {
        int mStatusBarColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout), mStatusBarColor);
    }   

    boolean tvVisible = true;

    public void onChangeScenesBtnClick(View view) {
        if(tvVisible) {
            fadeOutTextView();
        } else {
            fadeInTextView();
        }
    }

    public void onTTSBtnClick(View view) {
        startActivity(new Intent(this, TTSActivity.class));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void fadeInTextView() {
        Fade fade = new Fade(Fade.IN);
        TransitionManager.go(new Scene(rlRoot, btnFade), fade);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void fadeOutTextView() {
        Fade fade = new Fade(Fade.OUT);
        TransitionManager.go(new Scene(rlRoot, btnFade), fade);
    }

    public void onClickSystemBarBtn(View view) {
        startActivity(SystemBarActivity.getCallingIntent(this));
    }

    public void onClickCanvasExampleBtn(View view) {
        startActivity(CanvasExampleActivity.getCallingIntent(this));
    }

    public void onNewFeatureInSupportLibrary(View view) {
        startActivity(NewFeatureInSupportLibraryActivity.getCallingIntent(this));
    }

    public void onTouchTest(View view) {
        startActivity(MotionEventActivity.getCallingIntent(this));
    }

    public void onRecyclerViewDrag(View view) {
        startActivity(RecyclerViewDragDropActivity.getCallingIntent(this));
    }

    public void onStatusBarModel(View view) {
        startActivity(StatusBarModelActivity.getCallingIntent(this));
    }

    public void onImageBlur(View view) {
        startActivity(RenderScriptActivity.getCallingIntent(this));
    }

    public void onCallNativeMethod(View view) {
        startActivity(HelloJni.getCallingIntent(this));
    }

    public void onNetTest(View view) {
        startActivity(HttpsTestActivity.getCallingIntent(this));
    }

    public void onDevicePolicy(View view) {
        startActivity(DevicePolicySetupActivity.getCallingIntent(this));
    }

    public void onGridViewTest(View view) {
        startActivity(GridViewActivity.getCallingIntent(this));
    }

    public void onCalendar(View view) {
        startActivity(CalendarActivity.getCallingIntent(this));
    }

    public void onFragment(View view) {
        startActivity(FragmentActivity.getCallingIntent(this));
    }

    public void onSendNotification(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(this, DrawerNavigationActivity.class));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("测试推送Ticker")
                .setContentTitle("推送标题")
                .setContentText("推送内容")
                .setSmallIcon(R.drawable.ic_launcher)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);


    }

    public void onImageFilter(View view) {
        startActivity(ImageFilterActivity.getCallingIntent(this));
    }

    public void onPathUpdate(View view) {
        startActivity(ApkPatchActivity.getCallingIntent(this));
    }

    /**
     * 开启后台任务
     *
     * 使用LocalBroadcast接收处理结果数据
     * @param view
     */
    public void onNewIntentService(View view) {
        MyIntentService.startActionFoo(this, "intent", "service");
    }

    private class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(MyIntentService.EXTRA_RESULT);
            Toaster.showLongToast(getBaseContext(), "任务完成:"+result);
        }
    }

    private ResponseReceiver mBroadcastReceiver = new ResponseReceiver();

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播监听
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, mIntentServiceIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //取消注册
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
}
