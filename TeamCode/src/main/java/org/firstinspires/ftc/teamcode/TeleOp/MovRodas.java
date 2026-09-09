//package org.firstinspires.ftc.teamcode.TeleOp;
//
//import androidx.annotation.NonNull;
//
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//
//@TeleOp(name = "MovRodas Mecanum", group = "TeleOp")
//public class MovRodas extends OpMode {
//
//    private static final double DEADZONE = 0.05;
//    private static final double MV = 0.6; // limita a potência máxima das rodas (0 a 1)
//
//    private DcMotor LMF;
//    private DcMotor RMF;
//    private DcMotor LMB;
//    private DcMotor RBM;
//    private DcMotor motorIntOut;
//
//    private double potenciaFrenteEsquerda;
//    private double potenciaFrenteDireita;
//    private double potenciaTraseiraEsquerda;
//    private double potenciaTraseiraDireita;
//
//    @Override
//    public void init() {
//        LMF         = configurarMotor("LMF", DcMotorSimple.Direction.FORWARD);
//        RMF         = configurarMotor("RMF", DcMotorSimple.Direction.FORWARD);
//        LMB         = configurarMotor("LMB", DcMotorSimple.Direction.FORWARD);
//        RBM         = configurarMotor("RMB", DcMotorSimple.Direction.REVERSE);
//        motorIntOut = configurarMotor("INT", DcMotorSimple.Direction.FORWARD);
//
//        telemetry.addData("Status", "Inicializado com sucesso!");
//        telemetry.update();
//    }
//
//    @Override
//    public void loop() {
//        double y = aplicarDeadzone(-gamepad1.left_stick_y);
//        double x = aplicarDeadzone(gamepad1.left_stick_x);
//        double rx = aplicarDeadzone(gamepad1.right_stick_x);
//
//        potenciaFrenteEsquerda   = y + x + rx;
//        potenciaFrenteDireita    = y - x - rx;
//        potenciaTraseiraEsquerda = y - x + rx;
//        potenciaTraseiraDireita  = y + x - rx;
//
//        normalizarPotencias();
//
//        LMF.setPower(potenciaFrenteEsquerda*MV);
//        RMF.setPower(potenciaFrenteDireita*MV);
//        LMB.setPower(potenciaTraseiraEsquerda*MV);
//        RBM.setPower(potenciaTraseiraDireita*MV);
//
//        double potenciaIntOut = calcularPotenciaIntOut();
//        motorIntOut.setPower(potenciaIntOut);
//
//        telemetry.addData("Motores", "FE (%.2f), FD (%.2f), TE (%.2f), TD (%.2f)",
//                potenciaFrenteEsquerda, potenciaFrenteDireita,
//                potenciaTraseiraEsquerda, potenciaTraseiraDireita);
//        telemetry.addData("IntOut", "%.2f", potenciaIntOut);
//        telemetry.update();
//    }
//
//    @NonNull
//    private DcMotor configurarMotor(String nomeNoConfig, DcMotorSimple.Direction direcao) {
//        DcMotor motor = hardwareMap.get(DcMotor.class, nomeNoConfig);
//        motor.setDirection(direcao);
//        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        return motor;
//    }
//
//    private double aplicarDeadzone(double valor) {
//        return Math.abs(valor) < DEADZONE ? 0 : valor;
//    }
//
//    private double calcularPotenciaIntOut() {
//        if (gamepad1.right_trigger > 0.1) {
//            return -1.0;
//        } else if (gamepad1.left_trigger > 0.1) {
//            return 1.0;
//        } else {
//            return 0.0;
//        }
//    }
//
//    private void normalizarPotencias() {
//        double max = Math.max(1, Math.max(
//                Math.max(Math.abs(potenciaFrenteEsquerda), Math.abs(potenciaFrenteDireita)),
//                Math.max(Math.abs(potenciaTraseiraEsquerda), Math.abs(potenciaTraseiraDireita))));
//
//        potenciaFrenteEsquerda   /= max;
//        potenciaFrenteDireita    /= max;
//        potenciaTraseiraEsquerda /= max;
//        potenciaTraseiraDireita  /= max;
//    }
//}