package org.firstinspires.ftc.teamcode.TeleOp;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;


@TeleOp(name = "FieldCentric RR")
public class FieldCentricRR extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive drive =
                new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        double offset = 0;

        waitForStart();

        while (opModeIsActive()) {

            // Atualiza a localização do robô
            drive.updatePoseEstimate();

            // Botão OPTIONS = redefine o "frente do campo"
            if (gamepad1.options) {
                offset = drive.localizer.getPose().heading.toDouble();
            }

            // Joystick esquerdo
            Vector2d input = new Vector2d(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x
            );

            // Heading atual do robô
            Rotation2d botHeading =
                    Rotation2d.exp(
                            drive.localizer.getPose().heading.toDouble()
                                    - offset
                    );

            // Converte o joystick para Field Centric
            Vector2d rotated =
                    botHeading.inverse().times(input);

            // Movimento + rotação
            drive.setDrivePowers(
                    new PoseVelocity2d(
                            rotated,
                            -gamepad1.right_stick_x
                    )
            );

            telemetry.addData(
                    "Heading",
                    Math.toDegrees(botHeading.toDouble())
            );

            telemetry.update();
        }
    }
}