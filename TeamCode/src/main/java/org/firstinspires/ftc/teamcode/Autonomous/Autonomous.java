package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous", group = "Autonomous")
public class Autonomous extends OpMode {

    private static final double POTENCIA = 0.6;

    private DcMotor LMF, RMF, LMB, RBM, motorIntOut;
    private ElapsedTime tempoPasso = new ElapsedTime();
    private int passo = 0;

    @Override
    public void init() {
        LMF = configurarMotor("LMF", DcMotorSimple.Direction.FORWARD);
        RMF = configurarMotor("RMF", DcMotorSimple.Direction.FORWARD);
        LMB = configurarMotor("LMB", DcMotorSimple.Direction.FORWARD);
        RBM = configurarMotor("RMB", DcMotorSimple.Direction.REVERSE);
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
            case 0: // Vai pra direita (30,5 cm) pra alinhar com o centro da lateral
                direita();
                avancarSe(202);
                break;

            case 1: // Sobe (355,5 cm) até a altura das bolas
                frente();
                avancarSe(2357);
                break;

            case 2: // Vai pra esquerda (~142 cm) até as bolas
                esquerda();
                avancarSe(942);
                break;

            case 3: // Avança um pouco pra encostar nas bolinhas
                frente();
                avancarSe(100); // ~15 cm, AJUSTAR
                break;

            case 4: // Liga intake (pra dentro)
                motorIntOut.setPower(1.0);
                avancarSe(500);
                break;

            case 5: // Desce (233,5 cm) até a altura do Depósito Azul
                motorIntOut.setPower(0);
                tras();
                avancarSe(1549);
                break;

            case 6: // Vai pra esquerda pra alinhar com a entrada do depósito
                esquerda();
                avancarSe(66); // ~10 cm, AJUSTAR
                break;

            case 7: // Liga outtake (pra fora)
                motorIntOut.setPower(-1.0);
                avancarSe(500);
                break;

            case 8: // Vai pra esquerda (~91,5 cm) até a lateral esquerda do campo
                motorIntOut.setPower(0);
                esquerda();
                avancarSe(607);
                break;

            case 9: // Sobe (233,5 cm) até o estacionamento, ao lado das stones
                frente();
                avancarSe(1549);
                break;

            default: // Fim do autônomo
                parar();
                break;
        }

        telemetry.addData("Passo", passo);
        telemetry.addData("Tempo no passo (ms)", tempoPasso.milliseconds());
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
        setPotencias(POTENCIA, POTENCIA, POTENCIA, POTENCIA);
    }

    private void tras() {
        setPotencias(-POTENCIA, -POTENCIA, -POTENCIA, -POTENCIA);
    }

    private void esquerda() {
        setPotencias(-POTENCIA, POTENCIA, POTENCIA, -POTENCIA);
    }

    private void direita() {
        setPotencias(POTENCIA, -POTENCIA, -POTENCIA, POTENCIA);
    }

    private void setPotencias(double LMF, double RMF, double LMB, double RBM) {
        this.LMF.setPower(LMF);
        this.RMF.setPower(RMF);
        this.LMB.setPower(LMB);
        this.RBM.setPower(RBM);
    }

    private void parar() {
        setPotencias(0, 0, 0, 0);
    }

    private DcMotor configurarMotor(String nome, DcMotorSimple.Direction direcao) {
        DcMotor motor = hardwareMap.get(DcMotor.class, nome);
        motor.setDirection(direcao);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        return motor;
    }
}