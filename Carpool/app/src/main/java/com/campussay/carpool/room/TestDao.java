package com.campussay.carpool.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * 这个接口来可以用来被继承
 * create by WenJinG on 2019/4/5
 */
@Dao
public interface TestDao {

    //@Embedded 此注解可以拆封其他实体
    //@ignore 注解忽视被注解的变量

    String simpleSearch = "SELECT * FROM "+TestEntity.tableName;
    String complexSearch= "SELECT * FROM "+TestEntity.tableName + " WHERE id = :id";
    /**
     * 在表中插入一元或多元数据
     * @param entities
     * 这里可以有返回值，插入一元数据则返回概述句对应的ID（类型为Long）
     * 若插入多元数据，则应返回ID构成的数组（Long[]）或List
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntity(TestEntity... entities);


    /**
     * 更新表中实体的数据，通过匹配主键来找到目标实体
     * @param entities
     * 可以用int来返回表中更新的行数
     */
    @Update
    void updateEntity(TestEntity... entities);


    /**
     * 删除数据,通过匹配主键来找到目标实体
     * @param entities
     */
    @Delete
    void deleteEntity(TestEntity... entities);


    @Query(simpleSearch)
    LiveData<List<TestEntity>> queryEntities();

    @Query(complexSearch)
    LiveData<List<TestEntity>> queryComplex(String id);




}
