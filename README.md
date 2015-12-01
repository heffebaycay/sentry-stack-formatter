# Sentry Stack Formatter

Sentry Stack Formatter is a plugin that allows you to import Java stack traces logged by [Sentry](https://getsentry.com/)
into IntelliJ.
Using this plugin you will be able to use the stack trace as if it was actually thrown from IntelliJ and benefit from
the ability to jump directly to the code referenced in the stack trace.

## Installation instruction

This plugin is available on JetBrains plugins repository : <TBD>

You can compile this plugin yourself by cloning this repository and executing the following command:
```
./gradlew buildPlugin
```

This will generate a zip file that you can directly import in IntelliJ.

## How to use

After the plugin is installed, just call the _Analyze Stacktrace_ IntelliJ action, select _Sentry Stack Formatter_ as
the stacktrace unscrambler and paste the JSON export of the Sentry stacktrace you want to import into IntelliJ.

## License

This project is released under the MIT License. For more information, please refer to the LICENSE.txt file.