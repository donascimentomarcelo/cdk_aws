package com.myorg;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.services.ec2.Vpc;

import java.util.Arrays;

public class CursoAwsApp {

    private static final String VPC_NAME = "Vpc";
    private static final String CLUSTER_NAME = "Cluster";
    private static final String SERVICE_NAME = "Service";

    public static void main(final String[] args) {
        App app = new App();

        final VpcStack vpcStack = new VpcStack(app, VPC_NAME);

        final ClusterStack clusterStack = new ClusterStack(app, CLUSTER_NAME, vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        final RdsStack rdsStack = new RdsStack(app, "Rds", vpcStack.getVpc());
        rdsStack.addDependency(vpcStack);

        final SnsStack snsStack = new SnsStack(app, "Sns");

        final Service01Stack service01Stack = new Service01Stack(app, SERVICE_NAME, clusterStack.getCluster());
        service01Stack.addDependency(clusterStack);
        service01Stack.addDependency(rdsStack);

        app.synth();
    }
}
