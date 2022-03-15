package it.luik.rijksmuseum.ui.art.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment

fun Fragment.navigate(directions: NavDirections, navOptions: NavOptions? = null) =
    NavHostFragment.findNavController(this).safeNavigate(directions, navOptions)

fun NavController.safeNavigate(direction: NavDirections, navOptions: NavOptions?) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction, navOptions) }
}
