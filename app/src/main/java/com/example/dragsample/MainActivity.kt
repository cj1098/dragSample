package com.example.dragsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.asFlow
import com.example.dragsample.ui.BottomNavMenu
import com.example.dragsample.ui.theme.DragSampleTheme

val bottom_nav_icon_size = 56.dp

class MainActivity : ComponentActivity() {

    val viewModel = BaseViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DragSampleTheme {
                val uiState by viewModel.uiStateLiveData.asFlow().collectAsState(initial = viewModel.uiState)
                // A surface container using the 'background' color from the theme
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                    BottomNavMenu(uiState, viewModel)
                }
            }
        }
    }
}

@Composable
fun BottomNavPortrait(uiState: BottomNavUiState, uiActions: BottomNavUiActions) {
    var progress by rememberSaveable { mutableStateOf(1f) }
    var fingerUp by rememberSaveable { mutableStateOf(true) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }
    var extraLayoutHeight by rememberSaveable { mutableStateOf(0f) }

    DisposableEffect(Unit) {
        offsetY = if (uiState.progress <= .5f) {
            0f
        } else {
            extraLayoutHeight
        }
        onDispose {  }
    }

    val snapTransition = updateTransition(targetState = fingerUp, label = "")
    val snapOffset by snapTransition.animateIntOffset(
        label = "",
        transitionSpec = { tween(if (fingerUp) 200 else 0) }) {
        if (it) {
            if (uiState.progress <= .5f) {
                IntOffset(0, 0)
            } else {
                IntOffset(0, extraLayoutHeight.toInt())
            }
        } else {
            IntOffset(0, offsetY.toInt())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .offset { snapOffset }
            .pointerInput(Unit) {
                detectVerticalDragGestures(onDragStart = {
                    fingerUp = false
                }, onDragEnd = {
                    fingerUp = true
                    offsetY = if (progress <= .5f) {
                        0f
                    } else {
                        extraLayoutHeight
                    }
                    uiActions.onBottomMenuDragEnd(progress = progress)
                }) { change, dragAmount ->
                    offsetY = (offsetY + dragAmount).coerceIn(0f, extraLayoutHeight)
                    progress = offsetY / extraLayoutHeight
                    if (progress >= .5f) {
                        uiActions.onMenuCollapsed()
                    } else {
                        uiActions.onMenuExpanded()
                    }
                    change.consumePositionChange()
                }
            },
    ) {

        val extraLayoutModifier = if (extraLayoutHeight == 0f) {
            Modifier.layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                extraLayoutHeight = placeable.height.toFloat()
                offsetY = if (uiState.bottomNavMenuExpanded) {
                    0f
                } else {
                    placeable.height.toFloat()
                }
                layout(constraints.maxWidth, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            }
        } else {
            Modifier
        }

        MainLayout()
        ExtraLayout(extraLayoutModifier)
    }
}

@Composable
private fun MainLayout() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Black.copy(alpha = 0.38f))
            .padding(bottom = 24.dp)
    ) {
        val (arrow, firstIcon, firstIconText, secondIcon, secondIconText, thirdIcon, thirdIconText, fourthIcon, fourthIconText) = createRefs()

        Box(modifier = Modifier
            .constrainAs(arrow) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .size(width = 40.dp, height = 24.dp), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(
                    id = R.drawable.up_arrow
                ),
                contentDescription = ""
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .size(bottom_nav_icon_size)
                .constrainAs(firstIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(arrow.bottom)
                    end.linkTo(secondIcon.start)
                },
            onClick = { },
            backgroundColor = Color.Black,
            contentColor = Color.White,
        ) {
            Icon(painter = painterResource(R.drawable.add_person), contentDescription = "")
        }
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(firstIconText) {
                    top.linkTo(firstIcon.bottom)
                    start.linkTo(firstIcon.start)
                    end.linkTo(firstIcon.end)
                },
            text = "add person",
            color = Color.White,
        )

        FloatingActionButton(
            modifier = Modifier
                .size(bottom_nav_icon_size)
                .constrainAs(secondIcon) {
                    start.linkTo(firstIcon.end)
                    top.linkTo(arrow.bottom)
                    end.linkTo(thirdIcon.start)
                },
            onClick = {},
            backgroundColor = Color.DarkGray,
            contentColor = Color.White,
        ) {
            Icon(painter = painterResource(R.drawable.add_person), contentDescription = "")
        }
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(secondIconText) {
                    top.linkTo(secondIcon.bottom)
                    start.linkTo(secondIcon.start)
                    end.linkTo(secondIcon.end)
                },
            text = "add person",
            color = Color.White,
        )

        FloatingActionButton(
            modifier = Modifier
                .size(bottom_nav_icon_size)
                .constrainAs(thirdIcon) {
                    start.linkTo(secondIcon.end)
                    top.linkTo(arrow.bottom)
                    end.linkTo(fourthIcon.start)
                },
            onClick = { },
            backgroundColor = Color.DarkGray,
            contentColor = Color.White,
        ) {
            Icon(painter = painterResource(R.drawable.add_person), contentDescription = "")
        }
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(thirdIconText) {
                    top.linkTo(thirdIcon.bottom)
                    start.linkTo(thirdIcon.start)
                    end.linkTo(thirdIcon.end)
                },
            text = "add person",
            color = Color.White,
        )

        FloatingActionButton(
            modifier = Modifier
                .size(bottom_nav_icon_size)
                .constrainAs(fourthIcon) {
                    start.linkTo(thirdIcon.end)
                    top.linkTo(arrow.bottom)
                    end.linkTo(parent.end)
                },
            onClick = { },
            backgroundColor = Color.Red,
            contentColor = Color.White,
        ) {
            Icon(painter = painterResource(R.drawable.add_person), contentDescription = "")
        }
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(fourthIconText) {
                    top.linkTo(fourthIcon.bottom)
                    start.linkTo(fourthIcon.start)
                    end.linkTo(fourthIcon.end)
                },
            text = "add person",
            color = Color.White,
        )
    }
}

@Composable
private fun ExtraLayout(modifier: Modifier = Modifier) {
    ConstraintLayout(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .background(color = Color.Black.copy(alpha = 0.38f))
        )
    ) {
        val (firstIcon, firstIconText, secondIcon, secondIconText, thirdIcon, thirdIconText, fourthIcon, fourthIconText) = createRefs()

        FloatingActionButton(
            modifier = Modifier
                .size(bottom_nav_icon_size)
                .constrainAs(firstIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(secondIcon.start)
                },
            onClick = { },
            backgroundColor = Color.DarkGray,
            contentColor = Color.White,
        ) {
            Icon(painter = painterResource(R.drawable.add_person), contentDescription = "")
        }
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .constrainAs(firstIconText) {
                    top.linkTo(firstIcon.bottom)
                    start.linkTo(firstIcon.start)
                    end.linkTo(firstIcon.end)
                },
            text = "add person",
            color = Color.White,
        )

        FloatingActionButton(
            modifier = Modifier
                .size(bottom_nav_icon_size)
                .constrainAs(secondIcon) {
                    start.linkTo(firstIcon.end)
                    top.linkTo(parent.top)
                    end.linkTo(thirdIcon.start)
                },
            onClick = { },
            backgroundColor = Color.DarkGray,
            contentColor = Color.White,
        ) {
            Icon(painter = painterResource(R.drawable.add_person), contentDescription = "")
        }
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .constrainAs(secondIconText) {
                    top.linkTo(secondIcon.bottom)
                    start.linkTo(secondIcon.start)
                    end.linkTo(secondIcon.end)
                },
            text = "add person",
            color = Color.White,
        )

        FloatingActionButton(
            modifier = Modifier
                .size(bottom_nav_icon_size)
                .constrainAs(thirdIcon) {
                    start.linkTo(secondIcon.end)
                    top.linkTo(parent.top)
                    end.linkTo(fourthIcon.start)
                },
            onClick = {},
            backgroundColor = Color.DarkGray,
            contentColor = Color.White,
        ) {
            Icon(painter = painterResource(R.drawable.add_person), contentDescription = "")
        }
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .constrainAs(thirdIconText) {
                    top.linkTo(thirdIcon.bottom)
                    start.linkTo(thirdIcon.start)
                    end.linkTo(thirdIcon.end)
                },
            text = "add person",
            color = Color.White,
        )

        FloatingActionButton(
            modifier = Modifier
                .size(bottom_nav_icon_size)
                .constrainAs(fourthIcon) {
                    start.linkTo(thirdIcon.end)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
            onClick = {},
            backgroundColor = Color.DarkGray,
            contentColor = Color.White,
        ) {
            Icon(painter = painterResource(R.drawable.add_person), contentDescription = "")
        }
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .constrainAs(fourthIconText) {
                    top.linkTo(fourthIcon.bottom)
                    start.linkTo(fourthIcon.start)
                    end.linkTo(fourthIcon.end)
                },
            text = "add person",
            color = Color.White,
        )
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DragSampleTheme {
        Greeting("Android")
    }
}