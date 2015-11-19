package edm_opensource.niagarabooker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mattiaspernhult on 2015-11-19.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<BookingModel> bookings;

    public RecyclerViewAdapter(List<BookingModel> b) {
        this.bookings = b;
    }

    public void updateList(List<BookingModel> b) {
        this.bookings = b;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvRoom.setText(bookings.get(position).getRoom());
        holder.tvTime.setText(bookings.get(position).getTime());
        holder.tvDate.setText(bookings.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        if (bookings != null) {
            return bookings.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRoom;
        private TextView tvDate;
        private TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRoom = (TextView) itemView.findViewById(R.id.tvRoom);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
