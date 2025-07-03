package com.eattalk.table.util
import kotlinx.datetime.Clock
import java.security.SecureRandom


object UUIDv7 {
    private val random = SecureRandom()

    fun generate(): ByteArray {
        // random bytes
        val value = ByteArray(16)
        random.nextBytes(value)

        // current timestamp in ms
        val timestamp = Clock.System.now().toEpochMilliseconds()

        // timestamp
        value[0] = ((timestamp shr 40) and 0xFF).toByte()
        value[1] = ((timestamp shr 32) and 0xFF).toByte()
        value[2] = ((timestamp shr 24) and 0xFF).toByte()
        value[3] = ((timestamp shr 16) and 0xFF).toByte()
        value[4] = ((timestamp shr 8) and 0xFF).toByte()
        value[5] = (timestamp and 0xFF).toByte()

        // version and variant
        value[6] = (value[6].toInt() and 0x0F or 0x70).toByte()
        value[8] = (value[8].toInt() and 0x3F or 0x80).toByte()

        return value
    }

    /** ByteArray를 UUID 문자열(8-4-4-4-12)로 포맷 */
    fun generateString(): String {
        val uuidArray = generate()
        val hex = uuidArray.joinToString("") { "%02x".format(it) }
        return buildString {
            append(hex.substring(0,  8)); append('-')
            append(hex.substring(8, 12)); append('-')
            append(hex.substring(12,16)); append('-')
            append(hex.substring(16,20)); append('-')
            append(hex.substring(20,32))
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(generateString())
    }
}