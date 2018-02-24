package com.alex.devops.db;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by vynnykiakiv on 2/22/18.
 */

public class DateConverter {
    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
