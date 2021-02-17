package com.roc.common.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class CommandStreamGobbler extends Thread {

    private InputStream is;

    private String command;

    private String prefix = "";

    private boolean readFinish = false;

    private boolean ready = false;

    // 命令执行结果,0:执行中 1:超时 2:执行完成
    private int commandResult = 0;

    private List<String> infoList = new LinkedList<String>();

    CommandStreamGobbler(InputStream is, String command, String prefix) {
        this.is = is;
        this.command = command;
        this.prefix = prefix;
    }

    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            ready = true;
            while (commandResult != 1) {
                if (br.ready() || commandResult == 2) {
                    if ((line = br.readLine()) != null) {
                        infoList.add(line);
                    } else {
                        break;
                    }
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (IOException | InterruptedException ioe) {
            System.out.println("正式执行命令：" + prefix + command + "有IO异常");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
            } catch (IOException ioe) {
                System.out.println("正式执行命令：" + prefix + command + "有IO异常");
            }
            readFinish = true;
        }
    }

    public InputStream getIs() {
        return is;
    }

    public String getCommand() {
        return command;
    }

    public boolean isReadFinish() {
        return readFinish;
    }

    public boolean isReady() {
        return ready;
    }

    public List<String> getInfoList() {
        return infoList;
    }

    public void setTimeout(int timeout) {
        this.commandResult = timeout;
    }
}  