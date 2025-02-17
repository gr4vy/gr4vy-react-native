# Contributing

## Development workflow

To get started with the project, run `yarn` in the root directory to install the required dependencies for each package:

```sh
yarn
```

> While it's possible to use [`npm`](https://github.com/npm/cli), the tooling is built around [`yarn`](https://classic.yarnpkg.com/), so you'll have an easier time if you use `yarn` for development.

While developing, you can run the [example app](/example/) to test your changes. Any changes you make in your library's JavaScript code will be reflected in the example app without a rebuild. If you change any native code, then you'll need to rebuild the example app.

To start the packager:

```sh
yarn example start
```

To run the example app on Android:

```sh
yarn example android
```

To run the example app on iOS:

```sh
yarn example ios
```

To see the actual payment options on checkout, the [embed-ui](https://github.com/gr4vy/embed-ui) app needs to be running. By default, `embed.sandbox.spider.gr4vy.app` will be used, but you can set the `GR4VY_ID` environment variable to something else to use a different instance. To use your local running instance of `embed-ui`, set `GR4VY_ID` to `dev` and use our internal [dev HTTPS proxy](https://github.com/gr4vy/dev-https-proxy). Make sure the generated certificates are installed on your simulator by following these instructions https://github.com/gr4vy/dev-https-proxy?tab=readme-ov-file#installing-certificates-on-ios-simulator.

You can install the certificates the same way when running the app on your device (vs simulator), although in that case you'll also need to update your hosts file to use your computer's IP address instead of `127.0.0.1`. Then, on your device, make sure to use a manual DNS set to that specific IP. This way your device will use the host's IP to resolve `embed.sandbox.dev.gr4vy.app`.

### Using a local unreleased version of `gr4vy-ios` or `gr4vy-android`

If you want to use a local unreleased version of `gr4vy-ios` or `gr4vy-android` while testing with the example app, follow these steps:

<details>
<summary>iOS</summary>

1. Clone the `gr4vy-ios` repository locally
2. In the `example/ios/Podfile` file, add `pod 'gr4vy-ios', :path => '/path/to/gr4vy-ios'` to the very end.
3. Run `yarn` again to install the local pod.

</details>

<details>
<summary>Android</summary>

https://stackoverflow.com/questions/61806859/linking-an-android-library-that-is-on-local-machine/68887793#68887793

1. Build `aar` by running `./gradlew assembleRelease` from inside the `gr4vy-android` local project
2. In Android Studio, in the example app project, go to: File > Project Structure > Dependencies.
3. In: Modules > app > Declared Dependencies, click '+' and select 'Jar Dependency'. (Even though you are trying to use an .aar file, you still select 'Jar Dependency').
4. In the 'Add Jar/Aar Dependency' popup dialog
   a. in step 1 enter the path to your .aar file on your local drive, and
   b. in step 2 select 'implementation'

If everything worked out, your `build.gradle` should have a line in the dependencies that looks like:

```
dependencies {
    ...
    implementation files('../../../MyFolder/MyLibraryFile.aar')
}
```

or just add it directly in `build.gradle`:

```
dependencies {
    ...
    implementation files('/path/to/gr4vy-android/GravySDK/build/outputs/aar/GravySDK-release.aar')
}
```

</details>

Make sure your code passes TypeScript and ESLint. Run the following to verify:

```sh
yarn typecheck
yarn lint
```

To fix formatting errors, run the following:

```sh
yarn lint --fix
```

To edit the Objective-C or Swift files, open `example/ios/EmbedReactNativeExample.xcworkspace` in XCode and find the source files at `Pods > Development Pods > @gr4vy/embed-react-native`.

To edit the Java or Kotlin files, open `example/android` in Android studio and find the source files at `gr4vy-embed-react-native` under `Android`.

### Commit message convention

Auto will use the PR title as the message in the change log. This means it needs to be human readable and meaningful. Try to avoid using ticket numbers or meta information - instead describe the value the change brings.

### Linting

[ESLint](https://eslint.org/), [Prettier](https://prettier.io/), [TypeScript](https://www.typescriptlang.org/)

We use [TypeScript](https://www.typescriptlang.org/) for type checking, [ESLint](https://eslint.org/) with [Prettier](https://prettier.io/) for linting and formatting the code.

Our pre-commit hooks verify that the linter pass when committing.

### Releasing a PR

We use [auto](https://github.com/intuit/auto) to make it easier to publish new versions. It handles common tasks like bumping version based on semver, creating tags and releases etc.

Raising a PR against the `main` branch will trigger Github actions.

PR titles are limited in character length and you may need to provide more details. You can add a `## Release Notes` section for this. Only this section will appear in the Change Log, not the entire body of the PR.

```md
## Release Notes

Write additional notes here...
```

Labels inform what type of change has occurred so that the correct semver number can be assigned for the release.

The following labels will increment the version number:

- `major` - 💥 Breaking Change (major)
- `minor` - 🚀 Enhancement (minor)
- `patch` - 🐛 Bug Fix (patch)
- `performance` - 🏎 Performance (patch)

The following labels will have no effect on the version:

- `internal` - 🏠 Internal (no version)
- `documentation` - 📝 Documentation (no version)
- `dependencies` - 🔩 Dependency Updates (no version)

Finally, to trigger a release you must include the release label:

- `release` - Create a release when PR is merged

### Scripts

The `package.json` file contains various scripts for common tasks:

- `yarn bootstrap`: setup project by installing all dependencies and pods.
- `yarn typecheck`: type-check files with TypeScript.
- `yarn lint`: lint files with ESLint.
- `yarn example start`: start the Metro server for the example app.
- `yarn example android`: run the example app on Android.
- `yarn example ios`: run the example app on iOS.

### Sending a pull request

> **Working on your first pull request?** You can learn how from this _free_ series: [How to Contribute to an Open Source Project on GitHub](https://app.egghead.io/playlists/how-to-contribute-to-an-open-source-project-on-github).

When you're sending a pull request:

- Prefer small pull requests focused on one change.
- Verify that linters are passing.
- Review the documentation to make sure it looks good.
- Follow the pull request template when opening a pull request.
- For pull requests that change the API or implementation, discuss with maintainers first by opening an issue.

### SDK upgrades

From time to time a new version of the iOS and/or the Android SDKs will be published. To upgrade the version used by this library, do the following:

- for iOS, open the `gr4vy-embed-react-native.podspec` file and bump the version number in `s.dependency "gr4vy-ios", "X.X.X"` (check that it corresponds to what is published [here](https://github.com/CocoaPods/Specs/tree/master/Specs/4/f/7/gr4vy-ios)). Then, run `yarn` in the project root to re-run `pod install` and make sure the `Podfile.lock` references the new version
- for Android, open the `android/build.gradle` file and bump the version number in `implementation "com.github.gr4vy:gr4vy-android:vX.X.X"`
- Check that everything still builds and works fine with the example app
