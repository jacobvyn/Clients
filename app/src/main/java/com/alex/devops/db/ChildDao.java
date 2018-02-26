package com.alex.devops.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ChildDao {
    @Insert
    void insert(Child child);

    @Query("SELECT * FROM children ORDER BY id ASC")
    List<Child> getAllChildren();

    @Query("SELECT * FROM children WHERE time_stamp > :date")
    List<Child> getAllChildrenAfter(long date);
}
