package it.luik.rijksmuseum.ui.art.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * To avoid view binding boilerplate
 * https://medium.com/default-to-open/handling-lifecycle-with-view-binding-in-fragments-a7f237c56832
 */
fun <T> Fragment.viewBindingLifecycle(): ReadWriteProperty<Fragment, T> =
    object : ReadWriteProperty<Fragment, T>, DefaultLifecycleObserver {

        // A backing property to hold our value
        private var binding: T? = null

        private var viewLifecycleOwner: LifecycleOwner? = null

        init {
            // Observe the View Lifecycle of the Fragment
            this@viewBindingLifecycle
                .viewLifecycleOwnerLiveData
                .observe(this@viewBindingLifecycle) { newLifecycleOwner ->
                    viewLifecycleOwner
                        ?.lifecycle
                        ?.removeObserver(this)

                    viewLifecycleOwner = newLifecycleOwner.also {
                        it.lifecycle.addObserver(this)
                    }
                }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            binding = null
        }

        override fun getValue(
            thisRef: Fragment,
            property: KProperty<*>
        ): T {
            // Return the backing property if it's set
            return this.binding!!
        }

        override fun setValue(
            thisRef: Fragment,
            property: KProperty<*>,
            value: T
        ) {
            // Set the backing property
            this.binding = value
        }
    }
