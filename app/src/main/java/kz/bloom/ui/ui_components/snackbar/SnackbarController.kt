package kz.bloom.ui.ui_components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.RememberObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

public class SnackbarController(
    public val scope: CoroutineScope,
    private val snackbarHostState: SnackbarHostState
) : RememberObserver {
    private var snackbarJob: Job? = null

    init { cancelActiveJob() }

    override fun onRemembered() {
    }

    override fun onAbandoned() {
    }

    override fun onForgotten() {
        snackbarJob?.cancel()
        snackbarJob = null
    }

    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        snackbarJob?.cancel()

        snackbarJob = scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration
            )
            snackbarJob = null
        }
    }

    private fun cancelActiveJob() {
        snackbarJob?.let { job ->
            job.cancel()
            snackbarJob = Job()
        }
    }
}