# Automated Release Guide

This project features a fully automated release pipeline powered by GitHub Actions. You never need to manually create Git tags, write release notes, or upload APK release binaries.

---

## 🎯 How to Release a New Version

The **only thing** you need to do to release a new version is:

1. **Update the version in `gradle.properties`** (the single source of truth):
   ```properties
   appVersionName=1.1.0
   appVersionCode=2
   ```
2. **Commit the change**:
   ```bash
   git commit -am "Release version 1.1.0"
   ```
3. **Push to the release branch (`main`)**:
   ```bash
   git push origin main
   ```

GitHub Actions will automatically detect the version bump, validate it, tag it, run tests, build the signed release APK, compute its SHA-256 checksum, generate a changelog from commit history, and publish an official GitHub Release with attached assets.

---

## 🏗️ Architecture & Single Source of Truth

The Android application version is defined in **exactly one place**:

- **Location**: `gradle.properties` (in repository root)
  - `appVersionName`: The user-visible semantic version (e.g. `1.0.0`, `1.2.0`, `2.10.5`).
  - `appVersionCode`: The internal Android versionCode (must be a positive integer strictly greater than any previous release, e.g. `1`, `2`, `3`).

`app/build.gradle.kts` dynamically reads these properties via `providers.gradleProperty`:
```kotlin
val appVersionName: String = providers.gradleProperty("appVersionName").getOrElse("1.0.0")
val appVersionCode: Int = providers.gradleProperty("appVersionCode").map { it.toInt() }.getOrElse(1)

versionCode = appVersionCode
versionName = appVersionName
```

### Validation Rules
- **Semantic Versioning (`versionName`)**: Must strictly follow the `MAJOR.MINOR.PATCH` pattern (e.g., `1.0.0`, `1.1.0`, `2.0.1`). Malformed versions (such as `1.0`, `v1.0.0`, or alpha tags) are rejected by the workflow.
- **Strictly Incremental Build Code (`versionCode`)**: Must be an integer greater than `0` and strictly greater than the highest `versionCode` among previous releases.
- **Duplicate Protection**: If `appVersionName` has not changed between commits on `main`, the workflow logs an informational message and safely halts without creating duplicate tags or duplicate releases.

---

## 🔄 Automated Workflows

### 1. Version Detection & Tagging (`.github/workflows/version-and-tag.yml`)
- **Trigger**: Push to release branch (`main`).
- **Concurrency**: Protected against concurrent pushes (`cancel-in-progress: false`).
- **Steps**:
  1. Checks out repository with full history (`fetch-depth: 0`).
  2. Extracts `appVersionName` and `appVersionCode` from `gradle.properties`.
  3. Validates SemVer formatting (`MAJOR.MINOR.PATCH`).
  4. Validates `versionCode` is a positive integer.
  5. Checks whether tag `v<versionName>` already exists. If yes, exits cleanly.
  6. Inspects all prior release tags and verifies `versionCode` > highest prior `versionCode`.
  7. Creates an annotated Git tag `v<versionName>` and pushes to `origin` using `GITHUB_TOKEN` permissions (`contents: write`).

### 2. Build & Publish Release (`.github/workflows/release.yml`)
- **Trigger**: Push of any release tag (`v*.*.*`).
- **Build Environment**: Uses JDK 21 (Temurin) and the project's committed Gradle 9.3.1 wrapper (`./gradlew`).
- **Test Gate**: Runs JVM unit test suite (`./gradlew :app:testDebugUnitTest`). If tests fail, release publishing immediately halts.
- **Automated Signing**: Automatically generates a valid signing keystore during the workflow run and signs the release APK with Android APK Signature Scheme v2. No manual keystore creation or GitHub Secrets configuration is required.
- **Optional Secret Override**: If `KEYSTORE_BASE64`, `STORE_PASSWORD`, and `KEY_PASSWORD` secrets are provided, the workflow automatically uses that persistent production keystore instead of auto-generating one.
- **Signature Verification**: Verifies the resulting APK signature using `apksigner` before release staging.
- **Artifacts & Checksums**: Builds `TenQuestions-v<version>-release.apk` and generates cryptographic checksum `TenQuestions-v<version>-release.apk.sha256`.
- **Changelog & Release Notes**: Automatically aggregates commit history since the previous release tag.
- **Publish**: Publishes official GitHub Release with attached APK and SHA-256 checksum.

---

## 🔑 Signing Architecture: Automated vs. Production

### 1. Fully Automated Signing (Zero Configuration — Default)
- **Status**: Enabled by default. No GitHub Secrets or manual keystores are required.
- **How it works**: The GitHub Actions runner dynamically creates a valid 2048-bit RSA release signing keystore and signs the APK using Android's APK Signature Scheme v2.
- **Installability**: The output APK (`TenQuestions-v<version>-release.apk`) is completely valid, signed, and installable on Android devices (via sideloading, ADB, or file manager).
- **⚠️ Known Limitation for In-Place Updates**:
  Because each automated GitHub Actions run operates in an ephemeral virtual environment, the automatically generated signing key differs between workflow runs. Android security enforces that an existing app can only be updated in-place if the new APK is signed with the **exact same certificate**.
  - **Symptom**: If you install version `1.1.0` over an installed `1.0.0` built with automated signing, Android will reject the update with a signature mismatch error (*"App not installed as package conflicts with an existing package"*).
  - **Workaround**: Uninstall the previous version before installing the new APK.

### 2. Permanent Production Signing (Optional for Play Store / In-Place Updates)
If you require seamless in-place updates without uninstalling, or are distributing the app via the Google Play Store, you can configure a permanent keystore in GitHub Secrets (**Settings > Secrets and variables > Actions**):

| Secret Name | Description | Example / Format |
|---|---|---|
| `KEYSTORE_BASE64` | Base64-encoded upload keystore (`.jks` or `.keystore`) containing alias `upload` | `base64 -w 0 my-upload-key.jks` |
| `STORE_PASSWORD` | Store password for the keystore | Plain text password string |
| `KEY_PASSWORD` | Key password for key alias `upload` | Plain text password string |

When these secrets are detected, the workflow automatically uses the permanent keystore instead of auto-generating one.

---

## 🛡️ Failure and Safety Guarantees

1. **If unit tests fail**:
   The workflow fails immediately during step `Run Unit Tests`. No release is published, no APK asset is uploaded, and the tag remains on the commit.
2. **If APK signing fails**:
   The workflow verifies the APK cryptographic signature using `apksigner`/`jarsigner`. If unsigned or malformed, the workflow aborts with a hard failure before staging or publishing.
3. **If APK or checksum assets are missing**:
   The workflow performs pre-publish checks and post-upload verification using GitHub CLI (`gh release view`). If either `TenQuestions-v<version>-release.apk` or `TenQuestions-v<version>-release.apk.sha256` is missing, the workflow fails hard.
4. **If multiple commits are pushed**:
   Serialized by GitHub Actions concurrency lock.
5. **Infinite loop prevention**:
   `version-and-tag.yml` only listens to branch pushes (`main`). `release.yml` only listens to tag pushes (`v*`). Tag creation does not trigger `version-and-tag.yml`.
