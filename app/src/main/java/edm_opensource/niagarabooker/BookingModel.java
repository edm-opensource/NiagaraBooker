package edm_opensource.niagarabooker;

import android.util.Log;

/**
 * Created by mattiaspernhult on 2015-11-19.
 */
public class BookingModel {

    private String date;
    private String time;
    private String room;
    private StringBuilder dateAsPost;
    private StringBuilder timeAsPost;
    private StringBuilder roomAsPost;

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

    public void changeTimeToNormal() {
        if (this.time.contains("-") && this.time.contains(":")) {
            return;
        }
        StringBuilder builder = new StringBuilder(this.time);
        builder.insert(2, ':');
        builder.insert(5, '-');
        builder.insert(8, ':');
        Log.d("MainActivity", builder.toString());
        this.time = builder.toString();
    }

    public void changeRoomToNormal() {
        if (this.room.contains(":")) {
            return;
        }
        StringBuilder builder = new StringBuilder(this.room);
        builder.insert(2, ':');
        Log.d("MainActivity", builder.toString().toUpperCase());
        this.room = builder.toString().toUpperCase();
    }

    public String getDateAsPost() {
        if (dateAsPost == null) {
            dateAsPost = new StringBuilder(this.date);
            for (int i = 0; i < dateAsPost.length(); i++) {
                if (dateAsPost.charAt(i) == '-') {
                    dateAsPost.deleteCharAt(i);
                }
            }
        }
        return dateAsPost.toString();
    }

    public String getTimeAsPost() {
        if (timeAsPost == null) {
            timeAsPost = new StringBuilder(this.time);
            for (int i = 0; i < timeAsPost.length(); i++) {
                if (timeAsPost.charAt(i) == '-' || timeAsPost.charAt(i) == ':') {
                    timeAsPost.deleteCharAt(i);
                }
            }
        }
        return timeAsPost.toString();
    }

    public String getRoomAsPost() {
        if (roomAsPost == null) {
            roomAsPost = new StringBuilder(this.room);
            for (int i = 0; i < roomAsPost.length(); i++) {
                if (roomAsPost.charAt(i) == ':') {
                    roomAsPost.deleteCharAt(i);
                }
            }
        }
        return roomAsPost.toString().toLowerCase();
    }
}
