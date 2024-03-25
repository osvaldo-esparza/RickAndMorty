package com.oseo27jul.rickandmorty.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class LocationResponse (
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results:List<Locations>

) {constructor() : this(Info(0, 0, null, null), emptyList())}



data class Locations(
@SerializedName("id")val id:Int,
    @SerializedName("name") val name:String,
    @SerializedName("type") val type:String,
    @SerializedName("dimsension") val dimension:String,
    @SerializedName("residents") val residents: List<String>,
    @SerializedName("url") val url:String,
    @SerializedName("created") val created:String
)
    : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeString(dimension)
        parcel.writeStringList(residents)
        parcel.writeString(url)
        parcel.writeString(created)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Locations> {
        override fun createFromParcel(parcel: Parcel): Locations {
            return Locations(parcel)
        }

        override fun newArray(size: Int): Array<Locations?> {
            return arrayOfNulls(size)
        }
    }
}


