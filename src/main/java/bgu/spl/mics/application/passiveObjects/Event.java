package bgu.spl.mics.application.passiveObjects;

import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("custom_naming") private final long totalAttacks;
    private final long HanSoloFinish;
    private final long C3POFinish;
    private final long R2D2Deactivate;
    private final long LeiaTerminate;
    private final long HanSoloTerminate;
    private final long C3POTerminate;
    private final long R2D2Terminate;
    private final long LandoTerminate;

    public Event(long totalAttacks, long HanSoloFinish, long C3POFinish, long R2D2Deactivate, long LeiaTerminate,
                 long HanSoloTerminate, long C3POTerminate, long R2D2Terminate, long LandoTerminate){
        this.totalAttacks = totalAttacks;
        this.HanSoloFinish = HanSoloFinish;
        this.C3POFinish = C3POFinish;
        this.R2D2Deactivate = R2D2Deactivate;
        this.LeiaTerminate = LeiaTerminate;
        this.HanSoloTerminate = HanSoloTerminate;
        this.C3POTerminate = C3POTerminate;
        this.R2D2Terminate = R2D2Terminate;
        this.LandoTerminate = LandoTerminate;
    }
}
