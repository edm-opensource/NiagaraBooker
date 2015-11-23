package edm_opensource.niagarabooker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class BookingsFragment extends Fragment {

    private RecyclerViewAdapter adapter;

    public BookingsFragment() {

    }

    public void updateBookings(List<BookingModel> bookings) {
        if (adapter != null) {
            adapter.updateList(bookings);
        }
    }

    public void addBooking(BookingModel bookingModel) {
        if (adapter != null) {
            adapter.addBooking(bookingModel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bookings, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewTab1);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        BookingModel bookingModel1 = new BookingModel("2015-11-20", "NI:A0303", "15:15-17:00");
        BookingModel bookingModel2 = new BookingModel("2015-11-20", "NI:A0303", "15:15-17:00");
        BookingModel bookingModel3 = new BookingModel("2015-11-20", "NI:A0303", "15:15-17:00");

        List<BookingModel> bookings = new ArrayList<>();

        /*
        bookings.add(bookingModel1);
        bookings.add(bookingModel2);
        bookings.add(bookingModel3);
        */

        adapter = new RecyclerViewAdapter(bookings);
        recyclerView.setAdapter(adapter);

        return v;
    }
}
