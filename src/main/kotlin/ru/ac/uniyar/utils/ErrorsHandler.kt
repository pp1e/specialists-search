package ru.ac.uniyar.utils

import org.http4k.lens.Failure

class ErrorsHandler(
    private val errors: List<Failure>,
    private val customErrors: Map<String, String> = emptyMap()
) {
    fun errorToString(fieldName: String): String {
        if (customErrors.containsKey(fieldName))
            return customErrors[fieldName]!!
        else
            for (error in errors)
                if (error.meta.name == fieldName)
                    return translateErrorMessage(error.type)
        return ""
    }

    private fun translateErrorMessage(errorType: Failure.Type): String {
        return when (errorType) {
            Failure.Type.Invalid -> "Заполните корректными данными!"
            Failure.Type.Missing -> "Должно быть заполнено!"
            Failure.Type.Unsupported -> "Значение не поддерживается!"
        }
    }
}
