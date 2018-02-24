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

    @Query("SELECT * FROM clients WHERE second_name LIKE :search")
    List<Client> getClientsLike(String search);
}
