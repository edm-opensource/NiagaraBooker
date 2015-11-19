package edm_opensource.niagarabooker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Base64;

/**
 * Created by mattiaspernhult on 2015-11-19.
 */
public class Controller {

    private String titles[] = {"BOOK ROOM", "BOOKINGS"};

    private ViewPagerAdapter adapter;

    public Controller(FragmentManager fm) {
        this.adapter = new ViewPagerAdapter(fm);
    }

    public byte[] hashPassword(String originalPassword) {
        byte[] secret = Credentials.secretKey.getBytes();
        byte[] password = Base64.decode(originalPassword, Base64.DEFAULT);
        return xor(password, secret);
    }

    public String getPassword(byte[] password) {
        byte[] secret = Credentials.secretKey.getBytes();
        String originalPassword = Base64.encodeToString(xor(password, secret), Base64.DEFAULT);
        return originalPassword;
    }

    private byte[] xor(final byte[] input, final byte[] secret) {
        final byte[] output = new byte[input.length];
        if (secret.length == 0) {
            throw new IllegalArgumentException("empty security key");
        }
        int spos = 0;
        for (int pos = 0; pos < input.length; ++pos) {
            output[pos] = (byte) (input[pos] ^ secret[spos]);
            ++spos;
            if (spos >= secret.length) {
                spos = 0;
            }
        }
        return output;
    }

    public ViewPagerAdapter getAdapter() {
        return this.adapter;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private Controller controller;

        private ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                BookRoomFragment bookRoomFragment = new BookRoomFragment();
                return bookRoomFragment;
            } else {
                BookingsFragment bookingsFragment = new BookingsFragment();
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
