package com.sababado.rolo.cookbook.utils

import org.junit.Test

class RcMathTest {
    @Test
    fun randomInt() {
        var to = 1
        var from = 0
        var v: Int = 0

        for (i in 0..100) {
            v = RcMath.randomInt()
            assert(v in from..to)
        }

        to = 100
        for (i in 0..100) {
            v = RcMath.randomInt(to)
            assert(v in from..to)
        }

        to = 100
        from = 50
        for (i in 0..100) {
            v = RcMath.randomInt(to, from)
            assert(v in from..to)
        }
    }
}
