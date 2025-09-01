package org.example.echojournalcmp.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Stable
sealed interface UiText {
    data class Dynamic(val value: String) : UiText

    @Stable
    data class LocalizedString(
        val resource: StringResource,
        val args: Array<Any> = arrayOf()
    ) : UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as LocalizedString

            if (resource != other.resource) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = resource.hashCode()
            result = 31 * result + args.contentHashCode()
            return result
        }
    }

    @Composable
    fun UiText.asString(): String {
        return when(this) {
            is Dynamic -> value
            is LocalizedString -> stringResource(resource, *args)
        }
    }
}