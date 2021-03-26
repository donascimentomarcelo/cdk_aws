package com.myorg;

import software.amazon.awscdk.core.App;
import software.amazon.awscdk.services.ec2.Vpc;

import java.util.Arrays;

public class CursoAwsApp {

    private static final String VPC_NAME = "Vpc";
    private static final String CLUSTER_NAME = "Cluster";
    private static final String SERVICE_NAME_01 = "Service01";
    private static final String SERVICE_NAME_02 = "Service02";
    private static final String RDS_NAME = "Rds";
    private static final String SNS_NAME = "Sns";

    public static void main(final String[] args) {
        App app = new App();

        final VpcStack vpcStack = new VpcStack(app, VPC_NAME);

        final ClusterStack clusterStack = new ClusterStack(app, CLUSTER_NAME, vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        final RdsStack rdsStack = new RdsStack(app, RDS_NAME, vpcStack.getVpc());
        rdsStack.addDependency(vpcStack);

        final SnsStack snsStack = new SnsStack(app, SNS_NAME);

        final Service01Stack service01Stack = new Service01Stack(app, SERVICE_NAME_01, clusterStack.getCluster(), snsStack.getProductEventTopic());
        service01Stack.addDependency(clusterStack);
        service01Stack.addDependency(rdsStack);
        service01Stack.addDependency(snsStack);

        final Service02Stack service02Stack = new Service02Stack(app, SERVICE_NAME_02, clusterStack.getCluster(), snsStack.getProductEventTopic());
        service02Stack.addDependency(snsStack);

        app.synth();
    }
}
