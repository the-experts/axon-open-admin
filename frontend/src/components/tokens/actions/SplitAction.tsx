import {SplitCellsOutlined} from "@ant-design/icons";
import {Button, Popover} from "antd";
import React, {useCallback, useState} from "react";
import {executeCommands} from "../commands/CommandExecutor";
import {SplitSegmentCommand} from "../commands/Commands";
import {TokenOverviewData} from "../TokenOverviewData";

export function SplitAction({row}: { row: TokenOverviewData }) {
    const [loading, setLoading] = useState(false)

    const onSplitAction = useCallback(async () => {
        setLoading(true)
        await executeCommands([
                new SplitSegmentCommand(row.owner, row.processorName, row.segment)
            ]
        )
        setLoading(false)
    }, [row.processorName, row.segment, row.owner])

    return <Popover
        content={<p>Splits the token of this segment in two, increasing active threads of the processor by one. <br/>This action needs a free processing thread on the same node and is disabled when there are none.</p>}
        placement={"bottom"}>
        <Button type="default" loading={loading} onClick={onSplitAction} disabled={row.owner == null || !row.threadsAvailable}>
            <SplitCellsOutlined/>
        </Button>
    </Popover>
}
