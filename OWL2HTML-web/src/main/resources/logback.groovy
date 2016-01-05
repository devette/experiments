statusListener(OnConsoleStatusListener)

def appenderList = ["ROLLING"]
def WEBAPP_DIR = "./target"
def consoleAppender = true;

// does hostname match prod?
if (hostname =~ /prod/) {
    WEBAPP_DIR = "/opt/myapp"
    consoleAppender = false
} else {
    appenderList.add("CONSOLE")
}

if (consoleAppender) {
    appender("CONSOLE", ConsoleAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
        }
    }
}

appender("ROLLING", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        Pattern = "%d %level %thread %mdc %logger - %m%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        FileNamePattern = "${WEBAPP_DIR}/log/owl2html-web-%d{yyyy-MM}.zip"
    }
}

root(INFO, appenderList)