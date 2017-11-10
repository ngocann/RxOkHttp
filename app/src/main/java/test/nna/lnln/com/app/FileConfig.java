package test.nna.lnln.com.app;

import java.util.List;

/**
 * Created by DELL on 11/10/2017.
 */

public class FileConfig {

    private String msg;
    private int versioncode;
    private String link;
    private List<?> id;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<?> getId() {
        return id;
    }

    public void setId(List<?> id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FileConfig{" +
                "msg='" + msg + '\'' +
                ", versioncode=" + versioncode +
                ", link='" + link + '\'' +
                ", id=" + id +
                '}';
    }
}
