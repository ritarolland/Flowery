package com.example.prac1.presentation.composable

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.prac1.R

/**
 * Composable function that loads and displays an image using Glide library
 * with network connectivity check and fallback placeholder
 *
 * @param modifier Modifier for styling and layout
 * @param imageUrl URL of the image to load (nullable)
 * @param description Content description for accessibility
 * @param alpha Opacity value for the image [0f-1f]
 * @author Sofia Bakalskaya
 */
@Composable
fun GlideImage(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    description: String,
    alpha: Float = 1f
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ImageView(context).apply {
            contentDescription = description
        } },
        update = { imageView ->
            val isConnected = context.isNetworkAvailable()
            Glide.with(context)
                .load(if (isConnected && imageUrl != null) imageUrl else R.drawable.fairy)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(ColorDrawable(Color.GRAY))
                .error(R.drawable.fairy)
                .into(imageView)
            imageView.alpha = alpha
        }
    )
}

/**
 * Extension function to check network availability
 *
 * @return Boolean indicating if network is available and connected
 * @author Sofia Bakalskaya
 */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}