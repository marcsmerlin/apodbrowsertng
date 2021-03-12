package com.marcsmerlin.randomapod

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import com.marcsmerlin.randomapod.ui.theme.ApodBrowserTheme
import com.marcsmerlin.randomapod.utils.BitmapLoader


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ApodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer: AppContainer = (application as ApodApplication).container

        viewModel = ViewModelProvider(
            this,
            appContainer.viewModelFactory
        ).get(ApodViewModelImpl::class.java)

        setContent {
            ApodBrowserTheme {
                ProvideBitmapLoader(myBitmapLoader = appContainer.bitmapLoader) {
                    TopScreen(
                        appName = stringResource(id = R.string.app_name),
                        viewModel = viewModel,
                    )

                }
            }
        }
    }
}

val LocalBitmapLoader = staticCompositionLocalOf<BitmapLoader> { error("No BitmapLoader supplied") }

@Composable
fun ProvideBitmapLoader(
    myBitmapLoader: BitmapLoader,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val bitmapLoader = remember(context) { myBitmapLoader }

    CompositionLocalProvider(LocalBitmapLoader provides bitmapLoader, content = content)

}