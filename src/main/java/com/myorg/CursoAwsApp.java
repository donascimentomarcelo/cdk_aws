package com.myorg;

import software.amazon.awscdk.core.App;

import java.util.Arrays;

public class CursoAwsApp {

    private static final String VPC_NAME = "Vpc";

    public static void main(final String[] args) {
        App app = new App();

        new VpcStack(app, VPC_NAME);
        app.synth();
    }
}
