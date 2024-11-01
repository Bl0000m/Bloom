package kz.bloom.ui.ui_components

import kotlin.coroutines.CoroutineContext

import kotlinx.coroutines.cancel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.CoroutineScope

import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.LifecycleOwner

public fun LifecycleOwner.coroutineScope(
    context: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate
): CoroutineScope {
    val scope = CoroutineScope(context = context)

    lifecycle.doOnDestroy {
        scope.cancel()
    }

    return scope
}