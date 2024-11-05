package kz.bloom.ui.ui_components.preference.security

import android.util.Base64
import java.nio.charset.Charset
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Encrypt and decrypt messages using AES 256 bit encryption that are compatible with AESCrypt-ObjC and AESCrypt Ruby.
 * https://github.com/scottyab/AESCrypt-Android/blob/master/aescrypt/src/main/java/com/scottyab/aescrypt/AESCrypt.java
 */
object AESCrypt {
    private const val AES_MODE = "AES/CBC/PKCS7Padding"
    private const val CHARSET = "UTF-8"
    private const val HASH_ALGORITHM = "SHA-256"

    //AESCrypt-ObjC uses blank IV (not the best security, but the aim here is compatibility)
    private val ivBytes = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)

    /**
     * Generates SHA256 hash of the password which is used as key
     *
     * @param password used to generated key
     * @return SHA256 of the password
     */
    private fun generateKey(password: String): SecretKeySpec? {
        try {
            val digest = MessageDigest.getInstance(HASH_ALGORITHM)
            val bytes = password.toByteArray(charset("UTF-8"))
            digest.update(bytes, 0, bytes.size)
            val key = digest.digest()
            return SecretKeySpec(key, "AES")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Encrypt and encode message using 256-bit AES with key generated from password.
     *
     * @param password used to generated key
     * @param message  the thing you want to encrypt assumed String UTF-8
     * @return Base64 encoded CipherText
     */
    fun encrypt(password: String, message: String?): String? {
        try {
            if(!message.isNullOrBlank()) {
                val key = generateKey(password)
                val cipherText = encrypt(key, ivBytes, message.toByteArray(charset(CHARSET)))

                //NO_WRAP is important as was getting \n at the end
                return Base64.encodeToString(cipherText, Base64.NO_WRAP)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * More flexible AES encrypt that doesn't encode
     *
     * @param key     AES key typically 128, 192 or 256 bit
     * @param iv      Initiation Vector
     * @param message in bytes (assumed it's already been decoded)
     * @return Encrypted cipher text (not encoded)
     */
    fun encrypt(key: SecretKeySpec?, iv: ByteArray?, message: ByteArray?): ByteArray? {
        try {
            val cipher = Cipher.getInstance(AES_MODE)
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
            return cipher.doFinal(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Decrypt and decode ciphertext using 256-bit AES with key generated from password
     *
     * @param alias                   used to generated key
     * @param base64EncodedCipherText the encrpyted message encoded with base64
     * @return message in Plain text (String UTF-8)
     */
    fun decrypt(alias: String, base64EncodedCipherText: String?): String? {
        try {
            if(!base64EncodedCipherText.isNullOrBlank()) {
                val key = generateKey(alias)
                val decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP)
                val decryptedBytes = decrypt(key, ivBytes, decodedCipherText)
                return String(decryptedBytes!!, Charset.forName(CHARSET))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * More flexible AES decrypt that doesn't encode
     *
     * @param key               AES key typically 128, 192 or 256 bit
     * @param iv                Initiation Vector
     * @param decodedCipherText in bytes (assumed it's already been decoded)
     * @return Decrypted message cipher text (not encoded)
     */
    fun decrypt(key: SecretKeySpec?, iv: ByteArray?, decodedCipherText: ByteArray?): ByteArray? {
        try {
            val cipher = Cipher.getInstance(AES_MODE)
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
            return cipher.doFinal(decodedCipherText)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}