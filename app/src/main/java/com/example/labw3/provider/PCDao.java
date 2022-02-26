package com.example.labw3.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PCDao {

    @Query("SELECT * FROM PCs")
    LiveData<List<PC>> getAllPCs();

    @Query("SELECT * FROM PCs WHERE PCId=:id")
    PC getPCById(int id);

    @Query("SELECT * FROM PCs WHERE cpu=:cpu")
    List<PC> getPCByCPU(String cpu);

    @Query("SELECT * FROM PCs WHERE ram=:ram")
    List<PC> getPCByRAM(int ram);

    @Query("SELECT * FROM PCs WHERE ssd=:ssd")
    List<PC> getPCBySSD(String ssd);

    @Query("SELECT * FROM PCs WHERE motherBoard=:motherBoard")
    List<PC> getPCByMotherBoard(String motherBoard);

    @Query("SELECT * FROM PCs WHERE graphicsCard=:graphicsCard")
    List<PC> getPCByGraphicsCard(String graphicsCard);

    @Query("SELECT * FROM PCs WHERE price=:price")
    List<PC> getPCByPrice(Float price);

    @Query("SELECT COUNT(*) FROM PCs")
    LiveData<Integer> getNumOfPCs();

    @Insert
    void addPC(PC pc);

    @Query("DELETE FROM PCs WHERE PCId=:id")
    void deletePCById(int id);

    @Query("DELETE FROM PCs WHERE cpu=:cpu")
    void deletePCByCPU(String cpu);

    @Query("DELETE FROM PCs WHERE ram=:ram")
    void deletePCByRAM(int ram);

    @Query("DELETE FROM PCs WHERE ssd=:ssd")
    void deletePCBySSD(String ssd);

    @Query("DELETE FROM PCs WHERE motherBoard=:motherBoard")
    void deletePCByMotherBoard(String motherBoard);

    @Query("DELETE FROM PCs WHERE graphicsCard=:graphicsCard")
    void deletePCByGraphicsCard(String graphicsCard);

    @Query("DELETE FROM PCs WHERE price=:price")
    void deletePCByPrice(Float price);

    @Query("DELETE FROM PCs")
    void deleteAllPC();

    @Query("DELETE FROM PCs WHERE PCId = (SELECT MAX(PCId) FROM PCs)")
    void deleteLastPC();
}
