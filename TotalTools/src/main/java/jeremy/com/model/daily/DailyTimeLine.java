package jeremy.com.model.daily;

import java.io.Serializable;

public class DailyTimeLine implements Serializable {

    private Meta meta;
    private Response response;

    public Meta getMeta() {
        return meta;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public String toString() {
        return "DailyTimeLine{" +
                "meta=" + meta +
                ", response=" + response +
                '}';
    }
}
