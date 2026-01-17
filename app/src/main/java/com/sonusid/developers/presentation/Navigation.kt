package com.sonusid.developers.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
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
object CheckInRoute

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

private val EmphasizedEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
private const val AnimationDuration = 600

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
                animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)
            ) + fadeIn(animationSpec = tween(AnimationDuration))
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                targetOffset = { -it / 3 },
                animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)
            ) + scaleOut(targetScale = 0.92f, animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)) +
                    fadeOut(animationSpec = tween(AnimationDuration / 2))
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                initialOffset = { -it / 3 },
                animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)
            ) + scaleIn(initialScale = 0.92f, animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)) +
                    fadeIn(animationSpec = tween(AnimationDuration))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)
            ) + fadeOut(animationSpec = tween(AnimationDuration))
        }
    ) {
        composable<HomeRoute> {
            HomeScreen(
                viewModel = eventViewModel,
                onProfileClick = { navController.navigate(ProfileRoute) },
                onEventsClick = { navController.navigate(EventsManagementRoute) },
                onCheckInClick = { navController.navigate(CheckInRoute) },
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

        composable<CheckInRoute> {
            CheckInScreen(
                viewModel = eventViewModel,
                onBackClick = { navController.popBackStack() }
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
