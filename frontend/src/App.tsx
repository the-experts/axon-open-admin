import React, {useCallback} from 'react';
import 'antd/dist/antd.css';

import {Layout, Menu, Typography} from "antd";
import {Provider} from "react-redux";
import store from "./redux/store";
import {BranchesOutlined, DashboardOutlined, DatabaseOutlined} from "@ant-design/icons";
import {Header} from "antd/es/layout/layout";
import Sider from "antd/es/layout/Sider";
import {BrowserRouter, Route, useHistory, useLocation} from "react-router-dom";
import {TokenManagementPage} from "./pages/TokenManagementPage";
import {ProcessorStatusPage} from "./pages/ProcessorStatusPage";
import {EventsPage} from "./pages/EventsPage";

function AppMenu() {
    const history = useHistory();
    const location = useLocation();
    const onSelectCallback = useCallback(({key}) => {
        history.push(`${key}`)
    }, [history])

    const selectedKey = location.pathname === "/" ? "/tokens" : location.pathname;
    return <Menu
        mode="inline"
        defaultSelectedKeys={[selectedKey]}
        defaultOpenKeys={['/tokens']}
        onSelect={onSelectCallback}
        style={{height: '100%', borderRight: 0}}
    >
        <Menu.Item key="/tokens"><BranchesOutlined/> Token Management</Menu.Item>
        <Menu.Item key="/processors"><DashboardOutlined/> Processor Status</Menu.Item>
        <Menu.Item key="/events"><DatabaseOutlined/> Events log</Menu.Item>
    </Menu>;
}

function App() {
    return (
        <BrowserRouter>
            <Provider store={store}>
                <Layout style={{minHeight: '100vh'}}>
                    <Header className="header">
                        <img src="logo.png" alt={"codecentric Logo"}/>
                        <Typography.Title className={"title"} level={1}>Axon Open Admin</Typography.Title>
                    </Header>
                    <Layout>
                        <Sider width={200} className="site-layout-background">
                            <AppMenu/>
                        </Sider>
                        <Layout style={{padding: '0 24px 24px'}}>
                            <Layout.Content
                                className="site-layout-background"
                                style={{
                                    padding: 24,
                                    margin: 0,
                                    minHeight: 280,
                                }}
                            >

                                <Route path={"/"} exact><TokenManagementPage/></Route>
                                <Route path={"/tokens"}><TokenManagementPage/></Route>
                                <Route path={"/processors"}><ProcessorStatusPage/></Route>
                                <Route path={"/events"}><EventsPage/></Route>
                            </Layout.Content>
                        </Layout>
                    </Layout>
                </Layout>
            </Provider>
        </BrowserRouter>
    );
}

export default App;
