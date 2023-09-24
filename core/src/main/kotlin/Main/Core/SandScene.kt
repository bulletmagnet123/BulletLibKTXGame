package Main.Core

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.actors.onClick
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.label
import ktx.scene2d.scene2d
import ktx.scene2d.textButton

class SandScene(private val game: KtxGame<KtxScreen>) : KtxScreen, InputProcessor {

    private val batch = SpriteBatch()
    private val stage = Stage(ExtendViewport(350f, 250f))
    private val skin = Skin(Gdx.files.internal("uiskin.json"))
    private var transitionButton: TextButton
    private var sceneLabel: Label
    private val sandTexture: Texture = Texture(Gdx.files.internal("sand.png"))
    private val darkSandTexture: Texture = Texture(Gdx.files.internal("dark.png"))
    private val redSandTexture: Texture = Texture(Gdx.files.internal("red.png"))
    private var sandWidth: Int = 0
    private var sandHeight: Int = 0
    private lateinit var sandArray: Array<IntArray>
    private val sandSize = 24
    private lateinit var camera: OrthographicCamera
    private var isTouching = false

    init {
        Scene2DSkin.defaultSkin = skin
        stage.isDebugAll = true

        transitionButton = scene2d.textButton("Go to MENU", skin = skin) {
            setPosition(20f, 20f)
            onClick {
                game.setScreen<Menu>()
            }
        }

        sceneLabel = scene2d.label("SAND", skin = skin) {
            setPosition(10f, 160f)
        }

        stage.addActor(sceneLabel)
        stage.addActor(transitionButton)
        Gdx.app.logLevel = Application.LOG_DEBUG
    }

    override fun show() {
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(stage)
        inputMultiplexer.addProcessor(this)
        Gdx.input.inputProcessor = inputMultiplexer

        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f)
        camera.update()

        sandWidth = Gdx.graphics.width / sandSize
        sandHeight = Gdx.graphics.height / sandSize
        sandArray = Array(sandWidth) { IntArray(sandHeight) }
    }

    private fun addSandToArray(x: Int, y: Int, sandType: Int) {
        if (x in 0 until sandWidth && y in 0 until sandHeight) {
            sandArray[x][y] = sandType
            Gdx.app.log("DEBUG", "Added sand of type $sandType at ($x, $y)")
        }
    }

    private fun updateSand() {
        for (y in 1 until sandHeight) {
            for (x in 0 until sandWidth) {
                val sandType = sandArray[x][y]
                if (sandType > 0) {
                    when {
                        sandArray[x][y - 1] == 0 -> {
                            sandArray[x][y] = 0
                            sandArray[x][y - 1] = sandType
                        }
                        x > 0 && sandArray[x - 1][y - 1] == 0 -> {
                            sandArray[x][y] = 0
                            sandArray[x - 1][y - 1] = sandType
                        }
                        x < sandWidth - 1 && sandArray[x + 1][y - 1] == 0 -> {
                            sandArray[x][y] = 0
                            sandArray[x + 1][y - 1] = sandType
                        }
                    }
                }
            }
        }
    }

    private fun getTextureForSandType(sandType: Int): Texture = when (sandType) {
        1 -> sandTexture
        2 -> darkSandTexture
        3 -> redSandTexture
        else -> sandTexture
    }

    override fun render(delta: Float) {
        clearScreen(0.0f, 0.0f, 0.0f)
        updateSand()

        batch.projectionMatrix = camera.combined
        batch.begin()
        for (x in 0 until sandWidth) {
            for (y in 0 until sandHeight) {
                val sandType = sandArray[x][y]
                if (sandType > 0) {
                    val texture = getTextureForSandType(sandType)
                    batch.draw(texture, x.toFloat() * sandSize, y.toFloat() * sandSize)
                }
            }
        }
        batch.end()

        stage.act(delta)
        stage.draw()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        Gdx.app.log("DEBUG", "Touch detected at ($screenX, $screenY)")
        val worldPos = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
        val sandX = (worldPos.x / sandSize).toInt()
        val sandY = (worldPos.y / sandSize).toInt()

        isTouching = true
        spawnSand(screenX, screenY)
        return true
    }

    override fun dispose() {
        batch.disposeSafely()
        stage.disposeSafely()
        sandTexture.disposeSafely()
        darkSandTexture.disposeSafely()
        redSandTexture.disposeSafely()

    }
    private fun spawnSand(screenX: Int, screenY: Int) {
        val worldPos = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
        val sandX = (worldPos.x / sandSize).toInt()
        val sandY = (worldPos.y / sandSize).toInt()

        if (sandX in 0 until sandWidth && sandY in 0 until sandHeight) {
            addSandToArray(sandX, sandY, 1)
        }
    }

    // Other default methods of InputProcessor
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        isTouching = false
        return false
    }
    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (isTouching) {
            spawnSand(screenX, screenY)
            return true
        }
        return false
    }
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun keyDown(keycode: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun scrolled(amountX: Float, amountY: Float): Boolean = false
}
