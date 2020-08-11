package com.zhaisoft.lib.updater.model;

import java.io.Serializable;

/**
 * @author zhai
 */
public class VOApk implements Serializable {

    private static final long serialVersionUID = -9055744158390533168L;

    public String apk = "";
    public String version_name = "";
    public String info = "";
    public boolean force_update = false;

}


