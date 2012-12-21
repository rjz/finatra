package com.twitter.finatra

import javax.crypto.{Cipher, KeyGenerator}
import java.security.SecureRandom
import java.util.Random
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

class CSRFToken(val secret: Array[Byte]) {

  def this(secret: String) = this(secret.getBytes)

  val ks:SecretKeySpec = new SecretKeySpec(secret, "Blowfish")
  val cipher:Cipher = Cipher.getInstance("Blowfish")

  def generate: String = {
    cipher.init(Cipher.ENCRYPT_MODE, ks)
    val bytes = cipher.doFinal("foo".getBytes)
    DatatypeConverter.printBase64Binary(bytes)
  }

  def isValid(dataStr: String): Boolean = {
    var result = false
    try {
      val data = decrypt(dataStr)
      //TODO: timeout logic here
      result = true
    } catch {
      case e:javax.crypto.IllegalBlockSizeException => result = false
      case _ => result = false
    }
    return result
  }

  def decrypt(dataStr: String): String = {
    val data = DatatypeConverter.parseBase64Binary(dataStr)
    cipher.init(Cipher.DECRYPT_MODE, ks)
    new String(cipher.doFinal(data))
  }

}
