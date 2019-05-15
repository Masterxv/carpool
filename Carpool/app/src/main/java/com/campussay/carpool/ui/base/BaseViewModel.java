package com.campussay.carpool.ui.base;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.campussay.carpool.room.TestEntity;

import java.util.List;

/**
 * create by WenJinG on 2019/4/6
 */

//此类用于存储数据，在同一个Activity的不同fragment可以共享数据
public class BaseViewModel extends ViewModel {
    private MutableLiveData<List<TestEntity>>listTestEntities;

    public MutableLiveData<List<TestEntity>> getListTestEntities() {
        if(listTestEntities == null)
            listTestEntities = new MutableLiveData<>();
        return listTestEntities;
    }
}
