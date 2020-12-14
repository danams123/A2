package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * A class implementing {@link Broadcast}. When sending an object of this type, all the microservices subscribed to it
 * will call terminate() to terminate their event loop.
 */
public class TerminateBroadcast implements Broadcast {

    public TerminateBroadcast(){}
}
