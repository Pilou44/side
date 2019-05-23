package com.wechantloup.side.modules.list

import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.any
import com.wechantloup.side.domain.bean.ToiletsBean
import com.wechantloup.side.domain.usecase.AddFavoriteUseCase
import com.wechantloup.side.domain.usecase.GetFavoritesUseCase
import com.wechantloup.side.domain.usecase.GetToiletsUseCase
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
    private lateinit var mView: ListContract.View

    @Mock
    private lateinit var mGetToiletsUseCase: GetToiletsUseCase

    @Mock
    private lateinit var mGetFavoritesUseCase: GetFavoritesUseCase

    @Mock
    private lateinit var mAddFavoriteUseCase: AddFavoriteUseCase

    @Mock
    private lateinit var mRemoveFavoriteUseCase: RemoveFavoriteUseCase

    private lateinit var mPresenter: ListPresenter

    @Before
    @Throws(Exception::class)
    fun setUp() {

        mPresenter = ListPresenter(
            mGetToiletsUseCase,
            mGetFavoritesUseCase,
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
    fun retrieveToiletsList_success() {
        // Given
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.GetFavoritesSubscriber
            subscriber.onNext(emptyList())
            null
        }.`when`(mGetFavoritesUseCase).execute(any<ListPresenter.GetFavoritesSubscriber>())

        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.GetToiletsSubscriber
            subscriber.onNext(ArrayList())
            null
        }.`when`(mGetToiletsUseCase).execute(any<ListPresenter.GetToiletsSubscriber>())

        // When
        mPresenter.retrieveToiletsList(false, LatLng(0.0, 0.0))

        // Verify
        verify(mView).notifyToiletsListRetrieved()
    }

    @Test
    fun addFavorite_success() {
        // Given
        val toilet = ToiletsBean()
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.FavoriteSubscriber
            subscriber.onComplete()
            null
        }.`when`(mAddFavoriteUseCase).execute(any<ListPresenter.FavoriteSubscriber>(), any())

        // When
        mPresenter.setFavorite(toilet, true)

        // Verify
        assert(toilet.isFavorite)
    }

    @Test
    fun removeFavorite_success() {
        // Given
        val toilet = ToiletsBean()
        toilet.isFavorite = true
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.FavoriteSubscriber
            subscriber.onComplete()
            null
        }.`when`(mRemoveFavoriteUseCase).execute(any<ListPresenter.FavoriteSubscriber>(), any())

        // When
        mPresenter.setFavorite(toilet, false)

        // Verify
        assert(!toilet.isFavorite)
    }
}