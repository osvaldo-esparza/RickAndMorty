package com.oseo27jul.rickandmorty.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class EpisodesResponse (
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results: List<Episodes>
){constructor() : this(Info(0, 0, null, null), emptyList())}

data class Episodes(
    @SerializedName("id") val id:Int,
    @SerializedName("name") val name: String,
    @SerializedName("air_date")val airDate: String,
    @SerializedName("episode")val episode:String,
    @SerializedName("characters")val characters:List<String>,
    @SerializedName("url") val url:String,
    @SerializedName("created") val created:String
): Parcelable {
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
        parcel.writeString(airDate)
        parcel.writeString(episode)
        parcel.writeStringList(characters)
        parcel.writeString(url)
        parcel.writeString(created)// Usar writeTypedList para listas de Parcelable
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Episodes> {
        override fun createFromParcel(parcel: Parcel): Episodes {
            return Episodes(parcel)
        }

        override fun newArray(size: Int): Array<Episodes?> {
            return arrayOfNulls(size)
        }
    }
}