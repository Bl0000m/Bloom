package kz.bloom.ui.auth.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kz.bloom.ui.auth.confirm.component.VerificationGenericComponent
import kz.bloom.ui.auth.outcome.component.OutcomeComponent
import kz.bloom.ui.auth.pass_code.component.PassCodeComponent
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.auth.sign_up.component.SignUpComponent
import kz.bloom.ui.country_chooser.component.ChooseCountryComponent

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
        public data class OutcomePage(
            public val component: OutcomeComponent
        ) : Child
        public data class CountryChoose(
            public val component: ChooseCountryComponent
        ) : Child
        public data class PassCode(
            public val component: PassCodeComponent
        ) : Child
    }

    public val model: Value<Model>

    public val childStack: Value<ChildStack<*, Child>>
}