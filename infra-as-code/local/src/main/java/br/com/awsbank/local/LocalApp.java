package br.com.awsbank.local;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;

public class LocalApp {
    public static void main(final String[] args) {
        App app = new App();
        new LocalKeycloakStack(app, "LocalStack", StackProps.builder().build());
        app.synth();
    }
}

