# Automated Release Guide

This project features a fully automated CI/CD release system powered by GitHub Actions. You never need to manually create Git tags, write release notes, or upload APK release binaries.

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
3. **Push to the release branch** (`main` or `master`):
   ```bash
   git push origin main
   ```

That's it! GitHub Actions handles the rest automatically.

---

## 🏗️ Architecture & Single Source of Truth

The Android application version is defined in **exactly one place**:

- **Location**: `gradle.properties`
  - `appVersionName`: The user-visible semantic version (e.g. `1.0.0`, `1.2.0`, `2.10.5`).
  - `appVersionCode`: The internal Android build number (must be a positive integer strictly greater than the previous release, e.g. `1`, `2`, `3`).

### Validation Rules
- **Semantic Versioning (`versionName`)**: Must strictly follow the `MAJOR.MINOR.PATCH` pattern (e.g., `1.0.0`, `1.1.0`, `2.0.1`). Malformed versions (such as `1.0`, `v1.0.0`, or alpha tags) are rejected by the validation step.
- **Incremental Build Code (`versionCode`)**: Must be an integer greater than `0` and strictly greater than the previous released version's `versionCode`.
- **Duplicate Protection**: If `appVersionName` has not changed between commits, the workflow safely exits without creating duplicate tags or duplicate releases.

---

## 🔄 Automated Workflows

### 1. Version Detection & Tagging (`.github/workflows/version-and-tag.yml`)
- **Trigger**: Every push to `main` (or `master`).
- **Steps**:
  1. Checks out repository.
  2. Extracts `appVersionName` and `appVersionCode` from `gradle.properties`.
  3. Validates SemVer formatting and positive integer requirements.
  4. Checks whether tag `v<versionName>` already exists. If yes, exits cleanly.
  5. Verifies `versionCode` is greater than previous releases.
  6. Creates an annotated Git tag `v<versionName>` and pushes it to GitHub using the standard `GITHUB_TOKEN`.

### 2. Build & Publish Release (`.github/workflows/release.yml`)
- **Trigger**: Whenever a release tag (`v*.*.*`) is pushed.
- **Steps**:
  1. Checks out code with complete commit history.
  2. Sets up JDK 21 (Temurin) and Gradle 9.3.1.
  3. Executes unit tests (`gradle :app:testDebugUnitTest`).
  4. Configures signing (using custom keystore secrets if provided, or generating a valid signed upload keystore).
  5. Builds the optimized release APK (`gradle :app:assembleRelease`).
  6. Computes SHA-256 cryptographic checksums for security verification.
  7. Automatically compiles changelog notes from commit history.
  8. Publishes an official GitHub Release with attached `TenQuestions-v<version>.apk` and `.sha256` checksum files.

---

## 🔐 Production Keystore Secrets (Optional)

By default, the workflow signs the release APK so it is installable right away. For Google Play or custom production signing, you can optionally add these **Repository Secrets** in your GitHub repository (**Settings > Secrets and variables > Actions**):

- `KEYSTORE_BASE64`: Base64-encoded string of your `.jks` or `.keystore` file (`base64 -w 0 my-upload-key.jks`).
- `STORE_PASSWORD`: Keystore password.
- `KEY_PASSWORD`: Key password.
