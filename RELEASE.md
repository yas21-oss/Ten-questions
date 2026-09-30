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

## 🔄 Unified Release Pipeline (`.github/workflows/release.yml`)

The release system uses a **single, deterministic two-stage pipeline** that triggers automatically upon any push to `main` (or via manual `workflow_dispatch`). It does not rely on cross-workflow triggers or `GITHUB_TOKEN` event propagation.

### Stage 1: `version_and_tag` (Detection, Validation & Tagging)
1. **Repository Checkout**: Checks out full Git history (`fetch-depth: 0`).
2. **Version Extraction**: Extracts `appVersionName` and `appVersionCode` from `gradle.properties`.
3. **SemVer & Code Validation**: Enforces strict SemVer syntax (`MAJOR.MINOR.PATCH`) and positive integer `versionCode`.
4. **Duplicate Protection & Recovery**:
   - Queries `refs/tags/v<versionName>` and checks GitHub API (`gh release view`) for published releases and their attached assets.
   - If the GitHub Release for this version already exists **AND contains the APK artifact**: gracefully exits to prevent redundant runs.
   - If the release exists **but is missing the APK artifact** (e.g. created empty or aborted previously): automatically proceeds to build and attach the APK and checksum to the release.
   - If the tag exists but no GitHub Release was published: proceeds to build and publish the release.
   - If neither exists: verifies `versionCode` strictly increases over previous releases, then creates and pushes annotated Git tag `v<versionName>`.

### Stage 2: `release` (Build, Test, Sign & Publish)
*Runs automatically when Stage 1 determines a release should be published.*
1. **Exact Checkout**: Checks out the repository at the exact release tag (`ref: v<versionName>`).
2. **JDK & Gradle Setup**: Uses Temurin JDK 21 and the committed Gradle wrapper (`./gradlew`).
3. **Unit Tests Gate**: Executes `./gradlew :app:testDebugUnitTest`. If tests fail, the workflow aborts immediately before any APK is built or published.
4. **Signing**: Detects permanent production secrets (`KEYSTORE_BASE64`, `STORE_PASSWORD`, `KEY_PASSWORD`) and uses them; otherwise auto-generates a release key.
5. **APK Assembly**: Builds release APK (`./gradlew :app:assembleRelease`).
6. **Signature Verification**: Verifies cryptographic signature using `apksigner` (or `jarsigner`).
7. **Staging & Checksum**: Stages `TenQuestions-v<version>-release.apk` and generates cryptographic checksum `TenQuestions-v<version>-release.apk.sha256`.
8. **Changelog Generation**: Generates release notes from Git commits since the previous release tag.
9. **GitHub Release Publication**: Publishes the official GitHub Release with both assets attached (`softprops/action-gh-release@v2`).
10. **Post-Upload Verification**: Uses `gh release view` to verify both the APK and checksum are live on GitHub, with automatic CLI fallback upload (`gh release upload --clobber`) and hard exit on missing assets.

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

### 2. Permanent Production Signing Setup (Seamless In-Place Updates & Play Store)
To enable seamless updates where users can install new versions without uninstalling previous builds, configure a permanent signing keystore in GitHub Secrets (**Repository Settings > Secrets and variables > Actions**).

#### Step A: Generate the Keystore (e.g. from Termux on Android or a Linux/macOS terminal)
If using **Termux on your Android phone**, ensure OpenJDK is installed:
```bash
pkg install openjdk-21 -y
```

Run this command to create your permanent keystore:
```bash
keytool -genkeypair -v \
  -keystore my-upload-key.jks \
  -alias upload \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -dname "CN=Ten Questions, O=TenQuestions, C=US"
```
*Note: Keytool will prompt you to enter and confirm your password. Choose a strong password and remember it.*

#### Step B: Convert Keystore to Base64
Run this command to convert the keystore into a single Base64 string without line breaks:
```bash
base64 -w 0 my-upload-key.jks > keystore_base64.txt
```
*(On macOS: `base64 -i my-upload-key.jks -o keystore_base64.txt`)*

Display the Base64 string to copy it:
```bash
cat keystore_base64.txt
```
*(In Termux with Termux:API, you can also copy directly to clipboard with: `base64 -w 0 my-upload-key.jks | termux-clipboard-set`)*

#### Step C: Add the Three GitHub Actions Secrets
In your GitHub repository, navigate to **Settings > Secrets and variables > Actions > New repository secret** and add:

1. **`KEYSTORE_BASE64`**: The exact Base64 string from `keystore_base64.txt`.
2. **`STORE_PASSWORD`**: The exact keystore password you entered when creating the keystore.
3. **`KEY_PASSWORD`**: The exact key password for alias `upload` (the same password if you pressed Enter at the prompt).

#### Step D: Important Security Rules
- **NEVER** commit `my-upload-key.jks` or `keystore_base64.txt` to the Git repository (they are ignored by `.gitignore`).
- **Store a safe backup** of `my-upload-key.jks` in a secure location (e.g., password manager, encrypted cloud drive). If you lose this key, future app updates cannot be installed without uninstalling the previous version.
- Once configured, all future GitHub Actions release runs will permanently use this keystore and will never replace it.

---

### 📋 Setup Checklist
- [ ] Create keystore
- [ ] Convert to Base64
- [ ] Add `KEYSTORE_BASE64`
- [ ] Add `STORE_PASSWORD`
- [ ] Add `KEY_PASSWORD`
- [ ] Run first release
- [ ] Verify APK asset

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
