import {Table} from "antd";
import React from "react";
import { Link } from "react-router-dom";
import {contextPath} from "../../context";
import {EventModel} from "../../redux/events/models";

function ProcessorTable({rows}: { rows: EventModel[] }) {
    const mappedRows = rows.map(row => Object.assign({}, row, {key: row.aggregate + '_' + row.index}))
    return (
        <Table dataSource={mappedRows} pagination={false} size={"small"} tableLayout={"fixed"}>
            <Table.Column title="Sequence" key="globalSequence" dataIndex="globalSequence"/>
            <Table.Column title="Aggregate" key="aggregate" dataIndex="aggregate" render={(id) => <Link to={contextPath + "/events/" + id}>{id}</Link>}/>
            <Table.Column title="Index" key="index" dataIndex="index"/>
            <Table.Column title="Timestamp" key="timestamp" dataIndex="timestamp"/>
            <Table.Column title="Name" key="payloadType" dataIndex="payloadType"/>
            <Table.Column title="Payload" key="payload" dataIndex="payload"/>
        </Table>
    );
}


export default ProcessorTable;
