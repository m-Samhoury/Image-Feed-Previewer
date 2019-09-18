package com.moustafa.imagefeedpreviewer.di

import com.moustafa.imagefeedpreviewer.BuildConfig
import com.moustafa.imagefeedpreviewer.repository.NetworkFactory
import com.moustafa.imagefeedpreviewer.repository.unsplash.UnsplashService
import org.koin.android.ext.koin.androidContext
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
}
