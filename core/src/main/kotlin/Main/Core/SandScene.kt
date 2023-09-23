package Main.Core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.actors.onClick
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.dispose
import ktx.assets.disposeSafely
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.scene2d
import ktx.scene2d.textButton
import Main.Core.Menu
import com.badlogic.gdx.scenes.scene2d.ui.Label
import ktx.scene2d.label

class SandScene (private val game: KtxGame<KtxScreen>) : KtxScreen {
    private val batch = SpriteBatch()
    private val stage = Stage(ExtendViewport(350f, 180f))
    private val skin = Skin(Gdx.files.internal("uiskin.json"))
    var transitionButton: TextButton
    lateinit var SceneLabel: Label


    init {
        Scene2DSkin.defaultSkin = skin

        transitionButton = scene2d.textButton("Go to MENU", skin = skin) {
            setPosition(20f, 20f) // Adjust position as needed
            onClick {
                game.setScreen<Menu>()
            }


        }
        SceneLabel = scene2d.label("SAND", skin = skin) {
            setPosition(10f, 160f)
        }
        stage.addActor(SceneLabel)
        stage.addActor(transitionButton)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }


    override fun hide() {

    }

    override fun pause() {

    }

    override fun render(delta: Float) {
        clearScreen(red = 0.0f, green = 0.0f, blue = 0.0f)
        batch.begin()
        stage.act(delta)
        stage.draw()
        batch.end()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun resume() {

    }


    override fun dispose() {
        batch.disposeSafely()
        stage.disposeSafely()
    }

}
