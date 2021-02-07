package com.unito.models.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "tutors", indices = {@Index(value = {"name", "surname"},
        unique = true)})
public class Tutor {


    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @NonNull
    @ColumnInfo(name = "name")
    private String Name;

    @NonNull
    @ColumnInfo(name = "surname")
    private String Surname;

    public Tutor(String name, String surname) {
        Name = name;
        Surname = surname;
    }

    public Tutor(int id, String name, String surname) {
        ID = id;
        Name = name;
        Surname = surname;
    }

    public Tutor() {
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getSurname() {
        return Surname;
    }

    public String getTutorCompleteName() {
        return getName() + " " + getSurname();
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    @NotNull
    @Override
    public String toString() {
        return "Tutor{" +
                "ID=" + ID +
                ", Name='" + Name + '\'' +
                ", Surname='" + Surname + '\'' +
                '}';
    }
}
