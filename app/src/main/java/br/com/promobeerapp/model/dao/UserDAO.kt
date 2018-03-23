package br.com.promobeerapp.model.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import br.com.promobeerapp.model.User
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by root on 23/03/18.
 */
@Dao
interface UserDAO {

    @Query("select * from user")
    fun getUser(): Single<User>

    @Insert(onConflict = REPLACE)
    fun insertUser(user: User)

    @Update(onConflict = REPLACE)
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}