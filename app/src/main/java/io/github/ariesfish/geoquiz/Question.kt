package io.github.ariesfish.geoquiz

data class Question(val questionResId: Int,
                    val answer: Boolean,
                    var isAnswered: Boolean = false,
                    var isCheated: Boolean = false)