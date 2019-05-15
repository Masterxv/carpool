package com.campussay.carpool.room;

import android.arch.persistence.room.RoomDatabase;

import com.campussay.carpool.ui.chat.sql.ChatRecordDao;
import com.campussay.carpool.ui.chat.sql.GroupAndSelfChatEntity;

/**
 * create by WenJinG on 2019/4/5
 */
@android.arch.persistence.room.Database
        (entities = {TestEntity.class, GroupAndSelfChatEntity.class}, version = 2, exportSchema = false)
public  abstract class TestDatabase extends RoomDatabase {

    public abstract TestDao getDAO();

    public abstract ChatRecordDao getChatRecordDao();

}
