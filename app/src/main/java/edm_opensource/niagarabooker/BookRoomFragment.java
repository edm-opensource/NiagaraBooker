package edm_opensource.niagarabooker;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BookRoomFragment extends Fragment {
    public BookRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_room, container, false);

        Spinner rooms = (Spinner) v.findViewById(R.id.spinnerRooms);
        Spinner times = (Spinner) v.findViewById(R.id.spinnerTimes);
        Spinner dates = (Spinner) v.findViewById(R.id.spinnerDates);
        dates.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getDates()));

        return v;
    }

    private List<String> getDates() {
        List<String> dates = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        DateFormat dateFormat = new SimpleDateFormat("EEEEEEEEE, d MMMMMMMMMMM yyyy");
        for (int i = 0; i < 14; i++) {
            if (i == 0) {
                dates.add(i, "Today");
            } else if (i == 1) {
                dates.add(i, "Tomorrow");
            } else {
                dates.add(i, dateFormat.format(calendar.getTime()));
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }
}
