package kz.zeme.weather.core.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.reflect.KClass

class TypeMismatchException(expectedClass: KClass<*>, actualClass: KClass<*>) : IllegalArgumentException(
    "Expected instance of ${expectedClass.qualifiedName}, got ${actualClass.qualifiedName}"
)

/**
 * Приводит объект к типу `EXPECTED`, если это возможно.
 * Если объект не соответствует ожидаемому типу, выбрасывает исключение [TypeMismatchException].
 *
 * @param actual Объект, который требуется проверить.
 * @return Объект приведённый к типу `EXPECTED`.
 * @throws TypeMismatchException Если объект не является экземпляром `EXPECTED`.
 */
@[Throws(TypeMismatchException::class) OptIn(ExperimentalContracts::class)]
inline fun <reified EXPECTED> castOrThrow(actual: Any): EXPECTED {
    contract { returns() implies (actual is EXPECTED) }
    return if (actual !is EXPECTED) throw TypeMismatchException(EXPECTED::class, actual::class) else actual
}

/**
 * Приводит объект к типу `EXPECTED`, если это возможно.
 * Если объект не соответствует ожидаемому типу, возвращает `null`.
 *
 * @param actual Объект, который требуется проверить (может быть `null`).
 * @return Объект приведённый к типу `EXPECTED`, или `null`, если приведение невозможно.
 */
inline fun <reified R> castOrNull(actual: Any?): R? = actual as? R

fun <EXPECTED> castActionOrNull(actual: Any?, action: EXPECTED.() -> Unit): EXPECTED? {
    return (actual as? EXPECTED)?.apply(action)
}


@[Throws(TypeMismatchException::class) OptIn(ExperimentalContracts::class)]
inline fun <reified EXPECTED> ifInstance(actual: Any, action: EXPECTED.() -> Unit) {
    contract { returns() implies (actual is EXPECTED) }
    if (actual is EXPECTED) actual.action()
}