package kz.bloom.libraries

import com.arkivanov.decompose.Cancellation
import com.arkivanov.mvikotlin.rx.observer
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.core.store.Store

import com.arkivanov.decompose.value.Value

public val <Wrapped : Any> Store<*, Wrapped, *>.states: Value<Wrapped>
    get() = object : Value<Wrapped>() {

        override val value: Wrapped get() = state

        private val disposables = mutableMapOf<(Wrapped) -> Unit, Disposable>()

        override fun subscribe(observer: (Wrapped) -> Unit): Cancellation {
            // Subscribing to the store's states
            val disposable = states(observer(onNext = observer))

            // Storing the observer and its associated disposable for cleanup
            disposables[observer] = disposable

            return Cancellation {
                // Disposing when subscription is cancelled
                disposables.remove(observer)?.dispose()
            }
        }
    }