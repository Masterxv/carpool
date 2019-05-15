package com.campussay.carpool.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * create by WenJinG on 2019/4/5
 */
@Entity(tableName = "users2")
public class TestEntity {

     static final String tableName = "users2";
        @PrimaryKey(autoGenerate = true)//主键是否自动增长，默认为false
        private int id;
        private String name;
        private int age;
        private String phone_num;

      public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

}
