package com.baronzhang.android.weather.new_data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.baronzhang.android.weather.new_data.entity.LifeIndex;

import java.util.List;

@Dao
public interface LifeIndexDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateLifeIndexes(List<LifeIndex> lifeIndexes);

    @Delete
    void deleteLifeIndex(LifeIndex lifeIndex);

    @Update
    void updateLifeIndex(LifeIndex lifeIndex);

    @Query(value = "SELECT * FROM lifeindex WHERE cityId = :cityId")
    List<LifeIndex> queryLifeIndexes(String cityId);
}
