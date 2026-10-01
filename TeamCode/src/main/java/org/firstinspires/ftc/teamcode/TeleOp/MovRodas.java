package org.firstinspires.ftc.teamcode.TeleOp;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp(name = "MovRodas Mecanum", group = "TeleOp")
public class MovRodas extends OpMode {

    private static final double DEADZONE = 0.05;
    private static final double MV = 0.6; // limita a potência máxima das rodas (0 a 1)

    private DcMotor LMF;
    private DcMotor RMF;
    private DcMotor LMB;
    private DcMotor RMB; // Corrigido de RBM para RMB

    private IMU imu; // Variável da IMU

    private double potenciaFrenteEsquerda;
    private double potenciaFrenteDireita;
    private double potenciaTraseiraEsquerda;
    private double potenciaTraseiraDireita;

    @Override
    public void init() {
        LMF = configurarMotor("leftFront", DcMotorSimple.Direction.FORWARD);
        RMF = configurarMotor("rightFront", DcMotorSimple.Direction.FORWARD);
        LMB = configurarMotor("leftBack", DcMotorSimple.Direction.FORWARD);
        RMB = configurarMotor("rightBack", DcMotorSimple.Direction.FORWARD);

        // Inicialização e configuração da IMU interna do Control Hub
        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
        ));
        imu.initialize(parameters);

        telemetry.addData("Status", "Inicializado com sucesso!");
        telemetry.update();
    }

    @Override
    public void loop() {
        double y = aplicarDeadzone(-gamepad1.left_stick_y);
        double x = aplicarDeadzone(gamepad1.left_stick_x);
        double rx = aplicarDeadzone(gamepad1.right_stick_x);

        potenciaFrenteEsquerda   = y + x + rx;
        potenciaFrenteDireita    = y - x - rx;
        potenciaTraseiraEsquerda = y - x + rx;
        potenciaTraseiraDireita  = y + x - rx;

        normalizarPotencias();

        // Aplicando a potência multiplicada pelo limitador MV (com o '*' corrigido)
        LMF.setPower(potenciaFrenteEsquerda * MV);
        RMF.setPower(potenciaFrenteDireita * MV);
        LMB.setPower(potenciaTraseiraEsquerda * MV);
        RMB.setPower(potenciaTraseiraDireita * MV);

        // Leitura do ângulo da IMU para teste e aprendizado
        double robotHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        // Envia os dados para a Telemetry do Driver Hub
        telemetry.addData("Motores", "FE (%.2f), FD (%.2f), TE (%.2f), TD (%.2f)",
                potenciaFrenteEsquerda, potenciaFrenteDireita,
                potenciaTraseiraEsquerda, potenciaTraseiraDireita);
        telemetry.addData("IMU Heading (graus)", "%.2f", robotHeading);
        telemetry.update();
    }

    @NonNull
    private DcMotor configurarMotor(String nomeNoConfig, DcMotorSimple.Direction direcao) {
        DcMotor motor = hardwareMap.get(DcMotor.class, nomeNoConfig);
        motor.setDirection(direcao);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return motor;
    }

    private double aplicarDeadzone(double valor) {
        return Math.abs(valor) < DEADZONE ? 0 : valor;
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