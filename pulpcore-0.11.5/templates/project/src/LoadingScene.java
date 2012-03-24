import pulpcore.Build;
import pulpcore.CoreSystem;
import pulpcore.Stage;

public class LoadingScene extends pulpcore.scene.LoadingScene {
    
    public LoadingScene() {
        super("HelloWorld-" + ProjectBuild.VERSION + ".zip" , new TitleScene());
        
        CoreSystem.setTalkBackField("app.name", "HelloWorld");
        CoreSystem.setTalkBackField("app.version", ProjectBuild.VERSION);
        
        Stage.setUncaughtExceptionScene(new UncaughtExceptionScene());
        Stage.invokeOnShutdown(new Runnable() {
            public void run() {
                // Shutdown network connections, DB connections, etc. 
            }
        });
    }
    
    @Override
    public void load() {
        
        // Deter hotlinking
        String[] validHosts = {
            "pulpgames.net", "www.pulpgames.net", 
        };
        if (!Build.DEBUG && !CoreSystem.isValidHost(validHosts)) {
            CoreSystem.showDocument("http://www.pulpgames.net/");
        }
        else {
            // Start loading the zip
            super.load();
        }
    }
}
