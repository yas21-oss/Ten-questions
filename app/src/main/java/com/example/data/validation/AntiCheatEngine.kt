package com.example.data.validation

import java.security.MessageDigest

object AntiCheatEngine {

  /**
   * Generates a telemetry audit hash for stage completion verification
   */
  fun generateToken(
    uid: String,
    stageNumber: Int,
    score: Int,
    timeTakenSeconds: Long
  ): String {
    val payload = "$uid:$stageNumber:$score:$timeTakenSeconds"
    return sha256(payload)
  }

  /**
   * Validates stage completion integrity:
   * - Cannot skip ahead to locked stages
   * - Must have answered all 10 questions correctly
   * - Must meet realistic human response time (> 3 seconds for 10 typed answers)
   * - Integrity token matches session payload
   */
  fun validateStageCompletion(
    uid: String,
    stageNumber: Int,
    currentUnlockedStage: Int,
    score: Int,
    timeTakenSeconds: Long,
    token: String
  ): Result<Unit> {
    if (stageNumber > currentUnlockedStage) {
      return Result.failure(IllegalStateException("Progression validation: Cannot submit locked stage $stageNumber"))
    }

    if (score < 10) {
      return Result.failure(IllegalStateException("Progression validation: Score $score/10 does not meet win criteria"))
    }

    // A human cannot read and type 10 full answers in under 3 seconds total
    if (timeTakenSeconds < 3) {
      return Result.failure(IllegalStateException("Suspicious response duration ($timeTakenSeconds s)"))
    }

    val expectedToken = generateToken(uid, stageNumber, score, timeTakenSeconds)
    if (expectedToken != token) {
      return Result.failure(SecurityException("Session telemetry mismatch"))
    }

    return Result.success(Unit)
  }

  private fun sha256(input: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(input.toByteArray(Charsets.UTF_8))
    return digest.joinToString("") { "%02x".format(it) }
  }
}
