package bgu.spl.mics.application.passiveObjects;


public class GeneratedInput {
    private final Attack[] attacks;
    private final long R2D2;
    private final long Lando;
    private final int Ewoks;

    public GeneratedInput(Attack[] attacks, long R2D2, long Lando, int Ewoks) {
        this.attacks = attacks;
        this.R2D2 = R2D2;
        this.Lando = Lando;
        this.Ewoks = Ewoks;
    }

    public Attack[] getAttacks(){return attacks;}
    public long getR2D2(){return R2D2;}
    public long getLando(){return Lando;}
    public int getEwoks(){return Ewoks;}
}
