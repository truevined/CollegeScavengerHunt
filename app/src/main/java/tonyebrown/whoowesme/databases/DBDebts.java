package tonyebrown.whoowesme.databases;

import android.media.Image;
import android.widget.Spinner;

import java.sql.Blob;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Tonye Brown on 11/30/2015.
 */
public class DBDebts extends RealmObject {
    private byte userImage;
    private String Name;
    private String debtCategory;
    private String debtMemo;
    private Date dueDate;
    private Boolean setReminder;
    private int owing;
    private String menu;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDebtCategory() {
        return debtCategory;
    }

    public void setDebtCategory(String debtCategory) {
        this.debtCategory = debtCategory;
    }

    public String getDebtMemo() {
        return debtMemo;
    }

    public void setDebtMemo(String debtMemo) {
        this.debtMemo = debtMemo;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getSetReminder() {
        return setReminder;
    }

    public void setSetReminder(Boolean setReminder) {
        this.setReminder = setReminder;
    }

    public int getOwing() {
        return owing;
    }

    public void setOwing(int owing) {
        this.owing = owing;
    }

    public byte getUserImage() {
        return userImage;
    }

    public void setUserImage(byte userImage) {
        this.userImage = userImage;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
