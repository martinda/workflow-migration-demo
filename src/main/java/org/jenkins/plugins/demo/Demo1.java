package org.jenkins.plugins.demo;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.lang.InterruptedException;

import jenkins.model.GlobalConfiguration;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

public class Demo1 extends Builder {

    private String input1;

    @DataBoundConstructor
    public Demo1(String input1)
    {
        System.out.println("Demo1("+input1+")");
        this.input1 = input1;
    }

    public String getInput1() {
        System.out.println("getInput1() -> "+input1);
        return input1;
    }

    @Override
    public boolean perform(
        AbstractBuild<?,?> build,
        Launcher launcher,
        BuildListener listener
    ) throws IOException, InterruptedException
    {
        final PrintStream logger = listener.getLogger();
        logger.println("Input 1: "+input1);
        logger.println("Global Var: "+getDescriptor().globalVar);
        List<MyString> strings = getDescriptor().myStrings;
        logger.println("Strings: "+strings);
        if (strings != null) {
          for (MyString s : strings) {
              logger.println("String value: "+s.getValue());
          }
        }
        return true;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        // Global variables only
        private String globalVar;
        private List<MyString> myStrings;

        // We have 3 methods that are NOT for global variables
        public DescriptorImpl() {
            System.out.println("DescriptorImpl::load()");
            load();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Demo1";
        } 

        // Global variables handled here
        @Override
        public boolean configure(StaplerRequest req, JSONObject json)
        throws FormException
        {
            System.out.println("DescriptorImpl.configure()");
            req.bindJSON(this, json);
            save();
            return true;
        }
 
        public String getGlobalVar() {
            System.out.println("DescriptorImpl.getGlobalVar()");
            return globalVar;
        }
 
        @DataBoundSetter
        public void setGlobalVar(String globalVar) {
            System.out.println("DescriptorImpl.setGlobalVar()");
            this.globalVar = globalVar;
        }
 
        public List<MyString> getMyStrings() {
            System.out.println("DescriptorImpl.getMyStrings() -> "+myStrings);
            if (myStrings != null) {
                for (MyString s : myStrings)  {
                    System.out.println("value: "+s.getValue());
                }
            }
            return myStrings;
        }
 
        @DataBoundSetter
        public void setMyStrings(List<MyString> myStrings) {
            System.out.println("Descriptor.setMyStrings("+myStrings+")");
            this.myStrings = myStrings;
        }
    }
}
