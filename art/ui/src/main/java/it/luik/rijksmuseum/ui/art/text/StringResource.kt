package it.luik.rijksmuseum.ui.art.text

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class StringResource {

    abstract fun get(context: Context): String?

    data class Id(@StringRes val resId: Int) : StringResource() {
        override fun get(context: Context): String = context.getString(resId)
    }

    @Suppress("SpreadOperator")
    data class IdArg(@StringRes val resId: Int, val args: List<Any>) : StringResource() {
        override fun get(context: Context): String = context.getString(resId, *args.toTypedArray())
    }

    @Suppress("SpreadOperator")
    data class Quantity(@PluralsRes val resId: Int, val quantity: Int, val args: List<Any>) :
        StringResource() {
        override fun get(context: Context): String =
            context.resources.getQuantityString(resId, quantity, *args.toTypedArray())
    }

    data class Value(val value: String?) : StringResource() {
        override fun get(context: Context): String? = value
    }

    data class IdArgAction(
        @StringRes val resId: Int,
        val arg: Any,
        val action: (input: String) -> CharSequence
    ) : StringResource() {
        override fun get(context: Context): String {
            return action(context.getString(resId, arg)).toString()
        }
    }

    object None : StringResource() {
        override fun get(context: Context): String? = null
    }
}
