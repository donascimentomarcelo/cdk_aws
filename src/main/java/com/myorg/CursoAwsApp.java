package com.myorg;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.services.ec2.Vpc;

import java.util.Arrays;

public class CursoAwsApp {

    private static final String VPC_NAME = "Vpc";
    private static final String CLUSTER_NAME = "Cluster";

    public static void main(final String[] args) {
        App app = new App();

        final VpcStack vpcStack = new VpcStack(app, VPC_NAME);

        final ClusterStack clusterStack = new ClusterStack(app, CLUSTER_NAME, vpcStack.getVpc());

        clusterStack.addDependency(vpcStack);

        app.synth();
    }
}
