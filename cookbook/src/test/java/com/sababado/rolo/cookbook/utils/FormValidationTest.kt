package com.sababado.rolo.cookbook.utils

import org.junit.Test

class FormValidationTest {
    @Test
    fun testValidEmail() {
        assert(FormValidation.validateEmail("rat@gmail.c"))
        assert(FormValidation.validateEmail("rat@gmail.com"))
        assert(FormValidation.validateEmail("robert.james.szabo@gmail.vcu.edu"))
    }

    @Test
    fun testInvalidEmail() {
        assert(!FormValidation.validateEmail(""))
        assert(!FormValidation.validateEmail(null))
        assert(!FormValidation.validateEmail("giberrish"))
        assert(!FormValidation.validateEmail("sdfosdj##gmail.com"))
        assert(!FormValidation.validateEmail("oijwoijeoij***#. fsdoijdf"))
        assert(!FormValidation.validateEmail("rat@gmail"))
        assert(!FormValidation.validateEmail("rat@gmail."))
        assert(!FormValidation.validateEmail("rat @ gmail . com"))
    }
}