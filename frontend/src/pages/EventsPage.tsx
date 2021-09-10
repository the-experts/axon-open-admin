import {Card, Space} from "antd";
import EventTableContainer from "../components/events/EventTableContainer";
import React from "react";

export function EventsPage() {
    return <Card title={<div>Last 50 events</div>}>
        <Space direction={"vertical"}>
            <EventTableContainer/>
        </Space>
    </Card>;
}
