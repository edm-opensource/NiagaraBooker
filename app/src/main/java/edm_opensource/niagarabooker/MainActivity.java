package edm_opensource.niagarabooker;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements CustomClickListener {

    private Toolbar toolbar;
    private ViewPager pager;
    private SlidingTabLayout tabs;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new Controller(getSupportFragmentManager());

        initToolbar();
        initViewPager();
        initTabs();

        checkIfUserIsRegistered();
    }

    private void checkIfUserIsRegistered() {
        String username = SharedPref.getUser(this);

        if (username == null) {
            NewUserDialog newUserDialog = new NewUserDialog();
            newUserDialog.setListener(this);
            newUserDialog.show(getFragmentManager(), "NewUserDialog");
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViewPager() {
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(controller.getAdapter());
    }

    private void initTabs() {
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            byte[] password = SharedPref.getPassword(this);
            Log.d("MainActivity", Base64.encodeToString(password, Base64.DEFAULT));
            Log.d("MainActivity", controller.getPassword(password));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(boolean save, String username, String password) {
        if (save) {
            Log.d("MainActivity", username + " " + password);
            SharedPref.setUser(this, username);
            SharedPref.setPassword(this, password);
        }
    }
}
