package com.alex.devops.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ClientDao {
    @Insert
    void insert(Client client);

    @Query("SELECT * FROM clients ORDER BY id ASC")
    List<Client> getAllClients();

    @Query("SELECT * FROM clients WHERE time_stamp > :date")
    List<Client> getAllClientsAfter(long date);

//    @Query("SELECT * FROM clients WHERE main_second_name LIKE :search OR main_phone_number LIKE :search")
    List<Client> getClientsLike(String search);
}
