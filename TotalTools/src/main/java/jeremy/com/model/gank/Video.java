package jeremy.com.model.gank;

import java.io.Serializable;
import java.util.List;

public class Video implements Serializable {

    private boolean error;
    private List<Gank> results;

    public boolean isError() {
        return error;
    }

    public List<Gank> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "Video{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
