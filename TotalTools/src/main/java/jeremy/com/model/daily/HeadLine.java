package jeremy.com.model.daily;

import java.io.Serializable;

public class HeadLine implements Serializable {

    private String description;
    private String title;

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "HeadLine{" +
                "description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
