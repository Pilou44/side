package com.wechantloup.side.modules.home

import com.nhaarman.mockitokotlin2.any
import com.wechantloup.side.domain.usecase.GetFavoritesUseCase
import com.wechantloup.side.domain.usecase.GetToiletsUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class HomePresenterUTests {

    @get:Rule
    val unitOfWorkRule = MockitoJUnit.rule()!!

    @Mock
    private lateinit var mView: HomeContract.View

    @Mock
    private lateinit var mRouter: HomeContract.Router

    @Mock
    private lateinit var mGetToiletsUseCase: GetToiletsUseCase

    @Mock
    private lateinit var mGetFavoritesUseCase: GetFavoritesUseCase

    private lateinit var mPresenter: HomePresenter

    @Before
    @Throws(Exception::class)
    fun setUp() {

        mPresenter = HomePresenter(
            mRouter,
            mGetToiletsUseCase,
            mGetFavoritesUseCase
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
        Mockito.doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as HomePresenter.GetFavoritesSubscriber
            subscriber.onNext(emptyList())
            null
        }.`when`(mGetFavoritesUseCase).execute(any<HomePresenter.GetFavoritesSubscriber>())

        Mockito.doAnswer { invocation ->
            val subscriber = invocation.arguments[0] as HomePresenter.GetToiletsSubscriber
            subscriber.onNext(ArrayList())
            null
        }.`when`(mGetToiletsUseCase).execute(any<HomePresenter.GetToiletsSubscriber>())

        // When
        mPresenter.retrieveToiletsList()

        // Verify
        Mockito.verify(mView).notifyToiletsListRetrieved()
    }
}