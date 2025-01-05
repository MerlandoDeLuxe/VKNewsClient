package com.example.vknewsclient.data.model

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("response") val userDtos: List<UserDto>
) {
}