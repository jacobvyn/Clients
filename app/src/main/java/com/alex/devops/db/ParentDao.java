package com.alex.devops.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ParentDao {
    @Insert
    void insert(Parent parent);

    @Query("SELECT * FROM parents ORDER BY id")
    List<Parent> getAllParents();

    @Query("SELECT * FROM parents WHERE time_stamp > :date")
    List<Parent> getAllParentsAfter(long date);
}
