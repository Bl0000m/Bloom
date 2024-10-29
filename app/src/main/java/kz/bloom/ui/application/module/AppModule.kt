package kz.bloom.ui.application.module

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.coroutines.Dispatchers
import kz.bloom.ui.main.data.MainRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val appModule = module {
    single { MainRepository() }
    single<CoroutineContext>(qualifier = named(name = "Main")) { Dispatchers.Main.immediate }
    single<CoroutineContext>(qualifier = named(name = "IO")) { Dispatchers.IO }
    single<StoreFactory> { DefaultStoreFactory() }
}