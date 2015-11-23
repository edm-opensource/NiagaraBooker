package edm_opensource.niagarabooker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Base64;
import android.widget.Toast;

import com.goebl.david.Response;
import com.goebl.david.Webb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mattiaspernhult on 2015-11-19.
 */
public class Controller {

    private String titles[] = new String[2];

    private ViewPagerAdapter adapter;
    private MainActivity mainActivity;
    private BookRoomFragment bookRoomFragment;
    private BookingsFragment bookingsFragment;

    public Controller(FragmentManager fm, MainActivity mainActivity) {
        this.adapter = new ViewPagerAdapter(fm);
        this.mainActivity = mainActivity;
        titles[0] = mainActivity.getResources().getString(R.string.book_room);
        titles[1] = mainActivity.getResources().getString(R.string.bookings);
    }

    public String postBookings(BookingModel booking, String credentialsString) {
        byte[] credentials = new byte[0];
        try {
            credentials = (credentialsString).getBytes("UTF-8");
            String auth = "Basic " + Base64.encodeToString(credentials, 0);
            Webb webb = Webb.create();
            webb.setDefaultHeader(Webb.HDR_AUTHORIZATION, auth);
            webb.setDefaultHeader("Content-Type", "application/json");

            String time = booking.getTimeAsPost();
            String room = booking.getRoomAsPost();

            webb.setBaseUri("https://mah-book-room-api.herokuapp.com");

            Response<JSONObject> response = webb
                    .post("/bookings")
                    .body("{\"date\": \"" + booking.getDate() + "\", \"time\" : \"" + time + "\", \"room\" : \"" + room + "\"}")
                    .asJsonObject();

            if(response.isSuccess()) {
                JSONObject res = response.getBody();
                String date = res.getString("date");
                JSONObject embedded = res.getJSONObject("_embedded");
                String roomJson = embedded.getJSONObject("room").getString("name");
                String timeJson = embedded.getJSONObject("time").getString("name");
                final BookingModel bookingModel = new BookingModel(date, room, time);
                bookingModel.changeRoomToNormal();
                bookingModel.changeTimeToNormal();
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bookingsFragment.addBooking(bookingModel);
                    }
                });
                return "Booking was successful";
            } else {
                JSONObject error = new JSONObject(response.getErrorBody().toString());
                int statusCode = error.getInt("status");
                if (statusCode == 409) {
                    return "Room occupied";
                } else if (statusCode == 401) {
                    return error.getString("error");
                } else {
                    return "Unable to book the room";
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BookingModel> getBookings(String credentialsString) {
        byte[] credentials = new byte[0];
        try {
            credentials = (credentialsString).getBytes("UTF-8");
            String auth = "Basic " + Base64.encodeToString(credentials, 0);
            Webb webb = Webb.create();
            webb.setDefaultHeader(Webb.HDR_AUTHORIZATION, auth);
            webb.setDefaultHeader("Content-Type", "application/json");

            //webb.setBaseUri("https://mah-book-room-api.herokuapp.com");
            webb.setBaseUri("http://192.168.43.203:3000");

            Response<JSONObject> response = webb
                    .get("/bookings")
                    .asJsonObject();

            if(response.isSuccess()){
                JSONObject res = response.getBody();
                JSONArray bookingsArr = res.getJSONArray("bookings");
                List<BookingModel> bookings = new ArrayList<>();
                for (int i = 0; i < bookingsArr.length(); i++) {
                    JSONObject booking = bookingsArr.getJSONObject(i);
                    String date = booking.getString("date");
                    JSONObject embedded = booking.getJSONObject("_embedded");
                    String room = embedded.getJSONObject("room").getString("name");
                    String time = embedded.getJSONObject("time").getString("name");
                    bookings.add(new BookingModel(date, room, time));
                }
                return bookings;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ViewPagerAdapter getAdapter() {
        return this.adapter;
    }

    public void changeStateOfBookButton() {
        if (bookRoomFragment != null) {
            bookRoomFragment.changeStateOfButton();
        }
    }

    public void updateBookings(List<BookingModel> bookings) {
        if (bookingsFragment != null) {
            bookingsFragment.updateBookings(bookings);
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private Controller controller;

        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                bookRoomFragment = new BookRoomFragment();
                bookRoomFragment.setListener(mainActivity);
                return bookRoomFragment;
            } else {
                bookingsFragment = new BookingsFragment();
                return bookingsFragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
