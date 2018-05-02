package br.com.promobeerapp

import android.app.Application
import android.arch.persistence.room.Room
import br.com.promobeerapp.model.dao.PromoBeerDatabase
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * Created by root on 23/03/18.
 */
class PromoBeerApplication: Application() {

        companion object {
            var database: PromoBeerDatabase? = null
        }

        override fun onCreate() {
            super.onCreate()
            PromoBeerApplication.database =  Room.databaseBuilder(this, PromoBeerDatabase::class.java, "promobeer-db").build()
            Fresco.initialize(this)
        }
    }