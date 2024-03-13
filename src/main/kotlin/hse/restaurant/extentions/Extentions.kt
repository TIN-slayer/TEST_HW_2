package hse.restaurant.extentions

fun Boolean.toInt() = if (this) 1 else 0
fun Int.toBoolean() = this >= 1
fun String.toBoolean() = this == "д"