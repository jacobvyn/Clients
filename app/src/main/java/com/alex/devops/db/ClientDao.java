package com.alex.devops.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ClientDao {
    @Insert
    void insert(Client client);

    @Update
    void update(Client client);

    @Query("SELECT * FROM clients ORDER BY id ASC")
    List<Client> getAllClients();

    @Query("SELECT * FROM clients WHERE time_stamp > :dateNow")
    List<Client> getAllClientsAfter(long dateNow);

    @Query("SELECT * FROM clients WHERE" +
            " main_second_name LIKE :search OR main_phone_number LIKE :search OR" +
            " second_parent_second_name LIKE  :search OR second_phone_number LIKE :search")
    List<Client> getClientsLike(String search);

}
