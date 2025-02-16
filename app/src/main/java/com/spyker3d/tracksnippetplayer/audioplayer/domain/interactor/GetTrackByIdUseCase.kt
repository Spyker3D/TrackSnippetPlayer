package com.spyker3d.tracksnippetplayer.audioplayer.domain.interactor

import com.spyker3d.tracksnippetplayer.audioplayer.domain.repository.GetTrackByIdRepository
import com.spyker3d.tracksnippetplayer.common.domain.model.Track
import javax.inject.Inject

class GetTrackByIdUseCase @Inject constructor(private val getTrackByIdRepository: GetTrackByIdRepository) {
    suspend fun getTrackById(trackId: Long): Track = getTrackByIdRepository.getTrackById(trackId)
}