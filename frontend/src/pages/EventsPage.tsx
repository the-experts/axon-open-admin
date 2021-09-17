import {Card, Space} from "antd";
import React from "react";
import {useParams} from "react-router-dom";
import EventTableContainer from "../components/events/EventTableContainer";

export function EventsPage() {
    const {aggregateId} = useParams<{ aggregateId: string }>()
    const title = aggregateId ? "Events of aggregate: " + aggregateId : "Tailing last 50 events"
    return <Card title={
        <div>{title}</div>}>
        <Space direction={"vertical"}>
            <EventTableContainer/>
        </Space>
    </Card>;
}
