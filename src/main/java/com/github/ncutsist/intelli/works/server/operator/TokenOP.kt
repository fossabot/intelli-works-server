package com.github.ncutsist.intelli.works.server.operator

import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class TokenOP (base64UrlSafeKey: String) {
    private val logger = LoggerFactory.getLogger(com.github.ncutsist.intelli.works.server.operator.TokenOP::class.java)
    private val keySpec: SecretKeySpec = SecretKeySpec(com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.decodeBase64(base64UrlSafeKey), "AES")

    /**
     * @return Generate a based64(URL safe) encoded, teacher specific, time sensitive token with current timestamp
     */
    fun generateToken(): String? {
        return generateToken(System.currentTimeMillis() / 1000)
    }

    /**
     * @return Generate a based64(URL safe) encoded, teacher specific, time sensitive token with a given timestamp
     */
    private fun generateToken(epoch: Long): String? {
        val epochInMinute = com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.cutEpochToMinute(epoch)

        val input = com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.longToBytes(epochInMinute)

        val iv = com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.generateIv(com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.bytesToLong(Arrays.copyOfRange(keySpec.encoded, 0, 8)), epochInMinute)
        val spec = GCMParameterSpec(128, iv)

        return try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec)
            val cipherText = cipher.doFinal(input)
            com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.encodeBase64(cipherText)
        } catch (e: Exception) {
            logger.error(e.toString())
            null
        }
    }

    /**
     * @param base64UrlSafeToken The based64(URL safe) encoded token
     * @return true if token is validate with current time
     */
    fun validateToken(base64UrlSafeToken: String): Boolean {
        return validateToken(System.currentTimeMillis() / 1000, com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.decodeBase64(base64UrlSafeToken))
    }

    /**
     * @param epoch A given timestamp in second
     * @param cipherText Token in raw byte[] status
     * @return true if token is validate with given time. Allow ±1 minutes error.(current minute, last and next minute, 3 minutes in total)
     */
    private fun validateToken(epoch: Long, cipherText: ByteArray): Boolean {
        val baseEpoch = com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.cutEpochToMinute(epoch)

        //validate right now, this only be correct(>0) if can decrypt
        if (parseToken(baseEpoch, cipherText) > 0) {
            return true
        }

        //validate one minutes ago
        return if (parseToken(baseEpoch - 60, cipherText) > 0) {
            true
        } else parseToken(baseEpoch + 60, cipherText) > 0

        //validate one minutes later
    }

    /**
     * @return Generate a based64(URL safe) encoded, teacher specific, time sensitive token with a given timestamp
     */
    fun generateCode(counter: Int): String? {
        val input = com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.longToBytes(counter.toLong())

        val iv = com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.generateIv(com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.bytesToLong(Arrays.copyOfRange(keySpec.encoded, 0, 8)), com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.bytesToLong(Arrays.copyOfRange(keySpec.encoded, 8, 16)))
        val spec = GCMParameterSpec(128, iv)

        return try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, spec)
            val cipherText = cipher.doFinal(input)
            com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.encodeBase64(cipherText)
        } catch (e: Exception) {
            logger.error(e.toString())
            null
        }
    }

    /**
     * @return Generate a based64(URL safe) encoded, teacher specific, time sensitive token with a given timestamp
     */
    fun validateCode(currentCounter: Int, code: String): Boolean {
        val iv = com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.generateIv(com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.bytesToLong(Arrays.copyOfRange(keySpec.encoded, 0, 8)), com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.bytesToLong(Arrays.copyOfRange(keySpec.encoded, 8, 16)))
        val spec = GCMParameterSpec(128, iv)

        return try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec)
            val plainText = cipher.doFinal(com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.decodeBase64(code))
            val codeCounter = com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.bytesToLong(plainText)
            codeCounter >= currentCounter
        } catch (e: Exception) {
            logger.debug(e.toString())
            false
        }
    }

    /**
     * @param epoch A given timestamp in second
     * @param cipherText Token in raw status
     * @return the timestamp when token is created if correct, -1 when incorrect
     */
    private fun parseToken(epoch: Long, cipherText: ByteArray): Long {
        val iv = com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.generateIv(com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.bytesToLong(Arrays.copyOfRange(keySpec.encoded, 0, 8)), epoch)
        val spec = GCMParameterSpec(128, iv)

        return try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec)
            val plainText = cipher.doFinal(cipherText)
            com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.bytesToLong(plainText)
        } catch (e: Exception) {
            logger.debug(e.toString())
            -1
        }

    }

    companion object {
        private val logger = LoggerFactory.getLogger(com.github.ncutsist.intelli.works.server.operator.TokenOP::class.java)

        /**
         * Check teacher side token
         */
        fun validateTeacherToken(raw: String): String? {
            if (raw.length <= 12) {
                return null
            }

            val uid = raw.substring(0, 12)
            val token = raw.substring(12)

            val teacher = com.github.ncutsist.intelli.works.server.operator.DataBaseOP.fetchTeacherByUid(uid)
                    ?: return null

            return if(teacher.token == token)
                teacher.name
            else
                null
        }

        /**
         * @return A random based64(URL safe) encoded AES 256bits key, return null when failed.
         */
        fun generateKey(): String? {
            return try {
                val random = SecureRandom.getInstanceStrong()
                val keyGen = KeyGenerator.getInstance("AES")
                keyGen.init(256, random)
                val key = keyGen.generateKey()
                com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.encodeBase64(key.encoded)
            } catch (e: NoSuchAlgorithmException) {
                com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.logger.error(e.toString())
                null
            }

        }

        /**
         * @return A random based64(URL safe) encoded 24 character long ticket
         */
        fun generateTicket(): String? {
            return try {
                val random = SecureRandom.getInstanceStrong()
                val ticket = ByteArray(18)
                random.nextBytes(ticket)
                com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.encodeBase64(ticket)
            } catch (e: NoSuchAlgorithmException) {
                com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.logger.error(e.toString())
                null
            }
        }

        /**
         * @return A random based64(URL safe) encoded 23 character long Uid
         */
        fun generateUid(): String? {
            return try {
                val random = SecureRandom.getInstanceStrong()
                val uid = ByteArray(9)
                random.nextBytes(uid)
                com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.encodeBase64(uid)
            } catch (e: NoSuchAlgorithmException) {
                com.github.ncutsist.intelli.works.server.operator.TokenOP.Companion.logger.error(e.toString())
                null
            }

        }

        /**
         * @param p1 first parameter
         * @param p2 second parameter
         * @return a 16 byte long Iv generate using 2 given parameters
         */
        private fun generateIv(p1: Long, p2: Long): ByteArray {
            var para1 = p1
            var para2 = p2
            val result = ByteArray(16)
            var i = 15
            //0-7, 8-15
            while (i > 8) {
                result[i] = (para2 and 0xFF).toByte()
                para2 = para2 shr 8//shift a byte
                i--
            }
            while (i > 0) {
                result[i] = (para1 and 0xFF).toByte()
                para1 = para1 shr 8//shift a byte
                i--
            }

            return result
        }

        /**
         * @param epoch A given timestamp in second
         * @return A timestamp cut down to floor in minute.
         */
        fun cutEpochToMinute(epoch: Long): Long {
            return epoch - epoch % 60//remove second
        }

        /**
         * @param x a long value
         * @return corresponding bytes
         */
        private fun longToBytes(x: Long): ByteArray {
            val buffer = ByteBuffer.allocate(java.lang.Long.BYTES)
            buffer.putLong(x)
            return buffer.array()
        }

        /**
         * @param bytes 8 bytes
         * @return corresponding long value
         */
        private fun bytesToLong(bytes: ByteArray): Long {
            val buffer = ByteBuffer.allocate(java.lang.Long.BYTES)
            buffer.put(bytes)
            buffer.flip()//need flip
            return buffer.long
        }

        /**
         * @param source bytes
         * @return A string encoded with url safe base64
         */
        private fun encodeBase64(source: ByteArray): String {
            return String(Base64.getUrlEncoder().encode(source), StandardCharsets.UTF_8)
        }

        /**
         * @param target A string encoded with url safe base64
         * @return decoded bytes
         */
        private fun decodeBase64(target: String): ByteArray {
            return Base64.getUrlDecoder().decode(target.toByteArray(StandardCharsets.UTF_8))
        }
    }
}