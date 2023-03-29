# Contributing

Contributions are always welcome, no matter how large or small!

We want this community to be friendly and respectful to each other. Please follow it in all your interactions with the project. Before contributing, please read the [code of conduct](./CODE_OF_CONDUCT.md).

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

Make sure your code passes TypeScript and ESLint. Run the following to verify:

```sh
yarn typecheck
yarn lint
```

To fix formatting errors, run the following:

```sh
yarn lint --fix
```

Remember to add tests for your change if possible. Run the unit tests by:

```sh
yarn test
```

To edit the Objective-C or Swift files, open `example/ios/EmbedReactNativeExample.xcworkspace` in XCode and find the source files at `Pods > Development Pods > @gr4vy/embed-react-native`.

To edit the Java or Kotlin files, open `example/android` in Android studio and find the source files at `gr4vy-embed-react-native` under `Android`.

### Commit message convention

Auto will use the PR title as the message in the change log. This means it needs to be human readable and meaningful. Try to avoid using ticket numbers or meta information - instead describe the value the change brings.

### Linting and tests

[ESLint](https://eslint.org/), [Prettier](https://prettier.io/), [TypeScript](https://www.typescriptlang.org/)

We use [TypeScript](https://www.typescriptlang.org/) for type checking, [ESLint](https://eslint.org/) with [Prettier](https://prettier.io/) for linting and formatting the code, and [Jest](https://jestjs.io/) for testing.

Our pre-commit hooks verify that the linter and tests pass when committing.

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
- `test`- 🧪 Tests (no version)
- `dependencies` - 🔩 Dependency Updates (no version)

Finally, to trigger a release you must include the release label:

- `release` - Create a release when PR is merged

### Scripts

The `package.json` file contains various scripts for common tasks:

- `yarn bootstrap`: setup project by installing all dependencies and pods.
- `yarn typecheck`: type-check files with TypeScript.
- `yarn lint`: lint files with ESLint.
- `yarn test`: run unit tests with Jest.
- `yarn example start`: start the Metro server for the example app.
- `yarn example android`: run the example app on Android.
- `yarn example ios`: run the example app on iOS.

### Sending a pull request

> **Working on your first pull request?** You can learn how from this _free_ series: [How to Contribute to an Open Source Project on GitHub](https://app.egghead.io/playlists/how-to-contribute-to-an-open-source-project-on-github).

When you're sending a pull request:

- Prefer small pull requests focused on one change.
- Verify that linters and tests are passing.
- Review the documentation to make sure it looks good.
- Follow the pull request template when opening a pull request.
- For pull requests that change the API or implementation, discuss with maintainers first by opening an issue.
