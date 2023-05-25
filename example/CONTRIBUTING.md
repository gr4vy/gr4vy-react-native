# Contributing

## Development workflow

See [README.md][../README.md] for details on how to run the app in development

## Beta releases

We use [fastlane](https://fastlane.tools/) to orchestrate the build / release process of example app for internal testing. The distribution is handled by [Firebase App Distribution](https://firebase.google.com/docs/app-distribution); logging-in with the `code@gr4vy.com` Google account will take you to the console where you can see the project with its registered iOS and Android apps.

To release a beta, first make sure the required gems are installed by running `bundle install`. You also need to be authenticated with Firebase, you can use the Firebase CLI for that: https://firebase.google.com/docs/cli#sign-in-test-cli. Create a `.env.preview` file based on `.env.example` and set accordingly (we use `spider` for `GR4VY_ID`). The final step is to run the `beta.sh` script:

```sh
./beta.sh ios notes:"Release notes"
```

```sh
./beta.sh android notes:"Release notes"
```

That will get the environment variables defined in `.env.preview` and run fastlane using those variables.

If everything goes well, you should see the releases in the Firebase App Distribution console.

NOTE: you might have to run `chmod +x ./beta.sh` if the terminal complains about not having the permissions to run the script.
