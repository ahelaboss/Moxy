package moxy.ktx

import moxy.MvpDelegate
import moxy.MvpPresenter
import moxy.presenter.PresenterField
import kotlin.reflect.KProperty

class MoxyKtxDelegate<T : MvpPresenter<*>>(
    delegate: MvpDelegate<*>,
    name: String,
    private val factory: () -> T
) {

    private var presenter: T? = null

    init {
        val field = object : PresenterField<Any?>(name, null, null) {
            override fun providePresenter(delegated: Any?): MvpPresenter<*> = factory()
            override fun bind(container: Any?, presenter: MvpPresenter<*>) {
                this@MoxyKtxDelegate.presenter = presenter as T
            }
        }
        delegate.registerExternalPresenterField(field)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return presenter ?: throw IllegalStateException(
            "Presenter can be accessed only after MvpDelegate.onCreate() call"
        )
    }
}
