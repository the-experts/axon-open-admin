const appContextPath = "__APP_CONTEXT_PATH__";
function getContentPath() {
    if (appContextPath.startsWith("__")) {
        return "axon-admin"
    }
    return appContextPath
}

export const contextPath = getContentPath()
