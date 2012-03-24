/*
    Copyright (c) 2009, Interactive Pulp, LLC
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright
          notice, this list of conditions and the following disclaimer.
        * Redistributions in binary form must reproduce the above copyright
          notice, this list of conditions and the following disclaimer in the
          documentation and/or other materials provided with the distribution.
        * Neither the name of Interactive Pulp, LLC nor the names of its
          contributors may be used to endorse or promote products derived from
          this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
*/

// Global functions accessed via LiveConnect

function pulpcore_getCookie(name) {
    name = name + "=";

    var i;
    if (document.cookie.substring(0, name.length) == name) {
        i = name.length;
    }
    else {
        i = document.cookie.indexOf('; ' + name);
        if (i == -1) {
            return null;
        }
        i += name.length + 2;
    }

    var endIndex = document.cookie.indexOf('; ', i);
    if (endIndex == -1) {
        endIndex = document.cookie.length;
    }

    return unescape(document.cookie.substring(i, endIndex));
}

function pulpcore_setCookie(name, value, expireDate, path, domain, secure) {
    var expires = new Date();

    if (expireDate == null) {
        // Expires in 90 days
        expires.setTime(expires.getTime() + (24 * 60 * 60 * 1000) * 90);
    }
    else {
        expires.setTime(expireDate);
    }

    document.cookie =
        name + "=" + escape(value) +
        "; expires=" + expires.toGMTString() +
        ((path) ? "; path=" + path : "") +
        ((domain) ? "; domain=" + domain : "") +
        ((secure) ? "; secure" : "");
}

function pulpcore_deleteCookie(name, path, domain) {
    document.cookie = name + "=" +
        ((path) ? "; path=" + path : "") +
        ((domain) ? "; domain=" + domain : "") +
        "; expires=Thu, 01-Jan-70 00:00:01 GMT";
}

function pulpcore_appletLoaded(id) {
    if (id == null) {
        id = 0;
    }
    var self = PulpCore.applets[id];
    if (self != null) {
        self.show();
        PulpCore.notifyServer("loaded");
    }
}

// Internal PulpCore code

var PulpCore = window.PulpCore || {

    applets: [ ],

    version: 0.11,

    // Max time (milliseconds) to show the splash gif; after this time the applet is shown.
    // Usually the applet calls pulpcore_appletLoaded() before the timeout.
    splashTimeout: 15000,

    // Arguments for Java 6u10 (plugin2)
    javaArguments: "-Dsun.awt.noerasebackground=true -Djnlp.packEnabled=true",

    // The minimum JRE version
    requiredJRE: "1.4",

    // The minimum JRE version, formatted for IE
    ieRequiredJRE: "1,4,0,0",

    firstLoad: true,

    // Util functions

    getInstallMessage: function() {
        return window.pulpcore_text_install || "To play, install Java now.";
    },

    getEnableMessage: function() {
        return window.pulpcore_text_enable || "To play, enable Java in the Options or Preferences menu.";
    },

    splashLoaded: function(splash, id) {
        this.applets[id].splashLoaded(splash);
    },

    playClicked: function(splash, id) {
        splash.onclick = "";
        splash.onload = 'PulpCore.splashLoaded(this,' + id + ')';
        splash.style.cursor = "default";
        splash.src = window.pulpcore_splash || "splash.gif";
        var applet = this.applets[id];
        setTimeout(function() { applet.insertApplet(); }, 1000);
    },

    // Gets the codebase from the document URL
    getCodeBase: function() {
        var codeBase = document.URL;
        if (codeBase.length <= 7 || codeBase.substr(0, 7) != "http://") {
            return "";
        }
        if (codeBase.charAt(codeBase.length - 1) != '/') {
            var index = codeBase.lastIndexOf('/');
            // Don't include the http://
            if (index > 7) {
                codeBase = codeBase.substring(0, index + 1);
            }
            else {
                codeBase += '/';
            }
        }
        return codeBase;
    },

    /**
        Compares two versions in the form "x.x.x_x".

        Returns 1 if versionA is greater than versionB, -1 if versionA is less than versionB,
        and 0 if versionA is equal to versionB
    */
    compareVersions: function(versionA, versionB) {
        // Make sure both versions are strings
        versionA += '';
        versionB += '';

        var a = versionA.split(/\.|_/);
        var b = versionB.split(/\.|_/);
        var len = Math.max(a.length, b.length);
        for (var i = 0; i < len; i++) {
            if (i >= a.length) {
                a[i] = 0;
            }
            if (i >= b.length) {
                b[i] = 0;
            }
            var ai = parseInt(a[i]);
            var bi = parseInt(b[i]);
            if (ai > bi) {
                return 1;
            }
            if (ai < bi) {
                return -1;
            }
        }

        return 0;
    },

    /* Experimental logging.
     * Performs a HEAD request on pulpcore.js?notify=<type>
     * To enable, set pulpcore_notify to true in your html. */
    notifyServer: function(type) {
        var allowNotify = window.pulpcore_notify || false;
        if (allowNotify) {
            var xmlhttp;
            try {
                xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
            }
            catch (e1) { }
            if (!xmlhttp) {
                try {
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                catch (e2) { }
            }
            if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
                try {
                    xmlhttp = new XMLHttpRequest();
                }
                catch (e3) { }
            }
            if (!xmlhttp && window.createRequest) {
                try {
                    xmlhttp = window.createRequest();
                }
                catch (e4) { }
            }

            if (xmlhttp) {
                xmlhttp.open("HEAD", "pulpcore.js?notify=" + type, true);
                xmlhttp.send(null);
            }
        }
    }
};

// PulpCore applet class

PulpCore.applet = function() {
    this.id = PulpCore.applets.length;
    this.appletHTML = "";
    this.appletInserted = false;

    PulpCore.applets[this.id] = this;
};

PulpCore.applet.prototype = {

    show: function() {
        this.hideSplash();
        var self = this;
        setTimeout(function() { self.showObject(); }, 50);
    },

    hideSplash: function() {
        var splash = document.getElementById('pulpcore_splash' + this.id);
        splash.style.display = "none";
        splash.style.visibility = "hidden";
    },

    showObject: function() {
        if (PulpCore.System.browserIsMozillaFamily) {
            var spacer = document.getElementById('pulpcore_spacer' + this.id);
            spacer.style.display = "none";
            spacer.style.visibility = "hidden";
        }
        var gameContainer = document.getElementById('pulpcore_game' + this.id);
        gameContainer.style.display = "block";
        gameContainer.style.visibility = "visible";
        if (PulpCore.System.browserName == "Explorer") {
            gameContainer.style.position = "static";
        }
    },

    splashLoaded: function(splash) {
        // Prevent this call from occurring again
        // (IE will continue to call onLoad() if the splash loops.)
        if (splash != null) {
            splash.onload = "";
        }
        var self = this;
        setTimeout(function() { self.insertApplet(); }, 50);
    },

    insertApplet: function() {
        if (this.appletInserted) {
            return;
        }
        var gameContainer = document.getElementById('pulpcore_game' + this.id);
        var self = this;
        if (gameContainer == null) {
            setTimeout(function() { self.insertApplet(); }, 500);
        }
        else {
            this.appletInserted = true;
            gameContainer.innerHTML = this.appletHTML;
            this.appletHTML = "";
            setTimeout(function() { pulpcore_appletLoaded(self.id); },
                navigator.javaEnabled() ? PulpCore.splashTimeout : 10);
        }
    },

    getObjectHTML: function() {

        if (!PulpCore.JRE.isAcceptable()) {
            if (this.id === 0) {
                return PulpCore.JRE.installLatest();
            }
            else {
                return "";
            }
        }

        // The splash image to show during JRE boot and jar loading
        var loadSplash =  window.pulpcore_splash || "splash.gif";
        // The splash image to show if the app doesn't autoplay'
        var playSplash = window.pulpcore_play_splash || "play.gif";
        var autoPlayPref = window.pulpcore_start || "auto";
        var autoPlay = autoPlayPref === "true" ||
            (PulpCore.applets.length == 1 && autoPlayPref === "auto");

        // Applet attributes and parameters
        var code     = window.pulpcore_class || "pulpcore.platform.applet.CoreApplet.class";
        var width    = window.pulpcore_width || 640;
        var height   = window.pulpcore_height || 480;
        var archive  = window.pulpcore_archive || "project.jar";
        var bgcolor  = window.pulpcore_bgcolor || "#000000";
        var fgcolor  = window.pulpcore_fgcolor || "#aaaaaa";
        var scene    = window.pulpcore_scene || "";
        var assets   = window.pulpcore_assets || "";
        var params   = window.pulpcore_params || { };
        var codebase = window.pulpcore_codebase || PulpCore.getCodeBase();
        var name     = window.pulpcore_name || params.name || "";

        if (name === "") {
            // Use the archive name
            var index = archive.indexOf('.');
            var index2 = archive.indexOf('-');
            if (index2 != -1 && index2 < index) {
                index = index2;
            }
            if (index == -1) {
                name = archive;
            }
            else {
                name = archive.substring(0, index);
            }
        }

        // Create the object tag parameters
        var objectParams =
            '  <param name="code" value="' + code + '" />\n' +
            '  <param name="archive" value="' + archive + '" />\n' +
            '  <param name="name" value="' + name + '" />\n' +
            '  <param name="mayscript" value="true" />\n' +
            '  <param name="scriptable" value="true" />\n' +
            '  <param name="boxbgcolor" value="' + bgcolor + '" />\n' +
            '  <param name="boxfgcolor" value="' + fgcolor + '" />\n' +
            '  <param name="boxmessage" value="" />\n' +
            '  <param name="codebase_lookup" value="false" />\n' +
            '  <param name="pulpcore_id" value="' + this.id + '" />\n' +
            '  <param name="pulpcore_browser_name" value="' + PulpCore.System.browserName + '" />\n' +
            '  <param name="pulpcore_browser_version" value="' + PulpCore.System.browserVersion + '" />\n';

        // For Java 6u10 plugin2
        if (PulpCore.JRE.isPlugin2()) {
            var args = PulpCore.javaArguments;

            // NOTE: fastest performance of the software renderer on Windows is by
            // disabling both D3D and BufferStrategy
            // See http://bugs.sun.com/view_bug.do?bug_id=6652116
            if (PulpCore.System.osName == "Windows") {
                args += " -Dsun.java2d.d3d=false";
                objectParams += '  <param name="pulpcore_use_bufferstrategy" value="false" />\n';
            }

            objectParams +=
                '  <param name="boxborder" value="false" />\n' +
                '  <param name="image" value="' + loadSplash + '" />\n' +
                '  <param name="centerimage" value="true" />\n' +
                '  <param name="separate_jvm" value="true" />\n' +
                '  <param name="java_arguments" value="' + args + '" />\n';
        }

        // For the PulpCore app
        if (codebase.length > 0) {
            objectParams += '  <param name="codebase" value="' + codebase + '" />\n';
        }
        if (scene.length > 0) {
            objectParams += '  <param name="scene" value="' + scene + '" />\n';
        }
        if (assets.length > 0) {
            objectParams += '  <param name="assets" value="' + assets + '" />\n';
        }
        for (var i in params) {
            if (i !== "name") {
                objectParams += '  <param name="' + i + '" value="' + params[i] + '" />\n';
            }
        }
        objectParams += '  ' + PulpCore.JRE.getInstallHTML();

        // Create the Object tag.
        if (PulpCore.System.browserName == "Explorer") {
            var extraAttributes = '';
            var cabURL = PulpCore.JRE.getJavaCAB[PulpCore.System.osIsOldWindows ? 0 : 1];
            if (PulpCore.compareVersions(PulpCore.System.browserVersion, "7") < 0 &&
                parent.frames.length > 0)
            {
                // On IE6 and older, if the site is externally framed, LiveConnect will not work.
                // However, IE can use onfocus to emulate the appletLoaded() behavior
                extraAttributes = '  onfocus="pulpcore_appletLoaded(' + this.id + ');"\n';
            }
            // Use the <object> tag instead of <applet>. IE users without Java can get the
            // latest JRE without leaving the page or restarting the browser.
            this.appletHTML =
                '<object id="pulpcore_object' + this.id + '"\n' +
                '  classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"\n' +
                '  codebase="' + cabURL + '#Version=' + PulpCore.ieRequiredJRE + '"\n' +
                extraAttributes +
                '  width="' + width + '" height="' + height + '">\n' +
                objectParams +
                '</object>';
        }
        else {
            if (PulpCore.System.osName == "Windows" &&
                PulpCore.System.browserName == "Safari" &&
                PulpCore.compareVersions(PulpCore.System.browserVersion, "522.11") >= 0)
            {
                // Known versions: 522.11, 522.12, 522.15, 523.12
                // Safari 3 beta on Windows doesn't recognize the archive param when
                // the <object> tag is used. For now, use the <applet> tag.
                // See http://joliclic.free.fr/html/object-tag/en/object-java.html
                // LiveConnect also does not work.
                var extraSafariAttributes = "";
                if (codebase.length > 0) {
                    extraSafariAttributes += '  codebase="' + codebase + '"\n';
                }
                this.appletHTML =
                '<applet id="pulpcore_object' + this.id + '"\n' +
                extraSafariAttributes +
                '  code="' + code + '"\n' +
                '  archive="' + archive + '"\n' +
                '  width="' + width + '"\n' +
                '  height="' + height + '" mayscript="true">\n' +
                objectParams +
                '</applet>';
            }
            else {
                // Firefox, Safari, Opera, Mozilla, etc.
                // Note: the minimum version string is ignored on the Mozilla family
                this.appletHTML =
                '<object id="pulpcore_object' + this.id + '"\n' +
                '  classid="java:' + code + '"\n' +
                '  type="application/x-java-applet;version=' + PulpCore.requiredJRE + '"\n' +
                '  width="' + width + '" height="' + height + '">\n' +
                objectParams +
                '</object>';
            }
        }

        var onload;
        var onclick;
        var splash;
        if (autoPlay) {
            splash = loadSplash;
            onload = 'PulpCore.splashLoaded(this,' + this.id + ')';
            onclick = "";
            // In case splash.onLoad() is never called
            var self = this;
            setTimeout(function() { self.insertApplet(); }, 1000);
        }
        else {
            splash = playSplash;
            onload = '';
            onclick = 'PulpCore.playClicked(this,' + this.id + ')';
        }
        var splashHTML = this.getSplashHTML(width, height, splash, onload, onclick);

        return '<div style="margin: auto; overflow: hidden; text-align: left; ' +
            'width: ' + width + 'px; height: ' + height + 'px; ' +
            'background: ' + bgcolor + '">\n' +
            splashHTML +
            '</div>\n';
    },

    getSplashHTML: function(width, height, splash, onload, onclick) {
        var splashHTML;
        var extraAttributes = "";
        if (onclick.length > 0) {
            extraAttributes = 'style="cursor: pointer" onclick="' + onclick + '"';
        }
        if (PulpCore.System.browserName == "Explorer") {
            // Explorer has special code for centering the splash. Also, the game is
            // positioned far left to avoid flicker when the applet is displayed.
            splashHTML =
                '<div id="pulpcore_splash' + this.id + '"\n' +
                '  style="width: ' + width + 'px; ' +
                'height: ' + height + 'px; position: relative; overflow: hidden; ' +
                'text-align: center">\n' +
                '  <div style="position: absolute; top: 50%; left: 50%;">\n' +
                '    <img alt="" src="' + splash + '"\n' +
                '  onload="' + onload +'" ' + extraAttributes +
                '    style="position: relative; top: -50%; left: -50%;" />\n' +
                '  </div>\n' +
                '</div><div id="pulpcore_game' + this.id + '" style="position: relative; left: -4000px;"></div>\n';
        }
        else {
            var spacer = "";
            if (PulpCore.System.browserIsMozillaFamily) {
                // Prevents white flash on Firefox
                spacer = '<div id="pulpcore_spacer' + this.id + '" style="height: 100%">&nbsp;</div>\n';
            }
            splashHTML =
                '<div id="pulpcore_splash' + this.id + '"\n' +
                '  style="width: ' + width + 'px; ' +
                'height: ' + height + 'px; text-align: center; ' +
                'display: table-cell; vertical-align: middle">\n' +
                '  <img alt="" src="' + splash + '"\n' +
                '  onload="' + onload +'" ' + extraAttributes + '/>\n' +
                '</div>\n' +
                spacer +
                '<div id="pulpcore_game' + this.id + '" style="visibility: hidden"></div>\n';
        }
        return splashHTML;
    }
};

// JRE detection

PulpCore.JRE = window.PulpCore.JRE || {

    // The URL to the CAB of the latest JRE (for IE)
    // See http://java.sun.com/javase/6/docs/technotes/guides/deployment/deployment-guide/autodl-files.html
    // First is for Windows9x, second is For XP/2000/Vista/etc.
    getJavaCAB: [
        "http://java.sun.com/update/1.5.0/jinstall-1_5_0_17-windows-i586.cab",
        "http://java.sun.com/update/1.6.0/jinstall-6u13-windows-i586.cab"
    ],

    // The URL to the page to visit to install Java
    getJavaURL: "http://java.sun.com/webapps/getjava/BrowserRedirect?host=java.com" +
        '&type=kernel&returnPage=' + document.location,

    // Deployment Toolkit mime types (old and new)
    mimeTypes: [
        'application/npruntime-scriptable-plugin;DeploymentToolkit',
        'application/java-deployment-toolkit'
    ],

    init: function() {
        if (PulpCore.System.browserName == "Explorer") {
            document.write('<' +
                'object classid="clsid:CAFEEFAC-DEC7-0000-0000-ABCDEFFEDCBA" ' +
                'id="deploymentToolkit" width="0" height="0">' +
                '<' + '/' + 'object' + '>');
        }
        else if (PulpCore.System.browserIsMozillaFamily &&
            navigator.mimeTypes != null)
        {
            var written = false;
            for (var m = 0; !written && m < this.mimeTypes.length; m++) {
                var mimeType = this.mimeTypes[m];
                for (var i = 0; i < navigator.mimeTypes.length; i++) {
                    if (navigator.mimeTypes[i].type == mimeType) {
                        if (navigator.mimeTypes[i].enabledPlugin) {
                            document.write('<' +
                                'embed id="deploymentToolkit" type="' +
                                mimeType + '" hidden="true" />');
                            written = true;
                        }
                    }
                }
            }
        }
    },

    isPlugin2: function() {
        // Chrome can only run plugin2
        if (PulpCore.System.browserName == "Chrome") {
            return true;
        }
        var deploymentToolkit = document.getElementById('deploymentToolkit');
        if (deploymentToolkit != null) {
            try {
                return deploymentToolkit.isPlugin2();
            }
            catch (err) {
                // Fall through
            }
        }
        return false;
    },

    /**
        Returns true if the installed JRE is Java 1.4 or newer.
    */
    isAcceptable: function() {
        var version;
        var i;

        if (PulpCore.System.browserName == "Explorer") {
            // IE can install via the CAB
            return true;
        }
        else if (PulpCore.System.browserName == "Safari" && PulpCore.System.osName == "Windows") {
            // Can't detect the version - let Safari on Windows handle it
            return true;
        }
        else if (PulpCore.System.browserName == "Safari" &&
            navigator.plugins && navigator.plugins.length)
        {
            for (i = 0; i < navigator.plugins.length; i++) {
                var s = navigator.plugins[i].description;
                // Based on code from the Deployment Toolkit
                if (s.search(/^Java Switchable Plug-in/) != -1) {
                    return true;
                }

                var m = s.match(/^Java (1\.4\.2|1\.5|1\.6|1\.7).* Plug-in/);
                if (m != null) {
                    version = m[1];
                    if (PulpCore.JRE.isAcceptableVersion(version)) {
                        return true;
                    }
                }
            }
            return false;
        }
        else if (navigator.mimeTypes && navigator.mimeTypes.length &&
            PulpCore.System.browserIsMozillaFamily)
        {
            version = PulpCore.JRE.getHighestInstalledViaMimeTypes();
            return PulpCore.JRE.isAcceptableVersion(version);
        }
        else if (PulpCore.System.browserName == "Chrome") {
            // Chrome requires 1.6.0_10 as the minimum.
            // So, if the mime type is available, return true.
            if (navigator.mimeTypes && navigator.mimeTypes.length) {
                for (i = 0; i < navigator.mimeTypes.length; i++) {
                    if (navigator.mimeTypes[i].type == "application/x-java-applet") {
                        return true;
                    }
                }
            }
            return false;
        }
        else {
            // Couldn't detect - let the browser handle it
            return true;
        }
    },

    getHighestInstalledViaMimeTypes: function() {
        var version = "0.0";
        var mimeType = "application/x-java-applet;version=";
        for (var i = 0; i < navigator.mimeTypes.length; i++) {
            var s = navigator.mimeTypes[i].type;
            if (s.substr(0, mimeType.length) == mimeType) {
                var testVersion = s.substr(mimeType.length);
                if (PulpCore.compareVersions(testVersion, version) == 1) {
                    version = testVersion;
                }
            }
        }
        return version;
    },

    isAcceptableVersion: function(version) {
        var result = PulpCore.compareVersions(version, PulpCore.requiredJRE);
        return (result >= 0);
    },

    installLatest: function() {
        //if (PulpCore.System.browserName != "Explorer") {
        //    location.href = PulpCore.JRE.getJavaURL;
        //}
        return PulpCore.JRE.getInstallHTML();
    },

    getInstallHTML: function() {
        var message;
        if (navigator.javaEnabled()) {
            message = PulpCore.getInstallMessage();
        }
        else {
            message = PulpCore.getEnableMessage();
        }
        return '<p style="text-align: center">' +
            '<a href="' + PulpCore.JRE.getJavaURL + '">' +
            message + '</a></p>\n';
    }
};

// Browser detection
// Based on a script from Peter-Paul Koch at QuirksMode:
// http://www.quirksmode.org/js/detect.html
// Added browserIsMozillaFamily and osIsOldWindows
// Up-to-date as of September 7, 2008

PulpCore.System = window.PulpCore.System || {
    init: function () {
        this.browserName = this.searchString(this.dataBrowser) || "An unknown browser";
        this.browserVersion = this.searchVersion(navigator.userAgent) ||
            this.searchVersion(navigator.appVersion) ||
            "an unknown version";
        this.osName = this.searchString(this.dataOS) || "an unknown OS";
        this.browserIsMozillaFamily =
            this.browserName == "Netscape" ||
            this.browserName == "Mozilla" ||
            this.browserName == "Firefox";
        this.osIsOldWindows = false;
        if (this.osName == "Windows") {
            var ua = navigator.userAgent.toLowerCase();
            if (ua.search(/win98/) != -1 ||
                ua.search(/windows\s98/) != -1 ||
                ua.search(/windows\sme/) != -1 ||
                ua.search(/windows\s95/) != -1 ||
                ua.search(/win95/) != -1 ||
                ua.search(/nt\s4\.0/) != -1 ||
                ua.search(/nt4\.0/) != -1)
            {
                this.osIsOldWindows = true;
            }
        }
    },
    searchString: function (data) {
        for (var i = 0; i < data.length; i++) {
            var dataString = data[i].string;
            var dataProp = data[i].prop;
            this.versionSearchString = data[i].versionSearch || data[i].identity;
            if (dataString) {
                if (dataString.indexOf(data[i].subString) != -1) {
                    return data[i].identity;
                }
            }
            else if (dataProp) {
                return data[i].identity;
            }
        }
        return "";
    },
    searchVersion: function (dataString) {
        var index = dataString.indexOf(this.versionSearchString);
        if (index == -1) {
            return 0;
        }
        return parseFloat(dataString.substring(index+this.versionSearchString.length+1));
    },
    dataBrowser: [
        {
            string: navigator.userAgent,
            subString: "Chrome",
            identity: "Chrome"
        },
        {
            string: navigator.userAgent,
            subString: "OmniWeb",
            versionSearch: "OmniWeb/",
            identity: "OmniWeb"
        },
        {
            string: navigator.vendor,
            subString: "Apple",
            identity: "Safari"
        },
        {
            prop: window.opera,
            identity: "Opera"
        },
        {
            string: navigator.vendor,
            subString: "iCab",
            identity: "iCab"
        },
        {
            string: navigator.vendor,
            subString: "KDE",
            identity: "Konqueror"
        },
        {
            string: navigator.userAgent,
            subString: "Firefox",
            identity: "Firefox"
        },
        {
            string: navigator.vendor,
            subString: "Camino",
            identity: "Camino"
        },
        {   // for newer Netscapes (6+)
            string: navigator.userAgent,
            subString: "Netscape",
            identity: "Netscape"
        },
        {
            string: navigator.userAgent,
            subString: "MSIE",
            identity: "Explorer",
            versionSearch: "MSIE"
        },
        {
            string: navigator.userAgent,
            subString: "Gecko",
            identity: "Mozilla",
            versionSearch: "rv"
        },
        {   // for older Netscapes (4-)
            string: navigator.userAgent,
            subString: "Mozilla",
            identity: "Netscape",
            versionSearch: "Mozilla"
        }
    ],
    dataOS: [
        {
            string: navigator.platform,
            subString: "Win",
            identity: "Windows"
        },
        {
            string: navigator.platform,
            subString: "Mac",
            identity: "Mac"
        },
        {
            string: navigator.platform,
            subString: "Linux",
            identity: "Linux"
        }
    ]

};

if (PulpCore.firstLoad === true) {
    PulpCore.firstLoad = false;
    PulpCore.System.init();
    PulpCore.JRE.init();
}

document.write(new PulpCore.applet().getObjectHTML());
