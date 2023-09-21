package Main.Core

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.async.KtxAsync
import ktx.box2d.body
import ktx.box2d.box
import ktx.box2d.createWorld
import ktx.box2d.earthGravity
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.circle
import ktx.graphics.use


class Main : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()
        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }
}

class FirstScreen : KtxScreen {
    private val image = Texture("logo.png".toInternalFile(), true).apply { setFilter(Linear, Linear) }
    private val porn = Texture("box.png".toInternalFile(), true).apply { setFilter(Linear, Linear) }
    private val batch = SpriteBatch()
    private lateinit var renderer: ShapeRenderer
    private lateinit var world: World
    private lateinit var squareBody: Body
    private lateinit var ballBody: Body

    init {
        renderer = ShapeRenderer()
        world = createWorld(Vector2(0f, -9.81f))

        // Create a static square body at the center of the screen
        squareBody = world.body {
            position.set(Vector2(Gdx.graphics.width / 2f, Gdx.graphics.height / 2f))
            box(width = 50f, height = 50f) {
                density = 0f
            }
        }
        ballBody = world.body {
            type = BodyDef.BodyType.DynamicBody
            position.set(Vector2(Gdx.graphics.width / 2f, Gdx.graphics.height - 50f))
            circle(radius = 25f) {
                density = 1f
            }
        }
    }



    override fun show() {


    }


    override fun render(delta: Float) {
        clearScreen(red = 0.0f, green = 0.0f, blue = 0.0f)

        val deltaTime = Gdx.graphics.deltaTime

        /*batch.use {
            //it.draw(image, 200f, 200f)
            //it.draw(porn, 350f, 350f)

        }
        */
        // Update physics simulation
        world.step(1 / 60f, 6, 2)

        // Draw the square
        renderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color.set(1f, 0f, 0f, 1f)
            it.rect(
                squareBody.position.x - 25f,
                squareBody.position.y - 25f,
                50f,
                50f
            )
            it.color.set(0f, 0f, 1f, 1f)
            it.circle(ballBody.position.x, ballBody.position.y, 25f)
        }

    }



    override fun dispose() {
        image.disposeSafely()
        batch.disposeSafely()
        renderer.disposeSafely()

    }

}
