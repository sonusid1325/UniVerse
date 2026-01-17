package com.sonusid.developers.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
object CommunitiesRoute

@Serializable
object TrendingRoute

@Serializable
data class PostEventRoute(val communityId: String)

@Serializable
data class CommunityDetailRoute(
    val id: String,
    val name: String,
    val description: String,
    val memberCount: Int,
    val isAdmin: Boolean
)

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
private const val AnimationDuration = 500

@Composable
fun UniVerseNavHost(
    navController: NavHostController,
    eventViewModel: EventViewModel
) {
    // Wrapping in Surface with theme background to prevent white flashes during transitions
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)
                ) + fadeIn(animationSpec = tween(AnimationDuration)) +
                        scaleIn(initialScale = 0.95f, animationSpec = tween(AnimationDuration, easing = EmphasizedEasing))
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    targetOffset = { -it / 4 },
                    animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)
                ) + scaleOut(targetScale = 0.95f, animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)) +
                        fadeOut(animationSpec = tween(AnimationDuration / 2))
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    initialOffset = { -it / 4 },
                    animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)
                ) + scaleIn(initialScale = 0.95f, animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)) +
                        fadeIn(animationSpec = tween(AnimationDuration))
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(AnimationDuration, easing = EmphasizedEasing)
                ) + fadeOut(animationSpec = tween(AnimationDuration)) +
                        scaleOut(targetScale = 0.95f, animationSpec = tween(AnimationDuration, easing = EmphasizedEasing))
            }
        ) {
            composable<HomeRoute> {
                HomeScreen(
                    viewModel = eventViewModel,
                    onProfileClick = { navController.navigate(ProfileRoute) },
                    onEventsClick = { navController.navigate(EventsManagementRoute) },
                    onCheckInClick = { navController.navigate(CheckInRoute) },
                    onCommunityClick = { navController.navigate(CommunitiesRoute) },
                    onTrendingClick = { navController.navigate(TrendingRoute) },
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

            composable<CommunitiesRoute> {
                CommunityScreen(
                    viewModel = eventViewModel,
                    onBackClick = { navController.popBackStack() },
                    onCommunityClick = { community ->
                        navController.navigate(
                            CommunityDetailRoute(
                                id = community.id,
                                name = community.name,
                                description = community.description,
                                memberCount = community.memberCount,
                                isAdmin = community.isAdmin
                            )
                        )
                    }
                )
            }

            composable<TrendingRoute> {
                TrendingScreen(
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

            composable<CommunityDetailRoute> { backStackEntry ->
                val route: CommunityDetailRoute = backStackEntry.toRoute()
                CommunityDetailScreen(
                    viewModel = eventViewModel,
                    communityId = route.id,
                    communityName = route.name,
                    communityDescription = route.description,
                    memberCount = route.memberCount,
                    isAdmin = route.isAdmin,
                    onBackClick = { navController.popBackStack() },
                    onPostEventClick = {
                        navController.navigate(PostEventRoute(communityId = route.id))
                    },
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

            composable<PostEventRoute> { backStackEntry ->
                val route: PostEventRoute = backStackEntry.toRoute()
                PostEventScreen(
                    viewModel = eventViewModel,
                    communityId = route.communityId,
                    onBackClick = { navController.popBackStack() },
                    onEventPosted = { navController.popBackStack() }
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
}
