package com.wechantloup.side.modules.details

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.wechantloup.side.domain.bean.ToiletBean
import com.wechantloup.side.domain.usecase.AddFavoriteUseCase
import com.wechantloup.side.domain.usecase.RemoveFavoriteUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit

class CompanyReviewPresenterUTest {

    @get:Rule
    val unitOfWorkRule = MockitoJUnit.rule()!!

    @Mock
    private lateinit var mView: DetailsContract.View

    @Mock
    private lateinit var mRouter: DetailsContract.Router

    @Mock
    private lateinit var mAddFavoriteUseCase: AddFavoriteUseCase

    @Mock
    private lateinit var mRemoveFavoriteUseCase: RemoveFavoriteUseCase

    private lateinit var mPresenter: DetailsPresenter

    @Before
    @Throws(Exception::class)
    fun setUp() {

        mPresenter = DetailsPresenter(
            mRouter,
            mAddFavoriteUseCase,
            mRemoveFavoriteUseCase
        )
        mPresenter.subscribe(mView)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mPresenter.unsubscribe(mView)
    }

    @Test
    fun addFavorite_success() {
        // Given
        val toilet = ToiletBean()
        mPresenter.setToilet(toilet)
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as DetailsPresenter.FavoriteSubscriber
            subscriber.onComplete()
            null
        }.`when`(mAddFavoriteUseCase).execute(any<DetailsPresenter.FavoriteSubscriber>(), any())

        // When
        mPresenter.setFavorite(true)

        // Verify
        assert(toilet.isFavorite)
        verifyZeroInteractions(mView)
    }

    @Test
    fun addFavorite_error() {
        // Given
        val toilet = ToiletBean()
        mPresenter.setToilet(toilet)
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as DetailsPresenter.FavoriteSubscriber
            subscriber.onError(Throwable())
            null
        }.`when`(mAddFavoriteUseCase).execute(any<DetailsPresenter.FavoriteSubscriber>(), any())

        // When
        mPresenter.setFavorite(true)

        // Verify
        assert(!toilet.isFavorite)
        verify(mView).notifyErrorModifyingFavorite(toilet)
        verify(mView).notifyError()
        verifyNoMoreInteractions(mView)
    }

    @Test
    fun removeFavorite_success() {
        // Given
        val toilet = ToiletBean()
        toilet.isFavorite = true
        mPresenter.setToilet(toilet)
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as DetailsPresenter.FavoriteSubscriber
            subscriber.onComplete()
            null
        }.`when`(mRemoveFavoriteUseCase).execute(any<DetailsPresenter.FavoriteSubscriber>(), any())

        // When
        mPresenter.setFavorite(false)

        // Verify
        assert(!toilet.isFavorite)
        verifyZeroInteractions(mView)
    }

    @Test
    fun removeFavorite_error() {
        // Given
        val toilet = ToiletBean()
        toilet.isFavorite = true
        mPresenter.setToilet(toilet)
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as DetailsPresenter.FavoriteSubscriber
            subscriber.onError(Throwable())
            null
        }.`when`(mRemoveFavoriteUseCase).execute(any<DetailsPresenter.FavoriteSubscriber>(), any())

        // When
        mPresenter.setFavorite(false)

        // Verify
        assert(toilet.isFavorite)
        verify(mView).notifyErrorModifyingFavorite(toilet)
        verify(mView).notifyError()
        verifyNoMoreInteractions(mView)
    }
}