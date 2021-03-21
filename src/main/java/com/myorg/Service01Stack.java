package com.myorg;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.applicationautoscaling.EnableScalingProps;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.logs.LogGroup;

public class Service01Stack extends Stack {
    public Service01Stack(final Construct scope, final String id, final Cluster cluster) {
        this(scope, id, null, cluster);
    }

    public Service01Stack(final Construct scope, final String id, final StackProps props, final Cluster cluster) {
        super(scope, id, props);

        ApplicationLoadBalancedFargateService service01 = ApplicationLoadBalancedFargateService.Builder.create(this, "ALB01")
                    .serviceName("servico-01")
                    .cluster(cluster)
                    .cpu(512)
                    .desiredCount(2)
                    .listenerPort(8080)
                    .memoryLimitMiB(1024)
                    .taskImageOptions(
                            ApplicationLoadBalancedTaskImageOptions
                                .builder()
                                .containerName("aws_project")
                                .image(ContainerImage.fromRegistry("donascimentomarcelo/awsproject:1.0.0"))
                                .containerPort(8080)
                                .logDriver(LogDriver.awsLogs(AwsLogDriverProps.builder()
                                    .logGroup(LogGroup.Builder.create(this, "Service01LogGroup")
                                        .logGroupName("Service01")
                                        .removalPolicy(RemovalPolicy.DESTROY)
                                        .build())
                                    .streamPrefix("Service01")
                                    .build())
                        ).build())
                .publicLoadBalancer(true)
                .build();

        service01.getTargetGroup().configureHealthCheck(
            new HealthCheck
                    .Builder()
                    .path("/actuator/health")
                    .healthyHttpCodes("200")
                    .build()
        );

        ScalableTaskCount scalableTaskCount = service01
                .getService()
                .autoScaleTaskCount(EnableScalingProps.builder()
                        .minCapacity(2)
                        .maxCapacity(4)
                        .build()
        );

        scalableTaskCount.scaleOnCpuUtilization("Service01AutoScaling", CpuUtilizationScalingProps.builder()
                .targetUtilizationPercent(50)
                .scaleInCooldown(Duration.seconds(60))
                .scaleOutCooldown(Duration.seconds(60))
                .build());
    }
}
