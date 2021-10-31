import {MergeCellsOutlined} from "@ant-design/icons";
import {Button, Popover} from "antd";
import React, {useCallback, useState} from "react";
import {executeCommands} from "../commands/CommandExecutor";
import {MergeSegmentCommand} from "../commands/Commands";
import {TokenOverviewData} from "../TokenOverviewData";

export function MergeAction({row}: { row: TokenOverviewData }) {
    const [loading, setLoading] = useState(false)

    const onSplitAction = useCallback(async () => {
        setLoading(true)
        await executeCommands([
                new MergeSegmentCommand(row.owner, row.processorName, row.segment)
            ]
        )
        setLoading(false)
    }, [row.processorName, row.segment, row.owner])

    return <Popover content={<p>Merges the segment with its closest relative (segment {row.mergeableSegment}), creating one token out of two. <br/>Effectively reduces active event processor threads by one.</p>}
                    placement={"bottom"}>
        <Button type="default" loading={loading} onClick={onSplitAction} disabled={row.owner == null || row.mergeableSegment === row.segment}>
            <MergeCellsOutlined/>
        </Button>
    </Popover>
}
