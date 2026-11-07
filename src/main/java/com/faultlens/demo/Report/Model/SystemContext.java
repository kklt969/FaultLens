package com.faultlens.demo.Report.Model;

import java.util.List;

public class SystemContext {


    private String systemName;
    private List<String> components;
    private List<String> dependencies;
    private List<String> communicationPaths;

    public SystemContext() {
    }

    public SystemContext(
            String systemName,
            List<String> components,
            List<String> dependencies,
            List<String> communicationPaths) {
        this.systemName = systemName;
        this.components = components;
        this.dependencies = dependencies;
        this.communicationPaths = communicationPaths;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public List<String> getCommunicationPaths() {
        return communicationPaths;
    }

    public void setCommunicationPaths(List<String> communicationPaths) {
        this.communicationPaths = communicationPaths;
    }


}
