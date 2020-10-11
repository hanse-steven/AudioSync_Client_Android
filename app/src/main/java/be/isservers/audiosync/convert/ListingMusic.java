package be.isservers.audiosync.convert;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import  org.apache.commons.lang3.builder.ToStringBuilder;

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
}