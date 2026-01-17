package com.sonusid.developers.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sonusid.developers.modals.Event
import com.sonusid.developers.viewmodels.EventViewModel
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
object ProfileRoute

@Serializable
object EventsManagementRoute

@Serializable
data class ViewEventRoute(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val category: String,
    val isLive: Boolean,
    val attendees: Int = 0,
)

@Composable
fun UniVerseNavHost(
    navController: NavHostController,
    eventViewModel: EventViewModel
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            ) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            // Parallax effect: the exiting screen moves only 30% of the way
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                targetOffset = { it / 3 },
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            ) + scaleOut(targetScale = 0.9f, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
        },
        popEnterTransition = {
            // Parallax effect: the entering screen starts 30% offset
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                initialOffset = { it / 3 },
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            ) + scaleIn(initialScale = 0.9f, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            ) + fadeOut(animationSpec = tween(500))
        }
    ) {
        composable<HomeRoute> {
            HomeScreen(
                viewModel = eventViewModel,
                onProfileClick = { navController.navigate(ProfileRoute) },
                onEventsClick = { navController.navigate(EventsManagementRoute) },
                onEventClick = { event ->
                    navController.navigate(
                        ViewEventRoute(
                            id = event.id,
                            title = event.title,
                            date = event.date,
                            time = event.time,
                            location = event.location,
                            category = event.category,
                            isLive = event.isLive,
                            attendees = event.attendees
                        )
                    )
                }
            )
        }

        composable<ProfileRoute> {
            ProfileScreen(onBackClick = { navController.popBackStack() })
        }

        composable<EventsManagementRoute> {
            EventsManagementScreen(
                viewModel = eventViewModel,
                onBackClick = { navController.popBackStack() },
                onEventClick = { event ->
                    navController.navigate(
                        ViewEventRoute(
                            id = event.id,
                            title = event.title,
                            date = event.date,
                            time = event.time,
                            location = event.location,
                            category = event.category,
                            isLive = event.isLive,
                            attendees = event.attendees
                        )
                    )
                }
            )
        }

        composable<ViewEventRoute> { backStackEntry ->
            val route: ViewEventRoute = backStackEntry.toRoute()
            val event = Event(
                id = route.id,
                title = route.title,
                date = route.date,
                time = route.time,
                location = route.location,
                category = route.category,
                isLive = route.isLive,
                attendees = route.attendees
            )
            ViewEvent(
                event = event,
                viewModel = eventViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
