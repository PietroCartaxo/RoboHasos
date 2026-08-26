package org.firstinspires.ftc.teamcode.Mecanum;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "MovRodas Mecanum", group = "TeleOp")
public class MovRodas extends OpMode {

    private static final double DEADZONE = 0.05;
    private static final double MV = 0.5; // limita a potência máxima das rodas (0 a 1)

    private DcMotor LMF;
    private DcMotor RMF;
    private DcMotor LMB;
    private DcMotor RBM;
    private DcMotor motorIntOut;

    private double potenciaFrenteEsquerda;
    private double potenciaFrenteDireita;
    private double potenciaTraseiraEsquerda;
    private double potenciaTraseiraDireita;

    @Override
    public void init() {
        LMF         = configurarMotor("LMF", DcMotorSimple.Direction.REVERSE);
        RMF         = configurarMotor("RMF", DcMotorSimple.Direction.FORWARD);
        LMB         = configurarMotor("LMB", DcMotorSimple.Direction.REVERSE);
        RBM         = configurarMotor("RMB", DcMotorSimple.Direction.FORWARD);
        motorIntOut = configurarMotor("INT", DcMotorSimple.Direction.FORWARD);

        telemetry.addData("Status", "Inicializado com sucesso!");
        telemetry.update();
    }

    @Override
    public void loop() {
        double y = aplicarDeadzone(-gamepad1.left_stick_y);
        double x = aplicarDeadzone(gamepad1.left_stick_x);
        double rx = aplicarDeadzone(gamepad1.right_stick_x);

        double r = Math.hypot(x, y);
        double robotAngle = Math.atan2(y, x) - Math.PI / 4;

        potenciaFrenteEsquerda   = r * Math.cos(robotAngle) + rx;
        potenciaFrenteDireita    = r * Math.sin(robotAngle) - rx;
        potenciaTraseiraEsquerda = r * Math.sin(robotAngle) + rx;
        potenciaTraseiraDireita  = r * Math.cos(robotAngle) - rx;

        normalizarPotencias();

        LMF.setPower(potenciaFrenteEsquerda * MV);
        RMF.setPower(potenciaFrenteDireita * MV);
        LMB.setPower(potenciaTraseiraEsquerda * MV);
        RBM.setPower(potenciaTraseiraDireita * MV);

        double potenciaIntOut = calcularPotenciaIntOut();
        motorIntOut.setPower(potenciaIntOut);

        telemetry.addData("Motores", "FE (%.2f), FD (%.2f), TE (%.2f), TD (%.2f)",
                potenciaFrenteEsquerda, potenciaFrenteDireita,
                potenciaTraseiraEsquerda, potenciaTraseiraDireita);
        telemetry.addData("IntOut", "%.2f", potenciaIntOut);
        telemetry.update();
    }

    private DcMotor configurarMotor(String nomeNoConfig, DcMotorSimple.Direction direcao) {
        DcMotor motor = hardwareMap.get(DcMotor.class, nomeNoConfig);
        motor.setDirection(direcao);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return motor;
    }

    private double aplicarDeadzone(double valor) {
        return Math.abs(valor) < DEADZONE ? 0 : valor;
    }

    private double calcularPotenciaIntOut() {
        if (gamepad1.a) {
            return 1.0;
        } else if (gamepad1.b) {
            return -1.0;
        } else {
            return 0.0;
        }
    }

    private void normalizarPotencias() {
        double max = Math.max(1, Math.max(
                Math.max(Math.abs(potenciaFrenteEsquerda), Math.abs(potenciaFrenteDireita)),
                Math.max(Math.abs(potenciaTraseiraEsquerda), Math.abs(potenciaTraseiraDireita))));

        potenciaFrenteEsquerda   /= max;
        potenciaFrenteDireita    /= max;
        potenciaTraseiraEsquerda /= max;
        potenciaTraseiraDireita  /= max;
    }
}