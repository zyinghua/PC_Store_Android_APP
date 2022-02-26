package com.example.labw3.provider;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = PC.TABLE_NAME)
public class PC {
    public static final String TABLE_NAME = "PCs";

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "PCId")
    private int id;
    @ColumnInfo(name = "cpu")
    private String cpu;
    @ColumnInfo(name  = "ram")
    private int ram;
    @ColumnInfo(name = "ssd")
    private int ssd;
    @ColumnInfo(name = "motherBoard")
    private String motherBoard;
    @ColumnInfo(name = "graphicsCard")
    private String graphicsCard;
    @ColumnInfo(name = "price")
    private Float price;

    public PC(String cpu, int ram, int ssd, String motherBoard, String graphicsCard, Float price)
    {
        this.cpu = cpu;
        this.ram = ram;
        this.ssd = ssd;
        this.motherBoard = motherBoard;
        this.graphicsCard = graphicsCard;
        this.price = price;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public int getId() {
        return id;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public void setSsd(int ssd) {
        this.ssd = ssd;
    }

    public void setMotherBoard(String motherBoard) {
        this.motherBoard = motherBoard;
    }

    public void setGraphicsCard(String graphicsCard) {
        this.graphicsCard = graphicsCard;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpu() {
        return cpu;
    }

    public int getRam() {
        return ram;
    }

    public int getSsd() {
        return ssd;
    }

    public String getMotherBoard() {
        return motherBoard;
    }

    public String getGraphicsCard() {
        return graphicsCard;
    }

    public Float getPrice() {
        return price;
    }
}
