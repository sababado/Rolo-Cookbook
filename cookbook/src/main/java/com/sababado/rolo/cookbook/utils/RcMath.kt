package com.sababado.rolo.cookbook.utils

class RcMath {
    companion object {
        /**
         * Return a random integer between the provided to and from parameters.
         * @param to default is 1
         * @param from default is 0
         */
        fun randomInt(to: Int = 1, from: Int = 0): Int =
            (Math.random() * (to - from)).toInt() + from
    }
}