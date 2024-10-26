package initialization

import localDatabase.daos.DataDao


object ApplicationComponent {
    private var _coreComponent: CoreComponent? = null
    val coreComponent
        get() = _coreComponent
            ?: throw IllegalStateException("Make sure to call ApplicationComponent.init()")

    fun init(dao: DataDao) {
        _coreComponent = CoreComponentImpl(dao)
    }
}

val coreComponent get() = ApplicationComponent.coreComponent