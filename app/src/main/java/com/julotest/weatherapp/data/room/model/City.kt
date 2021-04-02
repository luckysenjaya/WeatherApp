package com.lukeone.mydiary.data.local.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "cities"
)
data class City (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "cityId")
    var cityId: Int,
    @ColumnInfo(name = "cityName")
    var cityName: String?,
    @ColumnInfo(name = "country")
    var country: String?
) : Parcelable