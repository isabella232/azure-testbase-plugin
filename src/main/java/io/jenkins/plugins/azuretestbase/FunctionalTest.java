package io.jenkins.plugins.azuretestbase;

import java.io.Serializable;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import lombok.ToString;

@ToString
public class FunctionalTest  extends AbstractDescribableImpl<FunctionalTest> implements Serializable {

    private String content;
    private boolean applyUpdateBefore;
    private boolean restartAfter;


    @DataBoundConstructor
    public FunctionalTest(String content) {
        this.content = content;
    }
    @DataBoundSetter
    public void setApplyUpdateBefore(boolean applyUpdateBefore) {
        this.applyUpdateBefore = applyUpdateBefore;
    }
    @DataBoundSetter
    public void setRestartAfter(boolean restartAfter) {
        this.restartAfter = restartAfter;
    }


    public String getContent() {
        return content;
    }
    public boolean isApplyUpdateBefore() {
        return applyUpdateBefore;
    }
    public boolean isRestartAfter() {
        return restartAfter;
    }



    @Symbol("functionalTest")
    @Extension
    public static class DescriptorImpl extends Descriptor<FunctionalTest> {
        @NonNull
        public String getDisplayName() {
            return "";
        }

        public FormValidation doCheckContent(@QueryParameter String value) {
            if(value == null || value.equals(""))
                return FormValidation.error(Messages.FunctionalTest_DescriptorImpl_infos_NotEmpty());
            return FormValidation.ok();
        }
    }

    private static final long serialVersionUID = 1L;
}
