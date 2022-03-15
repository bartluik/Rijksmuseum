package it.luik.rijksmuseum.ui.art.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.whenCreated
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun Fragment.inStartedLifecycle(vararg collectors: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        whenCreated {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectors.forEach { collector ->
                    launch { collector() }
                }
            }
        }
    }
}
