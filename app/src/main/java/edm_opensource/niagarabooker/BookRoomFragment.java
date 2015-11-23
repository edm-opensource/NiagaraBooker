package edm_opensource.niagarabooker;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class BookRoomFragment extends Fragment {

    private Spinner dates;
    private Spinner rooms;
    private Spinner times;
    private Button btnBook;
    private CustomClickListener listener;

    public BookRoomFragment() {
        // Required empty public constructor
    }

    public void setListener(CustomClickListener l) {
        this.listener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_room, container, false);

        rooms = (Spinner) v.findViewById(R.id.spinnerRooms);
        times = (Spinner) v.findViewById(R.id.spinnerTimes);
        dates = (Spinner) v.findViewById(R.id.spinnerDates);
        btnBook = (Button) v.findViewById(R.id.btnBook);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String room = rooms.getSelectedItem().toString();
                String time = times.getSelectedItem().toString();
                String date = dates.getSelectedItem().toString();
                date = encodeDate(date);
                BookingModel bookingModel = new BookingModel(date, room, time);
                if (listener != null) {
                    listener.onBook(bookingModel);
                }
            }
        });

        dates.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getDates()));

        return v;
    }

    private String encodeDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("EEEEEEEEE, d MMMMMMMMMMM yyyy");
        try {
            Date parsed = dateFormat.parse(date);
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> getDates() {
        List<String> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        DateFormat dateFormat = new SimpleDateFormat("EEEEEEEEE, d MMMMMMMMMMM yyyy");
        for (int i = 0; i < 14; i++) {
            dates.add(i, dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }

    public void changeStateOfButton() {
        if (btnBook.isEnabled()) {
            btnBook.setEnabled(false);
        } else {
            btnBook.setEnabled(true);
        }
    }
}
