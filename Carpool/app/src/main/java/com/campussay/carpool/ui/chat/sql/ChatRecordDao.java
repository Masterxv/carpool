package com.campussay.carpool.ui.chat.sql;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * create by zuyuan on 2019/4/16
 */
@Dao
public interface ChatRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSelfChatRecord(GroupAndSelfChatEntity... entities);

    @Query("DELETE FROM self_chat_table " +
            "WHERE account_id= :accountId AND friend_id= :friendId")
    void deleteSelfChatBeforeRecord(long accountId, long friendId);

    @Query("SELECT * FROM self_chat_table " +
            "WHERE account_id= :accountId AND friend_id= :friendId " +
            "ORDER BY key_id")
    GroupAndSelfChatEntity[] querySelfChatRecord(long accountId, long friendId);

}
