package com.moustafa.imagefeedpreviewer.di

import com.moustafa.imagefeedpreviewer.BuildConfig
import com.moustafa.imagefeedpreviewer.models.PhotoInfoUnsplashResponse
import com.moustafa.imagefeedpreviewer.repository.ImageFeedAdapter
import com.moustafa.imagefeedpreviewer.repository.NetworkFactory
import com.moustafa.imagefeedpreviewer.repository.unsplash.UnsplashAdapter
import com.moustafa.imagefeedpreviewer.repository.unsplash.UnsplashService
import com.moustafa.imagefeedpreviewer.ui.imageslist.ImageFeedsListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */
val repositoryModule: Module = module {

    single { NetworkFactory.makeHttpClient(androidContext()) }

    single<UnsplashService> { NetworkFactory.makeServiceFactory(get()) }

    single { NetworkFactory.makeRetrofit(BuildConfig.BASE_API_URL, get()) }

    single<ImageFeedAdapter<PhotoInfoUnsplashResponse>> { UnsplashAdapter(get()) }
}
val viewModelsModule = module {
    viewModel {
        ImageFeedsListViewModel(imageFeedAdapter = get())
    }
}