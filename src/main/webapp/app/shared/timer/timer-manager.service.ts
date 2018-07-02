import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class TimerManagerService {

    private beginTimerSubject = new BehaviorSubject(0);
    private timerEndedOSubject = new BehaviorSubject(false);
    beginTimerObservable = this.beginTimerSubject.asObservable();
    timerEndedObservable = this.timerEndedOSubject.asObservable();

    constructor() {}
    beginTimer(timeInSec: number) {
        this.beginTimerSubject.next(timeInSec);
    }

    endTimer() {
        this.timerEndedOSubject.next(true);
        this.beginTimerSubject.next(0);
    }

}
