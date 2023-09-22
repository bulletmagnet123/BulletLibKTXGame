package Main.Core
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.graphics.color
import ktx.scene2d.*
import ktx.style.label
import ktx.style.skin


class Menu : KtxScreen {
    private val batch = SpriteBatch()
    private val stage = Stage(ExtendViewport(350f, 180f))
    private val skin = Skin().apply {
        val font = BitmapFont()
        add("default", font)

        val labelStyle = Label.LabelStyle(font, Color.BLACK)
        add("default", labelStyle, Label.LabelStyle::class.java)
    }


    init {
        skin.add("default", BitmapFont())
        Scene2DSkin.defaultSkin = skin
    }

    val myFirstActor = scene2d.label(text = "Hellow World")


    override fun show() {
        stage.clear()
        stage.addActor(myFirstActor)
    }


    override fun render(delta: Float) {
        clearScreen(red = 0.5f, green = 0.0f, blue = 0.5f)
        batch.begin()
        stage.draw()

        batch.end()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        skin.getAll(BitmapFont::class.java).forEach { it.value.dispose() } // Dispose all fonts in the skin
        skin.dispose()
        batch.dispose()
        stage.dispose()

    }
}
