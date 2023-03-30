# Embed React Native example application

[![GitHub license](https://img.shields.io/github/license/Naereen/StrapDown.js.svg)](https://github.com/gr4vy/secure-fields/blob/main/LICENSE)

Simple React Native application demonstrating the usage of Embed.

## Usage

Before launching the example application, run `yarn` from the project's root to install all the required dependencies.

Make sure `GR4VY_ID` and `TOKEN` are set in the `.env` file (you can copy `.env.example` and update accordingly). If you later change any of these variables, you have to reset Metro's cache by running `yarn start --reset-cache`, otherwise the old values would still be used.

Running `yarn ios` or `yarn android` from the package folder will start the app on either an iOS or Android simulator.
