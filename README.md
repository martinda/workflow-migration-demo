A Demo of how to migrate a plugin to Pipeline
====

The goal is to understand how to convert a traditional Jenkins plugin to
a Pipeline plugin. The Pipeline plugin will use a `GlobalConfiguration`.
Care must be taken to make sure the new `GlobalConfiguration` is able
to read the old global configuration data.

The old configuration data is stored in an XML file, named after
the value returned by the `DescriptorImpl.getDisplayName()` method
(i.e. `org.jenkins.plugins.demo.Demo1.xml`).

The old configuration top level XML element name is:

```
<org.jenkins.plugins.demo.Demo1_-DescriptorImpl>
```

It points to the `Demo1.DescriptorImpl` class where the global variables
used to be declared, before the introduction of the `DemoGlobalConfig`
class. To maintain backward compatibility, the new version of the plugin
must map the top level XML element to the new `DemoGlobalConfig` class.

According to [Hint on retaining backward
compatibility](https://wiki.jenkins-ci.org/display/JENKINS/Hint+on+retaining+backward+compatibility),
this can be accomplished by using an alias. For this demo project,
we would thus call the `addAliases()` in the old `DescriptorImpl` class
like so:

```
public class Demo1 extends Builder {

   ...

   @Extension
   public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

       ...

        @Initializer(before = InitMilestone.PLUGINS_STARTED)
        public static void addAliases() {
            Items.XSTREAM2.addCompatibilityAlias(DescriptorImpl.class.getName(), DemoGlobalConfig.class);
        }

        ...
    }
}

```

To verify that the alias works, the following test checks that the
`DemoGlobalConfig` object was populated with the values from the
XML configuration file. The test uses the `@LocalData` annotation
and reads the old configuration which was previously saved to
`src/test/resources/org/jenkins/plugins/demo/Demo1Test/backwardsCompat/org.jenkins.plugins.demo.Demo1.xml`.

```
mvn -Dtest=Demo1Test#backwardsCompat test
```

At this time, the test fails. I tried placing the `addAliases()` method
inside the new `DemoGlobalConfiguration` to no avail.
