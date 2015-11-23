package edm_opensource.niagarabooker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends ActionBarActivity implements CustomClickListener {

    private Toolbar toolbar;
    private ViewPager pager;
    private SlidingTabLayout tabs;
    private Controller controller;
    private ProgressBar progressBar;
    private boolean hasInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controller = new Controller(getSupportFragmentManager(), this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        initToolbar();
        initViewPager();
        initTabs();

        checkIfUserIsRegistered();

        NetworkChangeReceiver receiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, intentFilter);

        hasInternet = isOnline(this);
        if (!hasInternet) {
            Toast.makeText(this, R.string.internet_app_use, Toast.LENGTH_LONG).show();
        } else {
            new BookingsTask(true).execute();
        }
    }

    private void checkIfUserIsRegistered() {
        String credentials = SharedPref.getUserCredentials(this);

        if (credentials == null) {
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

        if (id == R.id.action_reset_user) {
            NewUserDialog newUserDialog = new NewUserDialog();
            newUserDialog.setListener(this);
            newUserDialog.show(getFragmentManager(), "NewUserDialog");
        } else if (id == R.id.action_update) {
            if (!hasInternet) {
                Toast.makeText(this, R.string.internet_app_use, Toast.LENGTH_LONG).show();
            } else {
                new BookingsTask(false).execute();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(boolean save, String username, String password) {
        if (save) {
            SharedPref.setUserCredentials(this, username, password);
        }
    }

    @Override
    public void onBook(BookingModel booking) {
        if (booking != null) {
            if (!hasInternet) {
                Toast.makeText(this, R.string.internet_app_use, Toast.LENGTH_LONG).show();
            } else {
                new BookRoomTask().execute(booking);
            }
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (isOnline(MainActivity.this)) {
                hasInternet = true;
            } else {
                hasInternet = false;
            }
        }
    }

    private class BookingsTask extends AsyncTask<Void, List<BookingModel>, List<BookingModel>> {

        private boolean onCreate;

        public BookingsTask(boolean onCreate) {
            this.onCreate = onCreate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
            if (!onCreate) {
                controller.changeStateOfBookButton();
            }
        }

        @Override
        protected List<BookingModel> doInBackground(Void... voids) {
            String credentials = SharedPref.getUserCredentials(MainActivity.this);
            if (credentials == null) {
                return null;
            } else {
                return controller.getBookings(credentials);
            }
        }

        @Override
        protected void onPostExecute(List<BookingModel> bookings) {
            super.onPostExecute(bookings);
            if (bookings == null) {

            } else {
                controller.updateBookings(bookings);
                Toast.makeText(MainActivity.this, R.string.successful_booking, Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            if (!onCreate) {
                controller.changeStateOfBookButton();
            }
        }
    }


    private class BookRoomTask extends AsyncTask<BookingModel, String, String> {

        @Override
        protected String doInBackground(BookingModel... bookingModels) {
            BookingModel booking = bookingModels[0];
            String credentials = SharedPref.getUserCredentials(MainActivity.this);
            if (credentials == null) {
                return getString(R.string.set_credentials_to_book);
            }
            return controller.postBookings(booking, credentials);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
            controller.changeStateOfBookButton();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            controller.changeStateOfBookButton();
            if (s != null) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
        }
    }
}
