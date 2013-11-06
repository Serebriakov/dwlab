package dwlab.java;

import main.TurnExample;
import playn.core.PlayN;
import playn.java.JavaPlatform;

public class testJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register( config );
    PlayN.run( new TurnExample() );
  }
}
