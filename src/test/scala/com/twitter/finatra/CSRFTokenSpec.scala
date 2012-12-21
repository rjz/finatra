package com.twitter.finatra

import test.ShouldSpec
import java.util

import javax.xml.bind.DatatypeConverter


class CSRFTokenSpec extends ShouldSpec {

  "CSRFToken" should "be able to decrypt generated tokens" in {
    val secret = "foo"

    val csrf = new CSRFToken(secret)

    val token = csrf.generate

    val result = csrf.isValid(token)

    result should be === true

  }

  "CSRFToken" should "not be able to decrypt bullshit tokens" in {
    val secret = "foo"

    val csrf = new CSRFToken(secret)

    val result = csrf.isValid("not it")

    result should be === false

  }
}
