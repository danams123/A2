package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private AtomicInteger totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;


    private static class SingletonHolder {
        private static Diary instance = new Diary();
    }

    private Diary(){
        totalAttacks = new AtomicInteger();
        totalAttacks.set(0);
    }

    public static Diary getInstance() {
        return SingletonHolder.instance;
    }

    public void setTotalAttacks(int _totalAttacks){
        int old = totalAttacks.intValue();
        totalAttacks.compareAndSet(old, _totalAttacks);}
    public final int getTotalAttacks(){return totalAttacks.intValue();}

    public void setHanSoloFinish(long _HanSoloFinish){HanSoloFinish = _HanSoloFinish;}
    public final long getHanSoloFinish(){return HanSoloFinish;}

    public void setC3POFinish(long _C3POFinish){C3POFinish = _C3POFinish;}
    public final long getC3POFinish(){return C3POFinish;}

    public void setR2D2Deactivate(long _R2D2Deactivate){R2D2Deactivate = _R2D2Deactivate;}
    public final long getR2D2Deactivate(){return R2D2Deactivate;}

    public void setLeiaTerminate(long _LeiaTerminate){LeiaTerminate = _LeiaTerminate;}
    public final long getLeiaTerminate(){return LeiaTerminate;}

    public void setHanSoloTerminate(long _HanSoloTerminate){HanSoloTerminate = _HanSoloTerminate;}
    public final long getHanSoloTerminate(){return HanSoloTerminate;}

    public void setC3POTerminate(long _C3POTerminate){C3POTerminate = _C3POTerminate;}
    public final long getC3POTerminate(){return C3POTerminate;}

    public void setR2D2Terminate(long _R2D2Terminate){R2D2Terminate = _R2D2Terminate;}
    public final long getR2D2Terminate(){return R2D2Terminate;}

    public void setLandoTerminate(long _LandoTerminate){LandoTerminate = _LandoTerminate;}
    public final long getLandoTerminate(){return LandoTerminate;}

}
