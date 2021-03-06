package com.project.aidoctor.data.repository.profile

import com.project.aidoctor.data.remote.profile.DeleteTokenResponse
import com.project.aidoctor.data.remote.profile.LogoutResponse
import com.project.aidoctor.data.remote.profile.ProfileService
import com.project.aidoctor.data.repository.BaseRepository

class ProfileRepository(private val profileService: ProfileService) : BaseRepository() {
    suspend fun logout(): LogoutResponse {
        return apiRequest { profileService.logout() }
    }
    suspend fun deleteToken(userId:Int): DeleteTokenResponse {
        return apiRequest { profileService.deleteToken(userId) }
    }
}
