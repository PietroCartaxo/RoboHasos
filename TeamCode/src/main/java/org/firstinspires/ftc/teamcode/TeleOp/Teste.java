package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class Teste extends OpMode {

    private DcMotor motor;

    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, "motor");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop() {
        if (gamepad1.right_trigger > 0.1) {
            motor.setPower(1.0);
        } else if (gamepad1.left_trigger > 0.1) {
            motor.setPower(-1.0);
        } else {
            motor.setPower(0.0);
        }

        telemetry.addData("Ticks do encoder", motor.getCurrentPosition());
        telemetry.update();
    }
}