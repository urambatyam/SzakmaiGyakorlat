package initialization

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

interface CoroutinesComponent {
    val mainImmediateDispatcher: CoroutineDispatcher
    val applicationScope: CoroutineScope
}

