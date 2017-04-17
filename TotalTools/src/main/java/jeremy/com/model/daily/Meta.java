package jeremy.com.model.daily;

import java.io.Serializable;

public class Meta implements Serializable {

    private String msg;
    private int status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                '}';
    }
}
