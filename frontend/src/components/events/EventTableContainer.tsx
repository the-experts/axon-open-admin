import React, {useEffect} from "react";
import {useSelector} from "react-redux";
import {useParams} from "react-router-dom";
import {eventTailSelector} from "../../redux/events/EventsSlice";
import {startEventFetching, stopEventFetching} from "../../redux/events/fetcher";
import EventTable from "./EventTable";

function EventTableContainer() {
    const tail = useSelector(eventTailSelector)
    const {aggregateId} = useParams<{aggregateId: string}>()

    useEffect(() => {
        startEventFetching(aggregateId)
        return () => stopEventFetching()
    }, [aggregateId])


    return <EventTable rows={tail}/>
}

export default EventTableContainer;
