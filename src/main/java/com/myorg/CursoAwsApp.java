package com.myorg;

import software.amazon.awscdk.core.App;

import java.util.Arrays;

public class CursoAwsApp {
    public static void main(final String[] args) {
        App app = new App();

        new CursoAwsStack(app, "CursoAwsStack");

        app.synth();
    }
}
