package com.wechantloup.side.modules.list

import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.wechantloup.side.domain.bean.ToiletBean
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
    private lateinit var mRouter: ListContract.Router

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
            mRouter,
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
        verifyNoMoreInteractions(mView)
    }

    @Test
    fun retrieveToiletsList_error() {
        // Given
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.GetFavoritesSubscriber
            subscriber.onNext(emptyList())
            null
        }.`when`(mGetFavoritesUseCase).execute(any<ListPresenter.GetFavoritesSubscriber>())

        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.GetToiletsSubscriber
            subscriber.onError(Throwable())
            null
        }.`when`(mGetToiletsUseCase).execute(any<ListPresenter.GetToiletsSubscriber>())

        // When
        mPresenter.retrieveToiletsList()

        // Verify
        verify(mView).notifyToiletsListRetrieved()
        verify(mView).notifyError()
        verifyNoMoreInteractions(mView)
    }

    @Test
    fun retrieveFavorites_success() {
        // Given
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.GetFavoritesSubscriber
            subscriber.onNext(emptyList())
            null
        }.`when`(mGetFavoritesUseCase).execute(any<ListPresenter.GetFavoritesSubscriber>())

        // When
        mPresenter.retrieveToiletsList()

        // Verify
        verify(mGetToiletsUseCase).execute(any<ListPresenter.GetToiletsSubscriber>())
        verifyNoMoreInteractions(mGetToiletsUseCase)
        verifyZeroInteractions(mView)
    }

    @Test
    fun retrieveFavorites_error() {
        // Given
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.GetFavoritesSubscriber
            subscriber.onError(Throwable())
            null
        }.`when`(mGetFavoritesUseCase).execute(any<ListPresenter.GetFavoritesSubscriber>())

        // When
        mPresenter.retrieveToiletsList()

        // Verify
        verify(mGetToiletsUseCase).execute(any<ListPresenter.GetToiletsSubscriber>())
        verifyNoMoreInteractions(mGetToiletsUseCase)
        verify(mView).notifyError()
        verifyNoMoreInteractions(mView)
    }

    @Test
    fun addFavorite_success() {
        // Given
        val toilet = ToiletBean()
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.FavoriteSubscriber
            subscriber.onComplete()
            null
        }.`when`(mAddFavoriteUseCase).execute(any<ListPresenter.FavoriteSubscriber>(), any())

        // When
        mPresenter.setFavorite(toilet)

        // Verify
        assert(toilet.isFavorite)
    }

    @Test
    fun addFavorite_error() {
        // Given
        val toilet = ToiletBean()
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.FavoriteSubscriber
            subscriber.onError(Throwable())
            null
        }.`when`(mAddFavoriteUseCase).execute(any<ListPresenter.FavoriteSubscriber>(), any())

        // When
        mPresenter.setFavorite(toilet)

        // Verify
        assert(!toilet.isFavorite)
        verify(mView).notifyItemModified(any())
        verify(mView).notifyError()
        verifyNoMoreInteractions(mView)
    }

    @Test
    fun removeFavorite_success() {
        // Given
        val toilet = ToiletBean()
        toilet.isFavorite = true
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.FavoriteSubscriber
            subscriber.onComplete()
            null
        }.`when`(mRemoveFavoriteUseCase).execute(any<ListPresenter.FavoriteSubscriber>(), any())

        // When
        mPresenter.setFavorite(toilet)

        // Verify
        assert(!toilet.isFavorite)
    }

    @Test
    fun removeFavorite_error() {
        // Given
        val toilet = ToiletBean()
        toilet.isFavorite = true
        doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as ListPresenter.FavoriteSubscriber
            subscriber.onError(Throwable())
            null
        }.`when`(mRemoveFavoriteUseCase).execute(any<ListPresenter.FavoriteSubscriber>(), any())

        // When
        mPresenter.setFavorite(toilet)

        // Verify
        assert(toilet.isFavorite)
        verify(mView).notifyItemModified(any())
        verify(mView).notifyError()
        verifyNoMoreInteractions(mView)
    }
}