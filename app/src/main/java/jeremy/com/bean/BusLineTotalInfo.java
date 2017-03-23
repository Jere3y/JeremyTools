package jeremy.com.bean;

/**
 * Created by Xin on 2017/3/20 0020,13:24.
 * Bean类，封装一切路径信息
 */

public class BusLineTotalInfo {

    private String bus_line_number;
    private String start_station;
    private String bus_station_count;
    private String end_station;

    private String time;
    private String walk_dis;

    public BusLineTotalInfo(String bus_line_number,
                            String start_station,
                            String bus_station_count,
                            String end_station,
                            String time,
                            String walk_dis) {
        this.bus_line_number = bus_line_number;
        this.start_station = start_station;
        this.bus_station_count = bus_station_count;
        this.end_station = end_station;
        this.time = time;
        this.walk_dis = walk_dis;
    }

    public String getBus_line_number() {
        return bus_line_number;
    }

    public void setBus_line_number(String bus_line_number) {
        this.bus_line_number = bus_line_number;
    }

    public String getStart_station() {
        return start_station;
    }

    public void setStart_station(String start_station) {
        this.start_station = start_station;
    }

    public String getBus_station_count() {
        return bus_station_count;
    }

    public void setBus_station_count(String bus_station_count) {
        this.bus_station_count = bus_station_count;
    }

    public String getEnd_station() {
        return end_station;
    }

    public void setEnd_station(String end_station) {
        this.end_station = end_station;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWalk_dis() {
        return walk_dis;
    }

    public void setWalk_dis(String walk_dis) {
        this.walk_dis = walk_dis;
    }


}
