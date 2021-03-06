package playground

import react.*
import react.dom.*
import uspek.*
import kotlinx.coroutines.*
import painting.clearCanvas
import painting.paintSomething

interface PlaygroundProps : RProps { var speed: Int }
interface PlaygroundState : RState { var tree: USpekTree }

class Playground(props: PlaygroundProps) : RComponent<PlaygroundProps, PlaygroundState>(props) {

    override fun PlaygroundState.init(props: PlaygroundProps) {
        tree = uspekContext.root
        uspekLog = {
            println(it.status)
            setState { tree = uspekContext.root }
            paintSomething()
            delay(40)
            if (it.finished) clearCanvas()
        }
    }

    override fun componentDidMount() {
        GlobalScope.launch {
            example()
            paintSomething()
        }
    }

    override fun RBuilder.render() {
        div(classes = "playground") {
            div(classes = "tests-side") { rtree(state.tree) }
            div(classes = "canvas-side") {
                div(classes = "canvas") {
                    canvas {  }
                }
            }
        }
    }
}

fun RBuilder.playground(speed: Int = 400) = child(Playground::class) { attrs.speed = speed }
