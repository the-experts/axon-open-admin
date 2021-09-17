import React, {useCallback, useState} from "react";
import {Button, Popover} from "antd";
import {MergeCellsOutlined} from "@ant-design/icons";
import {contextPath} from "../../../context";
import {TokenOverviewData} from "../TokenOverviewData";

async function mergeProcessor(name: string, segment: number, attempt = 1) {
    const result = await fetch(`${contextPath}/processor/${name}/merge/${segment}`, {method: 'POST'})
    if (!result.ok) {
        if (attempt > 5) {
            return;
        }
        await mergeProcessor(name, segment, attempt + 1)
    }
}


export function MergeAction({row}: { row: TokenOverviewData }) {
    const [loading, setLoading] = useState(false)

    const onSplitAction = useCallback(async () => {
        setLoading(true)
        await mergeProcessor(row.processorName, row.segment)
        setLoading(false)
    }, [row.processorName, row.segment])

    return <Popover content={<p>Merges the segment with its closest relative (segment {row.mergeableSegment}), creating one token out of two. <br/>Effectively reduces active event processor threads by one.</p>}
                    placement={"bottom"}>
        <Button type="default" loading={loading} onClick={onSplitAction} disabled={row.owner == null || row.mergeableSegment === row.segment || !row.isMergable}>
            <MergeCellsOutlined/>
        </Button>
    </Popover>
}
