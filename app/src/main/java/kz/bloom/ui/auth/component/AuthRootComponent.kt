package kz.bloom.ui.auth.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kz.bloom.ui.auth.confirm_email.component.VerificationGenericComponent
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.auth.sign_up.component.SignUpComponent

public interface AuthRootComponent {
    public data class Model(
        public val smth: Boolean
    )

    public sealed interface Child {
        public data class SignIn(
            public val component: SignInComponent
        ) : Child
        public data class SignUp(
            public val component: SignUpComponent
        ) : Child
        public data class ConfirmEmail(
            public val component: VerificationGenericComponent
        ) : Child
    }

    public val model: Value<Model>

    public val childStack: Value<ChildStack<*, Child>>
}