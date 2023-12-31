package Main.Core
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.actors.onClick
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.scene2d
import ktx.scene2d.textButton



class Menu(private val game: KtxGame<KtxScreen>) : KtxScreen {
    private val batch = SpriteBatch()
    private val stage = Stage(ExtendViewport(350f, 180f))
    private val skin = Skin(Gdx.files.internal("uiskin.json"))
    var GoToMainBallButton: TextButton
    var GoToSandSimButton: TextButton

    init {
        Scene2DSkin.defaultSkin = skin
        GoToMainBallButton = scene2d.textButton("Go to MainBall Scene", skin = skin) {
            setPosition(130f, 90f) // Adjust position as needed
            onClick {
                game.setScreen<MainScene>()
            }
        }
        GoToSandSimButton = scene2d.textButton("Go to Sand Sim", skin = skin) {
            setPosition(130f, 60f) // Adjust position as needed
            onClick {
                game.setScreen<SandScene>()
            }
        }
        stage.addActor(GoToSandSimButton)
        stage.addActor(GoToMainBallButton)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        clearScreen(red = 0.5f, green = 0.0f, blue = 0.5f)
        batch.begin()
        stage.act(delta)
        stage.draw()
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun dispose() {
        skin.getAll(BitmapFont::class.java).forEach { it.value.dispose() } // Dispose all fonts in the skin
        skin.dispose()
        batch.dispose()
        stage.dispose()
    }
}
