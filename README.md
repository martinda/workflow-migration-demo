A Demo of how to migrate a plugin to Pipeline
====

The goal is to understand how to convert a regular Jenkins plugin to a
Pipeline plugin. The Pipeline plugin will use a `GlobalConfiguration`.
Care must be taken to make sure the new `GlobalConfiguration` is able
to read the old global configuration data.

