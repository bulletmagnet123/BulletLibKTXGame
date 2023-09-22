package Main.Core
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import Main.Core.MainScene
import Main.Core.Menu




class Main : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()
        addScreen(MainScene())
        addScreen(Menu())
        setScreen<Menu>()
    }
}

