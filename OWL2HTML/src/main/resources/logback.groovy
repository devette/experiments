statusListener(OnConsoleStatusListener)

def appenderList = []
appenderList.add("CONSOLE")

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%msg"
    }
}

root(INFO, appenderList)