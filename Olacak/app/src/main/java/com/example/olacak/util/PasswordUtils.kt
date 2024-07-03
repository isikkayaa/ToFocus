package com.example.olacak.util


import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordUtils {

    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }


    fun checkPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
    }
}
