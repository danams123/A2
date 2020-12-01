package bgu.spl.mics.application.services;
import java.util.LinkedList;
import java.util.List;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;



/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {

    private Diary d;

    public C3POMicroservice(Diary _d) {
        super("C3PO");
        d = _d;
    }

    @Override
    protected void initialize() {
        this.subscribeEvent(AttackEvent.class, c -> {
            List<Ewok> toRelease = new LinkedList<>();
            for(int serial : c.getSerials()){
                Ewok e = c.getEwoks().getEwoksList().get(serial -1);
                try {
                    e.acquire();
                }
                catch(InterruptedException i){}
                toRelease.add(e);
            }
            try{
                Thread.sleep(c.getDuration());}
            catch(InterruptedException i){}
            d.setC3POFinish(System.currentTimeMillis() - d.getStartTime());
            for(Ewok elem : toRelease){
                elem.release();
            }
            this.complete(c,c.getResult());
        });
        this.subscribeBroadcast(TerminateBroadcast.class, c -> {
            d.setC3POTerminate(System.currentTimeMillis() - d.getStartTime());
            this.terminate();
        });
    }
}
