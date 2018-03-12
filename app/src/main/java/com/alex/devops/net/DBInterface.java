package com.alex.devops.net;

import com.alex.devops.db.Client;

import java.util.List;

public interface DBInterface {

    List<Client> getAllClientsSyncAfter(long time);

    void deleteAllSync();

    void insertAllSync(List<Client> clientList);
}
