import store from "../store";
import {fetchEvents} from "./EventsSlice";

let intervalEvents: any;

export function startEventFetching(aggregateId?: string) {
    if(intervalEvents != null) {
        stopEventFetching()
    }
// @ts-ignore TODO
    intervalEvents = setInterval(() => store.dispatch(fetchEvents(aggregateId)), 1000)
// @ts-ignore TODO
    store.dispatch(fetchEvents(aggregateId))
}

export function stopEventFetching() {
    if (intervalEvents) {
        clearInterval(intervalEvents)
    }
}
