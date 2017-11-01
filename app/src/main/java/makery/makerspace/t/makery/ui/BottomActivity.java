package makery.makerspace.t.makery.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import makery.makerspace.t.makery.R;
import makery.makerspace.t.makery.fragment.EventFragment;
import makery.makerspace.t.makery.fragment.ManualFragment;
import makery.makerspace.t.makery.fragment.MypageFragment;
import makery.makerspace.t.makery.fragment.ProjectFragment;
import makery.makerspace.t.makery.util.U;
import makery.makerspace.t.makery.util.UIFactory;

public class BottomActivity extends AppCompatActivity {

    UIFactory uiFactory;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab1:
                    onMenu(0);
                    return true;
                case R.id.tab2:
                    onMenu(1);
                    return true;
                case R.id.tab3:
                    onMenu(2);
                    return true;
                case R.id.tab5:
                    onMenu(3);
                    return true;
            }
            return false;
        }

    };

    ViewPager pager;
    PAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        U.getInstance().appendApp(this);
        getSupportActionBar().hide();

        uiFactory = UIFactory.getInstance(this);

        pager = uiFactory.createView(R.id.pager);

        pAdapter = new PAdapter(getSupportFragmentManager());
        pager.setAdapter(pAdapter);
        BottomNavigationViewEx navigation = uiFactory.createView(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setupWithViewPager(pager);   // 스와이프해서 화면 전환

        //Log.i("T", "mGoogleApiClient = " + ((MakeryApplication)getApplication()).getmGoogleApiClient().isConnected());

    }

    ManualFragment manualFragment = new ManualFragment();
    ProjectFragment projectFragment = new ProjectFragment();
    EventFragment eventFragment = new EventFragment();
    MypageFragment mypageFragment = new MypageFragment();

    class PAdapter extends FragmentPagerAdapter {
        public PAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return manualFragment;
                case 1:
                    return projectFragment;
                case 2:
                    return eventFragment;
                case 3:
                    return mypageFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public void onMenu(int type) {
        pager.setCurrentItem(type);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed(); 막아줘야 백키를 한 번 눌렀을 때 종료되지 않는다.

        if (!isFirstEnd) {
            // 백키를 한 번 누름
            isFirstEnd = true;
            // 3초후에 초기화 (백키를 한 번 누른 상황)
            handler.sendEmptyMessageDelayed(1000, 1000 * 3); // 1초에 1000
            Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    boolean isFirstEnd; // 한 번 백키를 눌렀는가?

    // 핸들러, 메시지를 던져서 큐(메시지큐)에 담고 하나씩 꺼내서 처리하는 메시징 시스템
    Handler handler =
            new Handler() {
        // 이 메소드는 큐에 메세지가 존재하면 뽑아서 호출된다.
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {      // 3초가 지났다. 다시 초기화
                isFirstEnd = false;
            }
        }
    };
}
