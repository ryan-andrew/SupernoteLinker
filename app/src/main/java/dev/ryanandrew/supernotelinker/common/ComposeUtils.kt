package dev.ryanandrew.supernotelinker.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            onEvent(event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun OnResume(block: () -> Unit) {
    ComposableLifecycle {
        if (it == Lifecycle.Event.ON_RESUME) {
            block()
        }
    }
}

fun log(message: String) {
    Log.e("!!!!!", message)
}

tailrec fun Context.getActivityOrNull(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivityOrNull()
    else -> null
}

fun Context.getActivity(): Activity = getActivityOrNull()
    ?: throw IllegalStateException("Not in the context of an Activity")
