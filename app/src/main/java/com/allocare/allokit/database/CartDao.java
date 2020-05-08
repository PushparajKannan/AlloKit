package com.allocare.allokit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.allocare.allokit.cartmodule.CartModel;

import java.util.List;

@Dao
public interface CartDao {

    @Query("SELECT * FROM Cart ORDER BY ID")
    LiveData<List<CartModel>> loadAllPersons();

    @Insert
    void insertPerson(CartModel person);

    @Update
    void updatePerson(CartModel person);

    @Delete
    void delete(CartModel person);

    @Query("SELECT * FROM Cart WHERE id = :id")
    CartModel loadPersonById(int id);

    @Query("SELECT * FROM Cart WHERE isSelected = :type")
    LiveData<List<CartModel>> getCartViews(boolean type);
}
