package com.example.ioagh.gamefinder.validators

import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class EmailValidatorTest {

    @Test
    fun shouldValidateSimpleEmail() {
        assertTrue(EmailValidator.validate("niceandsimple@example.com"))
    }

    @Test
    fun shouldValidateEmailWithDotsBeforeDomain() {
        assertTrue(EmailValidator.validate("a.little.unusual@example.com"))
    }

    @Test
    fun shouldValidateEmailWithMultipleDots() {
        assertTrue(EmailValidator.validate("a.little.more.unusual@dept.example.com"))
    }

    @Test
    fun shouldValidateEmailWithFloors() {
        assertTrue(EmailValidator.validate("_______@example.com"))
    }
    @Test
    fun shouldValidateEmailWithIpAsDomain1() {
        assertTrue(EmailValidator.validate("email@123.123.123.123"))
    }

    @Test
    fun shouldValidateEmailWithIpAsDomain2() {
        assertTrue(EmailValidator.validate("email@[123.123.123.123]"))
    }
    @Test
    fun shouldValidateEmailWithPlusCharacter() {
        assertTrue(EmailValidator.validate("email@[123.123.123.123]"))
    }
    @Test
    fun shouldValidateEmailWithDigitsBeforeDomain() {
        assertTrue(EmailValidator.validate("1234567890@example.com"))
    }
    @Test
    fun shouldValidateEmailWithDashes() {
        assertTrue(EmailValidator.validate("firstname-lastname@unusual-example.com"))
    }
//    @Test
//    fun shouldNotValidateEmailWithInvalidIpAddress() {
//        assertFalse(EmailValidator.validate("email@111.222.333.44444"))
//    }
    @Test
    fun shouldNotValidateEmailWithWeirdCharacters1() {
        assertFalse(EmailValidator.validate("#@%^%#\$@#\$@#.com"))
    }
    @Test
    fun shouldNotValidateEmailWithWeirdCharacters2() {
        assertFalse(EmailValidator.validate("あいうえお@example.com"))
    }

    @Test
    fun shouldNotValidateEmailWithoutAtCharacter() {
        assertFalse(EmailValidator.validate("email.pl"))
    }

    @Test
    fun shouldNotValidateEmailWithoutDomain() {
        assertFalse(EmailValidator.validate("email@"))
    }

    @Test
    fun shouldNotValidateEmailWithoutCorrectDomain() {
        assertFalse(EmailValidator.validate("email@mailpl"))
    }

    @Test
    fun shouldNotValidateEmptyString() {
        assertFalse(EmailValidator.validate(""))
    }

    @Test
    fun shouldNotValidateWithMultipleDots() {
        assertFalse(EmailValidator.validate("Abc..123@example.com"))
        assertFalse(EmailValidator.validate("123@example...com"))
    }
    @Test
    fun shouldNotValidateWithMultipleAtCharacters() {
        assertFalse(EmailValidator.validate("email@example@example.com"))
    }
}