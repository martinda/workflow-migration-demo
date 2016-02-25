package org.jenkins.plugins.demo;

import hudson.Extension;
import java.util.List;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.Items;

@Extension
public class DemoGlobalConfig extends GlobalConfiguration {

    private String globalVar;

    private List<MyString> myStrings;

    public static DemoGlobalConfig get() {
        System.out.println("DemoGlobalConfig.get()");
        return GlobalConfiguration.all().get(DemoGlobalConfig.class);
    }

    public DemoGlobalConfig() {
        System.out.println("DemoGlobalConfig()");
        load();
    }
/*

    // I tried it here too, does not work
    @Initializer(before = InitMilestone.PLUGINS_STARTED)
    public static void addAliases() {
        System.out.println("Alias: "+Demo1.DescriptorImpl.class.getName()+" becomes "+DemoGlobalConfig.class.getName());
        Items.XSTREAM2.addCompatibilityAlias(Demo1.DescriptorImpl.class.getName(), DemoGlobalConfig.class);
    }
*/
    @Override
    public boolean configure(StaplerRequest req, JSONObject json)
    throws FormException
    {
        req.bindJSON(this, json);
        save();
        return true;
    }

    public String getGlobalVar() {
        System.out.println("DemoGlobalConfig.getGlobalVar()");
        return globalVar;
    }

    @DataBoundSetter
    public void setGlobalVar(String globalVar) {
        System.out.println("DemoGlobalConfig.setGlobalVar()");
        this.globalVar = globalVar;
    }

    public List<MyString> getMyStrings() {
        System.out.println("DemoGlobalConfig.getMyStrings() -> "+myStrings);
        if (myStrings != null) {
            for (MyString s : myStrings)  {
                System.out.println("value: "+s.getValue());
            }
        }
        return myStrings;
    }

    @DataBoundSetter
    public void setMyStrings(List<MyString> myStrings) {
        System.out.println("DemoGlobalConfig.setMyStrings("+myStrings+")");
        this.myStrings = myStrings;
    }
}
