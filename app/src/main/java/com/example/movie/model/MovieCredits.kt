package com.example.movie.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class MovieCredits(
    val cast: List<Cast>,
    val crew: List<Crew>
)

@JsonClass(generateAdapter = true)
data class Cast(
    val name: String,
    @Json(name = "profile_path")
    val imageProfile: String?,
    @Json(name = "known_for_department")
    val job: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString(),
        parcel.readString().orEmpty()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(imageProfile)
        parcel.writeString(job)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Cast> {
        override fun createFromParcel(parcel: Parcel): Cast = Cast(parcel)
        override fun newArray(size: Int): Array<Cast?> = arrayOfNulls(size)
    }
}

@JsonClass(generateAdapter = true)
data class Crew(
    val name: String,
    @Json(name = "profile_path")
    val imageProfile: String?,
    val job: String
)
