package jeremy.com.bean;

/**
 * 封装每个文件的信息，根据文件名排序后放入
 * Created by Xin on 2017/3/21 0021,15:56.
 */

public class FileInfo {
    private String path;
    private String name;
    private boolean isDir;


    public FileInfo(String path, String name, boolean isDir) {
        this.path = path;
        this.name = name;
        this.isDir = isDir;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }
}
