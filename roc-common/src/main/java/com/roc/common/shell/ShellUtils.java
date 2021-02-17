package com.roc.common.shell;

import java.io.IOException;
import java.util.Date;

public class ShellUtils {

    public static void runShell(String path) {
        Process process = null;

        long timeout = 10 * 1000;
        try {
            process = Runtime.getRuntime().exec(path);

            CommandStreamGobbler errorGobbler = new CommandStreamGobbler(process.getErrorStream(), path, "ERR");
            CommandStreamGobbler outputGobbler = new CommandStreamGobbler(process.getInputStream(), path, "STD");

            errorGobbler.start();
            // 必须先等待错误输出ready再建立标准输出
            while (!errorGobbler.isReady()) {
                Thread.sleep(10);
            }
            outputGobbler.start();
            while (!outputGobbler.isReady()) {
                Thread.sleep(10);
            }

            CommandWaitForThread commandThread = new CommandWaitForThread(process);
            commandThread.start();

            long commandTime = new Date().getTime();
            long nowTime = new Date().getTime();
            boolean timeoutFlag = false;
            while (!commandIsFinish(commandThread, errorGobbler, outputGobbler)) {
                if (nowTime - commandTime > timeout) {
                    timeoutFlag = true;
                    break;
                } else {
                    Thread.sleep(100);
                    nowTime = new Date().getTime();
                }
            }
            if (timeoutFlag) {
                // 命令超时
                errorGobbler.setTimeout(1);
                outputGobbler.setTimeout(1);
                System.out.println("正式执行命令：" + path + "超时");
            } else {
                // 命令执行完成
                errorGobbler.setTimeout(2);
                outputGobbler.setTimeout(2);
            }

            while (true) {
                if (errorGobbler.isReadFinish() && outputGobbler.isReadFinish()) {
                    break;
                }
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    private static boolean commandIsFinish(CommandWaitForThread commandThread, CommandStreamGobbler errorGobbler, CommandStreamGobbler outputGobbler) {
        if (commandThread != null) {
            return commandThread.isFinish();
        } else {
            return (errorGobbler.isReadFinish() && outputGobbler.isReadFinish());
        }
    }
} 