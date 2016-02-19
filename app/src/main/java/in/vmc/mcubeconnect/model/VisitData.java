package in.vmc.mcubeconnect.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mukesh on 8/1/16.
 */
public class VisitData implements Parcelable {

    private String sitename;
    private String siteid;
    private String siteicon;
    private String sitedesc;
    private String number;
    private String offer;
    private String bid;
    private boolean delete;
    private ArrayList<OptionsData> optionsData = new ArrayList<OptionsData>();
    private String offer_desc;
    private boolean Like;

    public ArrayList<OptionsData> getOptionsData() {
        return optionsData;
    }

    public void setOptionsData(ArrayList<OptionsData> optionsData) {
        this.optionsData = optionsData;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getOffer_desc() {
        return offer_desc;
    }

    public void setOffer_desc(String offer_desc) {
        this.offer_desc = offer_desc;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public boolean isLike() {
        return Like;
    }

    public void setLike(boolean like) {
        Like = like;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getSiteicon() {
        return siteicon;
    }

    public void setSiteicon(String siteicon) {
        this.siteicon = siteicon;
    }

    public String getSitedesc() {
        return sitedesc;
    }

    public void setSitedesc(String sitedesc) {
        this.sitedesc = sitedesc;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(sitedesc);
        out.writeString(number);
        out.writeString(offer);
        out.writeString(bid);
        out.writeList(optionsData);
        out.writeString(offer_desc);
        out.writeByte((byte) (Like ? 1 : 0));
        out.writeString(sitename);
        out.writeString(siteid);
        out.writeString(siteicon);
    }
}
