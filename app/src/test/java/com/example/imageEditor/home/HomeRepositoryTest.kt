package com.example.imageEditor.home

import com.example.imageEditor.apiService.NetworkService
import com.example.imageEditor.base.OnListenProcess
import com.example.imageEditor.model.CollectionModel
import com.example.imageEditor.repository.HomeRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.invoke
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Test

class HomeRepositoryTest {
    private val mockNetworkService = mockk<NetworkService>(relaxed = true)
    private val mockOnListenProcess = mockk<OnListenProcess>(relaxed = true)
    private val onFailureMock = mockk<() -> Unit>(relaxUnitFun = true)
    private val homeRepository = HomeRepository(mockOnListenProcess)

    @Test
    fun `test getCollections when result have value should invoke onResult`() {
        // Dữ liệu mẫu cho unit test
        val page = 1
        val expectedResult = emptyList<CollectionModel>()
        val onResultCallbackSlot = slot<(List<CollectionModel>?) -> Unit>()
        // Thiết lập mock cho phương thức getCollections của NetworkService
        every { mockNetworkService.getCollections(page, capture(onResultCallbackSlot)) }.answers {
            onResultCallbackSlot.captured.invoke(expectedResult)
        }

        // Gọi phương thức getCollections của HomeRepository với page là đầu vào
        homeRepository.getCollections(page) { result ->
            assertEquals(expectedResult, result)
        }
    }

    @Test
    fun `test likeImage when unauthorized should call onFailure`() {
        val id = ""
        val onFailureSlot = slot<() -> Unit>()
        every { mockNetworkService.likeImage(id, capture(onFailureSlot)) }
            .answers {
                onFailureSlot.captured.invoke()
            }

        every { onFailureMock.invoke() } just Runs
        // Gọi phương thức likeImage của HomeRepository với id là đầu vào
        homeRepository.likeImage(id, onFailureMock)

        verify {
            onFailureMock.invoke()
        }
    }
}
