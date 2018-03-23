package br.com.promobeerapp.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import java.io.Serializable

/**
 * Created by root on 14/03/18.
 */
@Entity(tableName = "user")
data class User( @ColumnInfo(name = "id_user") var uid: String,
                @ColumnInfo(name = "name_user") var name: String,
                @ColumnInfo(name = "email_user")var email: String,
                @ColumnInfo(name = "image_path_user")var imagePath: String
                ): Serializable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}