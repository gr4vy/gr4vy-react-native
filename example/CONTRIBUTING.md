# Contributing

## Development workflow

See [README.md][../README.md] for details on how to run the app in development

## Beta releases

We use [fastlane](https://fastlane.tools/) to orchestrate the build / release process of example app for internal testing. The distribution is handled by [Firebase App Distribution](https://firebase.google.com/docs/app-distribution); logging-in with the `code@gr4vy.com` Google account will take you to the console where you can see the project with its registered iOS and Android apps.

We use a specific version of Ruby to install the dependencies needed. We recommend using [rbenv](https://github.com/rbenv/rbenv) to manage multiple versions of Ruby on your system, but you can use whatever you want as long as reading the `.ruby-version` file is supported. Check what's the version defined in that file and install it by running `rbenv install x.x.x`, then, from inside the `example` folder, run `rbenv local` to pick that up and set it for your current shell. Lastly, install the Bundler by running `gem install bundler`.

To release a beta, first make sure the required gems are installed by running `bundle install` inside either the `ios` or `android` folder, depending on which platform you're releasing for. You also need to be authenticated with Firebase, you can use the Firebase CLI for that: https://firebase.google.com/docs/cli#sign-in-test-cli. Create a `.env.preview` file based on `.env.example` and set accordingly (we use `spider` for `GR4VY_ID`). The final step is to run the `beta.sh` script:

```sh
./beta.sh ios notes:"Release notes"
```

```sh
./beta.sh android notes:"Release notes"
```

That will get the environment variables defined in `.env.preview` and run fastlane using those variables.

By default, the build number is increased by 1 each time the process is triggered, but you can also set it manually:

```sh
./beta.sh ios notes:"Release notes" build:5
```

That will update the necessary files and create a new commit with message `Bump example iOS version to: {build} [skip ci]`. Then, if on `main` it will create a PR to review and merge, if on any other branch it will push directly.

If everything goes well, you should see the releases in the Firebase App Distribution console.

NOTE: you might have to run `chmod +x ./beta.sh` if the terminal complains about not having the permissions to run the script.
