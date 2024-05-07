package com.example.imageEditor.home

import com.example.imageEditor.model.CollectionModel
import com.example.imageEditor.repository.HomeRepository
import com.example.imageEditor.ui.home.HomeContract
import com.example.imageEditor.ui.home.HomePresenter
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomePresenterTest {
    private var mHomeRepository = mockk<HomeRepository>(relaxed = true)
    private var mView = mockk<HomeContract.View>(relaxed = true)
    private val mHomePresenter = HomePresenter(mHomeRepository)

    @Before
    fun setUp() {
        mHomePresenter.setView(mView)
        mHomePresenter.onStart()
    }

    @After
    fun stop() {
        mHomePresenter.onStop()
    }

    @Test
    fun `getCollections when result is not null should call onSuccess`() {
        val page = 0
        val result = emptyList<CollectionModel>()
        val onResultSlot = slot<(List<CollectionModel>?) -> Unit>()
        every {
            mHomeRepository.getCollections(page, capture(onResultSlot))
        }
            .answers {
                onResultSlot.captured.invoke(result)
            }
        mHomePresenter.getCollections(0)
        verify {
            mView.setupCollections(result)
        }
    }

    @Test
    fun `getCollections when result is null should view not call `() {
        val page = 0
        val result = null
        val onResultSlot = slot<(List<CollectionModel>?) -> Unit>()
        every {
            mHomeRepository.getCollections(page, capture(onResultSlot))
        }
            .answers {
                onResultSlot.captured.invoke(result)
            }
        mHomePresenter.getCollections(0)
        verify {
            mView wasNot called
        }
    }

    @Test
    fun `likeImage when unauthorized should call go to authorize`() {
        val id = ""
        val onResultSlot = slot<() -> Unit>()
        every {
            mHomeRepository.likeImage(id, onFailure = capture(onResultSlot))
        }.answers {
            onResultSlot.captured.invoke()
        }
        mHomePresenter.likeImage(id)
        verify {
            mView.gotoAuthorize()
        }
    }

    @Test
    fun `likeImage when authorized should not call view`() {
        val id = ""
        val onResultSlot = slot<() -> Unit>()
        every {
            mHomeRepository.likeImage(id, onFailure = capture(onResultSlot))
        }.answers {
            onResultSlot.clear()
        }
        mHomePresenter.likeImage(id)
        verify {
            mView wasNot called
        }
    }
}
