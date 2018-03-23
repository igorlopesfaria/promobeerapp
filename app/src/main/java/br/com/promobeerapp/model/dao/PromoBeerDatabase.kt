package br.com.promobeerapp.model.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import br.com.promobeerapp.model.User

/**
 * Created by root on 22/03/18.
 */
@Database(entities = arrayOf(User::class), version = 1, exportSchema = false)
abstract class PromoBeerDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDAO
}
