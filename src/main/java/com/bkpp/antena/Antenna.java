package com.bkpp.antena;

import com.bkpp.signals.SinusoidalSignal;
import com.bkpp.signals.TermSignal;
import lombok.AllArgsConstructor;
import com.bkpp.signals.Signal;

@AllArgsConstructor

public class Antena {
    private final static double TERM = 2.0;
    private final static double AMPLITUDE = 2.0;
    private final static double DURATION = 2.0;
    private final static double START_TIME = 0.0;
    private final static double FREQUENCY = 2000;
    private double speed;
    private double translation;

    public Signal sendedSignal(){
        TermSignal sinus = new SinusoidalSignal();
        sinus.setTerm(TERM);
        sinus.setAmplitude(AMPLITUDE);
        sinus.setDuration(DURATION);
        sinus.setStartTime(START_TIME);
        sinus.setFrequency(FREQUENCY);

        return sinus;
    }

    public Signal receiveSignal(){
        TermSignal sinus = new SinusoidalSignal();
        sinus.setTerm(TERM);
        sinus.setAmplitude(AMPLITUDE);
        sinus.setDuration(DURATION + translation);
        sinus.setStartTime(START_TIME + translation);
        sinus.setFrequency(FREQUENCY);

        return sinus;
    }
}
