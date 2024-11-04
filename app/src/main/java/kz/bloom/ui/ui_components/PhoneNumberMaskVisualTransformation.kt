package kz.bloom.ui.ui_components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

public class PhoneNumberMaskVisualTransformation(length: Int, val mask: String = " ") :
    VisualTransformation {
    private val length = length + 1

    override fun filter(text: AnnotatedString): TransformedText {
        var output = ""

        when (text.text.length >= length) {
            true -> text.text.substring(range = 0 until length)
            false -> text.text
        }.let { trimmed ->
            trimmed.indices.forEach { index ->
                output += trimmed[index]

                if ((index == 2) || (index == 5) || (index == 7)) {
                    output += mask
                }
            }
        }

        val annotatedText = AnnotatedString(text = output)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                when {
                    offset <= 3 -> offset
                    offset <= 6 -> offset + 1
                    offset <= 8 -> offset + 2
                    offset <= 10 -> offset + 3
                    else -> 10
                }

            override fun transformedToOriginal(offset: Int): Int {

                return when {
                    offset <= 3 -> offset
                    offset <= 6 -> offset - 1
                    offset <= 8 -> offset - 2
                    offset <= 10 -> offset - 3
                    else -> {
                        val originalLength = text.text.length
                        val transformedLength = annotatedText.text.length
                        (offset - (transformedLength - originalLength)).coerceAtMost(
                            originalLength
                        )
                    }
                }
            }
        }

        return TransformedText(
            text = annotatedText,
            offsetMapping = offsetMapping
        )
    }
}