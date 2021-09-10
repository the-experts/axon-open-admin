import {Card, Popover, Typography} from "antd";
import {QuestionCircleOutlined} from "@ant-design/icons";
import TokenStatusContainer from "../components/tokens/TokenStatusContainer";
import React, {useEffect} from "react";
import {startTokenFetching, stopTokenFetching} from "../redux/tokens/fetcher";

export function TokenManagementPage() {
    useEffect(() => {
        startTokenFetching()
        return () => stopTokenFetching()
    }, [])
    return <Card title={<div>
        <Popover
            placement={"right"}
            style={{float: 'right'}}
            content={
                <Typography.Text>Here you can find the status of the segments of each token. You can find a description of each possible action
                    by
                    hovering
                    over it. Good luck!</Typography.Text>}>
            <QuestionCircleOutlined/></Popover> Token status
    </div>}>
        <TokenStatusContainer/>
    </Card>;
}
