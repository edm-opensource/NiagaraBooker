package edm_opensource.niagarabooker;

/**
 * Created by mattiaspernhult on 2015-11-19.
 */
public class BookingModel {

    private String date;
    private String time;
    private String room;

    public BookingModel(String date, String room, String time) {
        this.date = date;
        this.room = room;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getRoom() {
        return room;
    }

    public String getTime() {
        return time;
    }
}
