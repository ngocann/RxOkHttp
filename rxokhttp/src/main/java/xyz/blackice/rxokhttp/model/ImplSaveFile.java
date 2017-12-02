package xyz.blackice.rxokhttp.model;

/**
 * Created by Ann on 11/18/17.
 */

public interface ImplSaveFile {

    String getUrlFile();
    void progress(int percent);
    void isCompleted(boolean isCompleted);

}
