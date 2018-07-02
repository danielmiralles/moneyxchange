import { Component, OnInit } from '@angular/core';
import {Observable} from 'rxjs/Rx';
import { TimerManagerService } from './timer-manager.service';

@Component({
    selector: 'jhi-mini-timer',
    template: `<span>{{hour ? hour : '00'}} h : {{(min) && (min <= 59) ? min : '00'}} m : {{(sec) && (sec <= 59) ? sec : '00'}} s </span>`
})
export class TimerComponent implements OnInit {

    hour: number;
    min: number;
    sec: number;

    count = 0;

    constructor(private timerManager: TimerManagerService) {

    }

    init(seconds: number) {
        this.count = seconds;
        const timer = Observable.timer(0, 1000);
        timer.subscribe((t) => {
            if (this.count > 0) {
                --this.count;
                this.sec = this.getSeconds(this.count);
                this.min = this.getMinutes(this.count);
                this.hour = this.getHours(this.count);
            } else {
                this.timerManager.endTimer();
            }

        });
    }

    private getSeconds(ticks: number) {
        return this.pad(ticks % 60);
    }

    private getMinutes(ticks: number) {
        return this.pad((Math.floor(ticks / 60)) % 60);
    }

    private getHours(ticks: number) {
        return this.pad(Math.floor((ticks / 60) / 60));
    }

    private pad(digit: any) {
        return digit <= 9 ? '0' + digit : digit;
    }

    ngOnInit() {
        this.timerManager.beginTimerObservable.subscribe((timeInSec) => {
            if (timeInSec > 0) {
                this.init(timeInSec);
            }
        });
    }

}
