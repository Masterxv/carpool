package com.campussay.carpool.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

import com.campussay.carpool.CarpoolApplication;
import com.campussay.carpool.utils.LogUtils;

import java.util.List;

/**
 * create by WenJinG on 2019/4/5
 */
public class RoomHelper {

    private static final String databaseName = "CarpoolDatabase 1.0";
    private static RoomHelper helper;
    private TestDatabase database;
    private TestDatabase.Builder builder;
    private  static int CarpoolDatabaseVersion = 2;



    private RoomHelper () {
        builder = Room.databaseBuilder(CarpoolApplication.getApplication(),
                TestDatabase.class,databaseName)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    //第一创建数据库的执行
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        CarpoolDatabaseVersion = 1;
                    }
                    //每次打开数据库的时候执行
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        LogUtils.d(CarpoolDatabaseVersion+"");
                        super.onOpen(db);
                    }
                }).allowMainThreadQueries();
    }

    public RoomHelper openCarpoolDatabase(){
        if(builder!=null){
            LogUtils.d(CarpoolDatabaseVersion+"");
            database = (TestDatabase) builder.build();
        }
        return this;
    }

    //获取数据库帮助类单例
    public static RoomHelper getInstance(){
        if(helper == null)
            synchronized (RoomHelper.class){
                if(helper == null){
                    helper = new RoomHelper();
                }
            }
        return helper;
    }


    public TestDatabase getDatabase(){
        if(database!=null){
            LogUtils.d(CarpoolDatabaseVersion+"");
            return database;
        }else {
            throw new NullPointerException("CarpoolDatabase does not exist !");
        }
    }

    //此方法不推荐使用，先留着
    public void changeTable(List<String>SQL){

    }

    //升级
    public void upgrade(final String SQL) {
        if (builder != null) {
            LogUtils.d(CarpoolDatabaseVersion+"");
            builder = builder.addMigrations(new Migration(CarpoolDatabaseVersion, ++CarpoolDatabaseVersion) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL(SQL);
                }
            });
        }
        database = (TestDatabase) builder.build();
        LogUtils.d(CarpoolDatabaseVersion+"");
    }

    //升级
    public void upgrade(final List<String> SQL)  {
        if(builder!=null){
            builder = builder.addMigrations(new Migration(CarpoolDatabaseVersion,++CarpoolDatabaseVersion) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    for(int i = 0;i<SQL.size();i++) {
                        database.execSQL(SQL.get(i));
                    }
                }
            });
        }
        database = (TestDatabase) builder.build();
    }
}

