package com.unito.models.entity;
// TODO: Lasciare solo tutor_id
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "reservations", foreignKeys = {
        @ForeignKey(entity = User.class,
                parentColumns = "username",
                childColumns = "username",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Tutor.class,
                parentColumns = "ID",
                childColumns = "tutor_id",
                onDelete = ForeignKey.NO_ACTION)})
public class Reservation {

    public static final String RESERVATION_STATUS_FREE = "free";
    public static final String RESERVATION_STATUS_RESERVED = "reserved";
    public static final String RESERVATION_STATUS_COMPLETED = "completed";
    public static final String RESERVATION_STATUS_DELETE = "deleted";
    public static final String RESERVATION_STATUS_HIDE = "hided";


    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int ID;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "tutor_id")
    private int tutorID;

    @ColumnInfo(name = "tutor_name")
    private String tutorName;

    @ColumnInfo(name = "tutor_surname")
    private String tutorSurname;

    @ColumnInfo(name = "course_name")
    private String course_name;

    @ColumnInfo(name = "slot")
    private int slot;

    @ColumnInfo(name = "reservation_status")
    private String reservationStatus;

    public Reservation(String username, int tutorID, String tutorName, String tutorSurname, String course_name, int slot, String reservationStatus) {
        this.username = username;
        this.tutorID = tutorID;
        this.tutorName = tutorName;
        this.tutorSurname = tutorSurname;
        this.course_name = course_name;
        this.slot = slot;
        this.reservationStatus = reservationStatus;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTutorID() {
        return tutorID;
    }

    public void setTutorID(int tutorID) {
        this.tutorID = tutorID;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getTutorSurname() {
        return tutorSurname;
    }

    public void setTutorSurname(String tutorSurname) {
        this.tutorSurname = tutorSurname;
    }

    public String getTutorFullName() {
        return tutorName + " " + tutorSurname;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public int getSlot() {
        return slot;
    }

    public String getDisplayableSlot() {
        int day = slot % 5;
        int hour = slot / 5;
        String dayVal = "";
        String hourVal = "";
        switch (day) {
            case 0:
                dayVal += "LUN ";
                break;
            case 1:
                dayVal += "MAR ";
                break;
            case 2:
                dayVal += "MER ";
                break;
            case 3:
                dayVal += "GIO ";
                break;
            case 4:
                dayVal += "VEN ";
                break;
        }
        switch (hour) {
            case 0:
                hourVal += "15:00";
                break;
            case 1:
                hourVal += "16:00";
                break;
            case 2:
                hourVal += "17:00";
                break;
            case 3:
                hourVal += "18:00";
                break;
            case 4:
                hourVal += "19:00";
                break;
        }


        return dayVal + hourVal;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "ID=" + ID +
                ", username='" + username + '\'' +
                ", tutorID=" + tutorID +
                ", tutorName='" + tutorName + '\'' +
                ", tutorSurname=" + tutorSurname +
                ", course_name='" + course_name + '\'' +
                ", slot=" + slot +
                ", reservation_status='" + reservationStatus + '\'' +
                '}';
    }

    public boolean isFree() {
        return getReservationStatus().equals(Reservation.RESERVATION_STATUS_FREE);
    }

    public boolean isReserved() {
        return getReservationStatus().equals(Reservation.RESERVATION_STATUS_RESERVED);
    }

    public boolean isCompleted() {
        return getReservationStatus().equals(Reservation.RESERVATION_STATUS_COMPLETED);
    }

    public boolean isDeleted() {
        return getReservationStatus().equals(Reservation.RESERVATION_STATUS_DELETE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return ID == that.ID &&
                tutorID == that.tutorID &&
                slot == that.slot &&
                Objects.equals(username, that.username) &&
                Objects.equals(tutorName, that.tutorName) &&
                Objects.equals(tutorSurname, that.tutorSurname) &&
                Objects.equals(course_name, that.course_name) &&
                Objects.equals(reservationStatus, that.reservationStatus);
    }

}
