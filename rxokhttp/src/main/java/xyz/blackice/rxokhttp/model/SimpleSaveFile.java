package xyz.blackice.rxokhttp.model;

/**
 * Created by Ann on 12/2/17.
 */

public class SimpleSaveFile implements ImplSaveFile {
    private int percent;
    private boolean isCompleted;
    private String url;

    public SimpleSaveFile(String url) {
        this.url = url;
    }

    @Override
    public String getUrlFile() {
        return url;
    }

    @Override
    public void progress(int percent) {

        this.percent = percent;
    }

    @Override
    public void isCompleted(boolean isCompleted) {

        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return "SimpleSaveFile{" +
                "percent=" + percent +
                ", isCompleted=" + isCompleted +
                ", url='" + url + '\'' +
                '}';
    }
}
