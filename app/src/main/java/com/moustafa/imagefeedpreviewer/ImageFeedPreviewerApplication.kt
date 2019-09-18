package com.moustafa.imagefeedpreviewer

import android.app.Application
import com.moustafa.imagefeedpreviewer.di.repositoryModule
import com.moustafa.imagefeedpreviewer.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author moustafasamhoury
 * created on Wednesday, 18 Sep, 2019
 */

class ImageFeedPreviewerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // declare used Android context
            androidContext(this@ImageFeedPreviewerApplication)

            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidFileProperties()


            modules(listOf(repositoryModule, viewModelsModule))
        }
    }
}