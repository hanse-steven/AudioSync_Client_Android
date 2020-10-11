package be.isservers.audiosync.convert;

import android.view.MenuItem;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import  org.apache.commons.lang3.builder.ToStringBuilder;

import be.isservers.audiosync.R;

public class ListingMusic implements Serializable {

    @SerializedName("ToKeep")
    @Expose
    private List<Music> toKeep = null;
    @SerializedName("ToDelete")
    @Expose
    private List<Music> toDelete = null;
    @SerializedName("ToDownload")
    @Expose
    private List<Music> toDownload = null;
    @SerializedName("SizeToDownload")
    @Expose
    private String sizeToDownload = null;

    public final static int TO_KEEP = 1;
    public final static int TO_DELETE = 2;
    public final static int TO_DOWNLOAD = 3;

    /**
     * No args constructor for use in serialization
     *
     */
    public ListingMusic() {
    }

    public List<Music> getToKeep() { return toKeep; }
    public List<Music> getToDelete() { return toDelete; }
    public List<Music> getToDownload() { return toDownload; }
    public String getSizeToDownload() { return sizeToDownload; }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("toKeep", toKeep).append("toDelete", toDelete).append("toDownload", toDownload).append("sizeToDownload",sizeToDownload).toString();
    }

    public boolean searchMusic(int number_list, String val){

        if (number_list == TO_KEEP){
            for (Music keep : this.getToKeep()) {
                if (keep.getHash().contains(val)) return true;
            }
        }
        else if (number_list == TO_DELETE){
            for (Music delete : this.getToDelete()) {
                if (delete.getHash().contains(val)) return true;
            }
        }
        else if (number_list == TO_DOWNLOAD){
            for (Music download : this.getToDownload()) {
                if (download.getHash().contains(val)) return true;
            }
        }

        return false;
    }
}