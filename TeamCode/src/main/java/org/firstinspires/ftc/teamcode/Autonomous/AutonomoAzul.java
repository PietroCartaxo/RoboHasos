package org.firstinspires.ftc.teamcode.Autonomous;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "AutonomoAzul", group = "Autonomous")
public class AutonomoAzul extends OpMode {

    private static final double MV = 0.4; // limita a potência máxima das rodas (0 a 1)

    private DcMotor LMF, RMF, LMB, RBM, motorIntOut;
    private ElapsedTime tempoPasso = new ElapsedTime();
    private int passo = 0;

    private double potenciaFrenteEsquerda;
    private double potenciaFrenteDireita;
    private double potenciaTraseiraEsquerda;
    private double potenciaTraseiraDireita;

    @Override
    public void init() {
        LMF         = configurarMotor("LMF", DcMotorSimple.Direction.FORWARD);
        RMF         = configurarMotor("RMF", DcMotorSimple.Direction.FORWARD);
        LMB         = configurarMotor("LMB", DcMotorSimple.Direction.FORWARD);
        RBM         = configurarMotor("RMB", DcMotorSimple.Direction.REVERSE);
        motorIntOut = configurarMotor("INT", DcMotorSimple.Direction.FORWARD);

        telemetry.addData("Status", "Pronto");
        telemetry.update();
    }

    @Override
    public void start() {
        tempoPasso.reset();
        passo = 0;
    }

    @Override
    public void loop() {
        switch (passo) {
            case 0: // 60 cm para a direita
                direita();
                avancarSe(1200);
                break;

            case 1: // 245 cm para cima
                frente();
                avancarSe(2600);
                break;

            case 2: // 150 cm para a esquerda
                esquerda();
                avancarSe(1850);
                break;

            case 3: // 72 cm para a frente
                frente();
                avancarSe(650);
                break;

            case 4: // Liga intake pra depositar os cargos
                motorIntOut.setPower(-1.0);
                avancarSe(800);
                break;

            case 5: // Desliga intake, 90 cm para a esquerda
                motorIntOut.setPower(0);
                avancarSe(600);
                break;

            case 6: // 245 cm para baixo
                tras();
                avancarSe(2200);
                break;

            case 7: // 58 cm para a esquerda
                esquerda();
                avancarSe(1000);
                break;

            case 8: // Liga outtake pra depositar os cargos
                motorIntOut.setPower(1.0);
                avancarSe(800);
                break;

            case 9: // Desliga outtake, 90 cm para a esquerda
                motorIntOut.setPower(0);
                esquerda();
                avancarSe(597);
                break;

            case 10: // 229 cm para cima
                frente();
                avancarSe(1519);
                break;

            default: // Fim do autônomo
                parar();
                break;
        }

        telemetry.addData("Passo", passo);
        telemetry.addData("Tempo no passo (ms)", tempoPasso.milliseconds());
        telemetry.addData("Motores", "FE (%.2f), FD (%.2f), TE (%.2f), TD (%.2f)",
                potenciaFrenteEsquerda, potenciaFrenteDireita,
                potenciaTraseiraEsquerda, potenciaTraseiraDireita);
        telemetry.update();
    }

    // ---- Controle dos passos ----

    private void avancarSe(long duracaoMs) {
        if (tempoPasso.milliseconds() >= duracaoMs) {
            parar();
            passo++;
            tempoPasso.reset();
        }
    }

    // ---- Movimentos ----

    private void frente() {
        aplicarPotencias(1, 1, 1, 1);
    }

    private void tras() {
        aplicarPotencias(-1, -1, -1, -1);
    }

    private void esquerda() {
        aplicarPotencias(-1, 1, 1, -1);
    }

    private void direita() {
        aplicarPotencias(1, -1, -1, 1);
    }

    private void aplicarPotencias(double fe, double fd, double te, double td) {
        potenciaFrenteEsquerda   = fe;
        potenciaFrenteDireita    = fd;
        potenciaTraseiraEsquerda = te;
        potenciaTraseiraDireita  = td;

        normalizarPotencias();

        LMF.setPower(potenciaFrenteEsquerda * MV);
        RMF.setPower(potenciaFrenteDireita * MV);
        LMB.setPower(potenciaTraseiraEsquerda * MV);
        RBM.setPower(potenciaTraseiraDireita * MV);
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

    private void parar() {
        aplicarPotencias(0, 0, 0, 0);
    }

    @NonNull
    private DcMotor configurarMotor(String nomeNoConfig, DcMotorSimple.Direction direcao) {
        DcMotor motor = hardwareMap.get(DcMotor.class, nomeNoConfig);
        motor.setDirection(direcao);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return motor;
    }
}